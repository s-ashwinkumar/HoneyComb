package initialfaults;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import lib.AsgService;
import lib.ElbService;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wilsoncao on 7/7/16.
 */
public class ChangeELBHealthCheckTargetFault extends AbstractFault {
  private String faultyHealthCheckTarget;
  private String asgName;
  private AsgService asgService;
  private ElbService elbService;
  private static Loggi logger;

  public ChangeELBHealthCheckTargetFault(HashMap<String, String> params)
      throws IOException {
    super(params);
    this.asgName = params.get("asgName");
    this.faultyHealthCheckTarget = params.get("faultyHealthCheckTarget");
    logger = new Loggi(faultInstanceId, ChangeELBHealthCheckTargetFault.class
        .getName());
  }

  @Override
  public void start() throws Exception {

    logger.start();
    // Get the services
    if (asgService == null) {
      asgService = ServiceFactory.getAsgService(faultInstanceId);
    }
    if (elbService == null) {
      elbService = ServiceFactory.getElbService(faultInstanceId);
    }


    if (this.isTerminated())
      return;
    // Log the fault injection
    logger.log("Injecting fault: Change the ELB Health Check target");

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    if (this.isTerminated())
      return;
    // Get the ELB associated with the ASG
    if (asg.getLoadBalancerNames().isEmpty()) {
      throw new HoneyCombException("The ASG has no ELB attached");
    }
    String elbName = asg.getLoadBalancerNames().get(0);

    if (this.isTerminated())
      return;
    // Update the ELB Health Check target
    try {
      elbService.updateElbHealthCheckTarget(elbName, faultyHealthCheckTarget);
    } catch (IllegalArgumentException e) {
      throw new HoneyCombException(
          "Failed to update Health Check target of ELB " + elbName + ". " +
              "Caused by: " + e);
    }

    // Delay for ELB to become unhealthy
    Thread.sleep(10000);

    logger.finish();
  }

  public void asgServiceSetter(AsgService asgService) {
    this.asgService = asgService;
  }

  public void elbServiceSetter(ElbService elbService) {
    this.elbService = elbService;
  }
}
