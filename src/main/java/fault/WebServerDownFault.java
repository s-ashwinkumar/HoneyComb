package fault;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.ec2.model.Instance;
import lib.AsgService;
import lib.Ec2Service;
import lib.ServiceFactory;
import lib.SshService;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wilsoncao on 7/7/16.
 */
public class WebServerDownFault extends AbstractFault{
  private String faultInstanceId;
  private String sshUser;
  private String sshKeyFilePath;
  private static Loggi logger;
  private String asgName;

  public WebServerDownFault(HashMap<String,String> params) throws IOException {

    super(params);
    this.sshUser = params.get("sshUser");
    this.faultInstanceId = params.get("faultInstanceId");
    this.sshKeyFilePath = params.get("sshKeyFilePath");
    this.asgName = params.get("asgName");
    logger = new Loggi(faultInstanceId,WebServerDownFault.class.getName());
  }

  public void start() throws Exception{
    // Get the Services
    AsgService asgService = ServiceFactory.getAsgService(this.faultInstanceId);
    Ec2Service ec2Service = ServiceFactory.getEc2Service(faultInstanceId);
    SshService sshService = ServiceFactory.getSshService(faultInstanceId);

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    // Get the list of "running" EC2 Instances in the ASG
    // (i.e. the EC2 instances which has state "running")
    List<Instance> ec2RunningInstances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances = asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance : asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance.getInstanceId());
      if (ec2Instance.getState().getName().equals("running")) {
        ec2RunningInstances.add(ec2Instance);
      }
    }

    // If the ASG has any "running" instances
    if (!ec2RunningInstances.isEmpty()) {
      // Randomize an Instance to inject fault
      Collections.shuffle(ec2RunningInstances);
      Instance instanceToInject = ec2RunningInstances.get(0);

      // Log the fault injection
      logger.log("Injecting fault: take down Apache Web Server on EC2 Instance with ID = " +
          instanceToInject.getInstanceId());

      // Inject fault: Take down Web Server on Instance (stop Apache Web Server)
      try {
        List<String> sshCommands = new ArrayList<String>();
        sshCommands.add("sudo /etc/init.d/httpd stop");
        sshService.executeSshCommands(
            instanceToInject.getPublicIpAddress(), sshUser, sshKeyFilePath, sshCommands);
      } catch (IOException e) {
        throw new HoneyCombException("Unable to inject fault to Instance ID = " +
            instanceToInject.getInstanceId() + ". Caused by: " + e);
      }

      // Delay for 30s for ELB to detect the failure
      Thread.sleep(30000);
    }
  }
}
