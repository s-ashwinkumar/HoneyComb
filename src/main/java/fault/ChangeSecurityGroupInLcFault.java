package fault;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
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
public class ChangeSecurityGroupInLcFault extends AbstractFault {
  private String asgName;
  private String faultySecurityGroupName;
  private static Loggi logger;
  private String faultInstanceId;
  private Ec2Service ec2Service;
  private AsgService asgService;

  public ChangeSecurityGroupInLcFault(HashMap<String,String> params) throws IOException{
    super(params);
    this.asgName = params.get("asgName");
    this.faultySecurityGroupName = params.get("faultySecurityGroupName");
    this.faultInstanceId = params.get("faultInstanceId");
    logger = new Loggi(faultInstanceId,ChangeSecurityGroupInLcFault.class.getName());
  }

  public void start() throws Exception{
    logger.start();
    // Get the services
    if(ec2Service == null) {
      ec2Service = ServiceFactory.getEc2Service(this.faultInstanceId);
    }

    if(asgService == null) {
      asgService = ServiceFactory.getAsgService(this.faultInstanceId);
    }

    // Log the fault injection
    logger.log("Injecting fault: Attach new LaunchConfiguration with different Security Group to the ASG");

    // Grab the current Launch Configuration
    if (this.isTerminated())
      return;
    LaunchConfiguration lc = asgService.getLaunchConfigurationForAutoScalingGroup(asgName);
    if (lc == null) {
      throw new HoneyCombException("LC or ASG do not exist");
    }

    if (this.isTerminated())
      return;
    // Create a new LaunchConfiguration based on the current LC with the faulty SG Name
    // and with name "faulty-lc"
    CreateLaunchConfigurationRequest req = new CreateLaunchConfigurationRequest();
    req.withImageId(lc.getImageId()).
        withInstanceType(lc.getInstanceType()).
        withKeyName(lc.getKeyName()).
        withLaunchConfigurationName("faulty-lc").
        withSecurityGroups(faultySecurityGroupName);
    asgService.createLaunchConfiguration(req);

    if (this.isTerminated())
      return;
    // Update the ASG to use the new faulty LC
    asgService.updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");


		/* Terminate 1 random instance in the ASG to trigger the launch of a faulty LC instance */

    // Get the AutoScalingGroup with given Name
    AutoScalingGroup asg = asgService.getAutoScalingGroup(asgName);
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    if (this.isTerminated())
      return;
    // Get the list of "online" EC2 Instances in the ASG
    // (i.e. the EC2 instances which has state "pending" or "running")
    List<Instance> ec2RunningInstances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances = asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance : asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance.getInstanceId());
      if (ec2Instance.getState().getName().equals("pending") ||
          ec2Instance.getState().getName().equals("running")) {
        ec2RunningInstances.add(ec2Instance);
      }
    }

    if (this.isTerminated())
      return;
    // If the ASG has any "online" instances
    if (!ec2RunningInstances.isEmpty()) {

      // Randomize an online Instance to be killed
      Collections.shuffle(ec2RunningInstances);
      Instance instanceToInject = ec2RunningInstances.get(0);

      // Log the action
      logger.log("Faulty LaunchConfiguration with wrong Security Group updated. Terminating instance with id = "
          + instanceToInject.getInstanceId() + " for spawning new instance with faulty LC...");

      // Terminate the instance
      ec2Service.terminateInstance(instanceToInject.getInstanceId());

      // Delay for 10 seconds (ASG EC2 Health Check time) for ASG to spawn new faulty instance
      Thread.sleep(10 * 1000);
      logger.finish();
    }
  }

  public void ec2ServiceSetter(Ec2Service ec2Service){
    this.ec2Service = ec2Service;
  }

  public void asgServiceSetter(AsgService asgService){
    this.asgService = asgService;
  }
}
