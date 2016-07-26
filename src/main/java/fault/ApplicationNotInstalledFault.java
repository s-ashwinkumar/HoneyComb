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
public class ApplicationNotInstalledFault extends AbstractFault {
  private String sshUser;
  private String sshKeyFilePath;
  private String asgName;
  private static Loggi logger;
  private AsgService asgService;
  private Ec2Service ec2Service;
  private SshService sshService;

  public ApplicationNotInstalledFault(HashMap<String, String> params) throws
      IOException {

    super(params);
    sshUser = params.get("sshUser");
    asgName = params.get("asgName");
    sshKeyFilePath = params.get("sshKeyFilePath");
    logger = new Loggi(faultInstanceId, ApplicationNotInstalledFault.class
        .getName());
  }

  public void start() throws Exception {

    logger.start();

    // Get the Services
    if (asgService == null) {
      asgService = ServiceFactory.getAsgService(faultInstanceId);
    }
    if (ec2Service == null) {
      ec2Service = ServiceFactory.getEc2Service(faultInstanceId);
    }
    if (sshService == null) {
      sshService = ServiceFactory.getSshService(faultInstanceId);
    }

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    if (this.isTerminated())
      return;
    // Get the list of "running" EC2 Instances in the ASG
    // (i.e. the EC2 instances which has state "running")
    List<Instance> ec2RunningInstances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances =
        asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance :
        asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance
          .getInstanceId());
      if (ec2Instance.getState().getName().equals("running")) {
        ec2RunningInstances.add(ec2Instance);
      }
    }

    if (this.isTerminated())
      return;
    // If the ASG has any "running" instances
    if (!ec2RunningInstances.isEmpty()) {
      // Randomize an Instance to inject fault
      Collections.shuffle(ec2RunningInstances);
      Instance instanceToInject = ec2RunningInstances.get(0);

      // Log the fault injection
      logger.log("Injecting fault: uninstall Wordpress Application on EC2 " +
          "Instance with ID = " +
          instanceToInject.getInstanceId());

      // Inject fault: Uninstall Application on Instance (uninstall Wordpress)
      if (this.isTerminated())
        return;
      try {
        List<String> sshCommands = new ArrayList<String>();
        sshCommands.add("rm -rf /var/www/html/*");
        sshService.executeSshCommands(
            instanceToInject.getPublicIpAddress(), sshUser, sshKeyFilePath,
            sshCommands);
      } catch (IOException e) {
        throw new HoneyCombException("Unable to inject fault to Instance ID =" +
            " " +
            instanceToInject.getInstanceId() + ". Caused by: " + e);
      }

      // Delay for 10s for ELB to detect the failure
      Thread.sleep(10000);

      logger.finish();
    }
  }

  public void asgServiceSetter(AsgService asgService) {
    this.asgService = asgService;
  }

  public void ec2ServiceSetter(Ec2Service ec2Service) {
    this.ec2Service = ec2Service;
  }

  public void sshServiceSetter(SshService sshService) {
    this.sshService = sshService;
  }

}
