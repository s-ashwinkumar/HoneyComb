package fault;

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
public class ChangeElbHealthCheckTargetFault extends AbstractFault {
  private String faultInstanceId;
  private String faultyHealthCheckTarget;
  private String asgName;
  private static Loggi logger;

  public ChangeElbHealthCheckTargetFault(HashMap<String,String> params) throws IOException {
    super(params);
    this.faultInstanceId = params.get("faultInstanceId");
    this.asgName = params.get("asgName");
    this.faultyHealthCheckTarget = params.get("faultyHealthCheckTarget");
    logger = new Loggi(faultInstanceId,ChangeElbHealthCheckTargetFault.class.getName());
  }

  @Override
  public void start() throws Exception {
    // Get the services
    AsgService asgService = ServiceFactory.getAsgService(faultInstanceId);
    ElbService elbService = ServiceFactory.getElbService(faultInstanceId);

    // Log the fault injection
    logger.log("Injecting fault: Change the ELB Health Check target");

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    // Get the ELB associated with the ASG
    if (asg.getLoadBalancerNames().isEmpty()) {
      throw new HoneyCombException("The ASG has no ELB attached");
    }
    String elbName = asg.getLoadBalancerNames().get(0);

    // Update the ELB Health Check target
    try {
      elbService.updateElbHealthCheckTarget(elbName, faultyHealthCheckTarget);
    } catch (IllegalArgumentException e) {
      throw new HoneyCombException(
          "Failed to update Health Check target of ELB " + elbName + ". Caused by: " + e);
    }

    // Delay for ELB to become unhealthy
    Thread.sleep(30000);
  }
}
