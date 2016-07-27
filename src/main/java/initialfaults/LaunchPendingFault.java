package initialfaults;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.ec2.model.Instance;
import lib.AsgService;
import lib.Ec2Service;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wilsoncao on 7/7/16.
 */
public class LaunchPendingFault extends AbstractFault {
  private String asgName;
  private static Loggi logger;
  private AsgService asgService;
  private Ec2Service ec2Service;

  public LaunchPendingFault(HashMap<String, String> params) throws IOException {

    super(params);
    this.asgName = params.get("asgName");
    logger = new Loggi(faultInstanceId, LaunchPendingFault.class.getName());
  }


  public void start() throws Exception {
    logger.start();
    // Get the Services
    if (asgService == null)
      asgService = ServiceFactory.getAsgService(faultInstanceId);
    if (ec2Service == null)
      ec2Service = ServiceFactory.getEc2Service(faultInstanceId);

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    // Get the list of "online" EC2 Instances in the ASG
    // (i.e. the EC2 instances which has state "pending" or "running")
    List<Instance> ec2RunningInstances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances =
        asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance :
        asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance
          .getInstanceId());
      if (ec2Instance.getState().getName().equals("pending") ||
          ec2Instance.getState().getName().equals("running")) {
        ec2RunningInstances.add(ec2Instance);
      }
    }

    // If the ASG has any "online" instances
    if (!ec2RunningInstances.isEmpty()) {

      // Randomize an online Instance to be injected with failure
      Collections.shuffle(ec2RunningInstances);
      Instance instanceToInject = ec2RunningInstances.get(0);

      // Log the fault injected
      logger.log("Fault injected: tag as Launch Stuck Pending EC2 Instance " +
          "with ID = " +
          instanceToInject.getInstanceId());

      // Inject the fault - "Tag" the instance as: <"launch_status", "pending">
      ec2Service.tagEC2Instance(
          instanceToInject.getInstanceId(), "launch_status", "pending");

    }
    logger.finish();
  }

  public void asgServiceSetter(AsgService asgService) {
    this.asgService = asgService;
  }

  public void ec2ServiceSetter(Ec2Service ec2Service) {
    this.ec2Service = ec2Service;
  }
}
