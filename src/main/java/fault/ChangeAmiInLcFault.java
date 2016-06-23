package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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
 * Created by wilsoncao on 6/16/16.
 */
public class ChangeAmiInLcFault extends AbstractFault {

  private static Loggi logger;
  //  static final Logger logger = LogManager.getLogger(ChangeAmiInLcFault.class.getName());

  private String faultyAmiId;
  private String faultInstanceId;
  private String asgName;
  private Ec2Service ec2Service;
  private AsgService asgService;
  private AutoScalingGroup asg;

  /**
   * The constructor of the class.
   * @param params An HashMap as a parameter.
   */
  public ChangeAmiInLcFault(HashMap<String, String> params) throws IOException {
    super(params);
    this.asgName = params.get("asgName");
    this.faultyAmiId = params.get("faultyAmiId");
    this.faultInstanceId = params.get("faultInstanceId");
    logger = new Loggi(faultInstanceId,ChangeAmiInLcFault.class.getName());
  }

  /**
   * A method that implements the interface
   * @throws AmazonServiceException thrown by AWS API.
   * @throws AmazonClientException thrown by AWS API.
   * @throws HoneyCombException thrown by AWS API.
   */
  @Override
  public void start() throws Exception {

    logger.start();
    // Get the services
    if (ec2Service == null) {
      ec2Service = ServiceFactory.getEc2Service(faultInstanceId);
    }

    if (asgService == null) {
      asgService = ServiceFactory.getAsgService(faultInstanceId);
    }

    // Log the fault injection
    logger.log("Injecting fault: Attach new LaunchConfiguration with different AMI to the ASG");

    // Grab the current Launch Configuration
    LaunchConfiguration lc = asgService.getLaunchConfigurationForAutoScalingGroup(asgName);
    if (lc == null) {
      throw new HoneyCombException("LC or ASG do not exist");
    }

    // Create a new LaunchConfiguration based on the current LC with the faulty AMI ID
    // and with name "faulty-lc"
    CreateLaunchConfigurationRequest req = new CreateLaunchConfigurationRequest();
    req.withImageId(faultyAmiId)
            .withInstanceType(lc.getInstanceType())
            .withKeyName(lc.getKeyName())
            .withLaunchConfigurationName("faulty-lc")
            .withSecurityGroups(lc.getSecurityGroups());
    asgService.createLaunchConfiguration(req);

    // Update the ASG to use the new faulty LC
    asgService.updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");


    /* Terminate 1 random instance in the ASG to trigger the launch of a faulty LC instance */

    // Get the AutoScalingGroup with given Name
    if (asg == null) {
      asg = asgService.getAutoScalingGroup(asgName);
    }
    if (asg == null) {
      throw new HoneyCombException("Invalid ASG name provided");
    }

    // Get the list of "online" EC2 Instances in the ASG
    // (i.e. the EC2 instances which has state "pending" or "running")
    List<Instance> ec2RunningInstances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances = asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance : asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance.getInstanceId());
      if (ec2Instance.getState().getName().equals("pending")
              || ec2Instance.getState().getName().equals("running")) {
        ec2RunningInstances.add(ec2Instance);
      }
    }

    // If the ASG has any "online" instances
    if (!ec2RunningInstances.isEmpty()) {

      // Randomize an online Instance to be killed
      Collections.shuffle(ec2RunningInstances);
      Instance instanceToInject = ec2RunningInstances.get(0);

      // Log the action
      logger.log("Faulty LaunchConfiguration with wrong AMI updated. "
              + "Terminating instance with id = "
              + instanceToInject.getInstanceId()
              + " for spawning new instance with faulty LC...");

      // Terminate the instance
      ec2Service.terminateInstance(instanceToInject.getInstanceId());

      // Delay for 5 minutes (ASG EC2 Health Check time) for ASG to spawn new faulty instance
      Thread.sleep(1000);

    }

    logger.finish();

  }

  /**
   * ec2Service setter for testing purpose.
   *
   * @param ec2Service An Ec2Service Object.
   */
  public void ec2ServiceSetter(Ec2Service ec2Service) {
    this.ec2Service = ec2Service;
  }


  /**
   * asgService setter for testing purpose.
   *
   * @param asgService An AsgService Object.
   */
  public void asgServiceSetter(AsgService asgService) {
    this.asgService = asgService;
  }

  /**
   * asg Setter for testing purpose.
   *
   * @param asg An AutoScalingGroup Object.
   */
  public void asgSetter(AutoScalingGroup asg) {
    this.asg = asg;
  }
}
