package lib;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.model.*;
import com.amazonaws.services.ec2.model.Instance;
import loggi.faultinjection.Loggi;
import org.apache.commons.validator.GenericValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation class for ASG-related services.
 */
public class AsgServiceImpl implements AsgService {


  private String faultInstanceId;
  private static Loggi logger;
  private AmazonAutoScaling client;
  private Ec2Service ec2Service;

  /**
   * A constructor of this class.
   *
   * @param faultInstanceId A String.
   */
  public AsgServiceImpl(String faultInstanceId) throws IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, AsgServiceImpl.class.getName());
    client = AmazonClientFactory.getAmazonAutoScalingClient();
    ec2Service = new Ec2ServiceImpl(faultInstanceId);
  }

  public AsgServiceImpl(String faultInstanceId, AmazonAutoScaling client,
                        Ec2Service ec2Service) throws IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, AsgServiceImpl.class.getName());
    this.client = client;
    this.ec2Service = ec2Service;
  }

  public String getFaultInstanceId() {
    return faultInstanceId;
  }

  public AmazonAutoScaling getClient() {
    return client;
  }

  public Ec2Service getEc2Service() {
    return ec2Service;
  }

  @Override
  public AutoScalingGroup getAutoScalingGroup(String asgName)
      throws AmazonServiceException, AmazonClientException {

    // Check for invalid parameter
    if (GenericValidator.isBlankOrNull(asgName)) {
      return null;
    }

    // Make API call to describe the AutoScalingGroup
    DescribeAutoScalingGroupsRequest req = new
        DescribeAutoScalingGroupsRequest();
    req.withAutoScalingGroupNames(asgName);
    List<AutoScalingGroup> asgs = client.describeAutoScalingGroups(req)
        .getAutoScalingGroups();

    // Return the AutoScalingGroup object, or NULL if no ASG with the
    // specified name are found
    if (asgs.isEmpty()) {
      return null;
    } else {
      return asgs.get(0);
    }

  }

  @Override
  public LaunchConfiguration getLaunchConfigurationForAutoScalingGroup(
      String asgName) throws AmazonServiceException,
      AmazonClientException {

    // Check for invalid parameter
    if (GenericValidator.isBlankOrNull(asgName)) {
      return null;
    }

    // Grab the AutoScalingGroup object with the specified Name
    AutoScalingGroup asg = getAutoScalingGroup(asgName);
    // Return NULL if no ASG with the name is found
    if (asg == null) {
      return null;
    }

    // Make API call to describe the LaunchConfiguration associated with the
    // above ASG
    DescribeLaunchConfigurationsRequest req = new
        DescribeLaunchConfigurationsRequest();
    req.withLaunchConfigurationNames(asg.getLaunchConfigurationName());
    List<LaunchConfiguration> lcs = client.describeLaunchConfigurations(req)
        .getLaunchConfigurations();

    // Return the LaunchConfiguration object, or NULL if no LC with the name
    // is found
    if (lcs.isEmpty()) {
      return null;
    } else {
      return lcs.get(0);
    }

  }

  @Override
  public LaunchConfiguration getLaunchConfigurationByName(String lcName)
      throws AmazonServiceException, AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(lcName)) {
      return null;
    }

    // Invoke API to describe the LC with the specified Name
    DescribeLaunchConfigurationsRequest req = new
        DescribeLaunchConfigurationsRequest();
    req.withLaunchConfigurationNames(lcName);
    List<LaunchConfiguration> lcs = client.describeLaunchConfigurations(req)
        .getLaunchConfigurations();

    // Return the LaunchConfiguration object, or NULL if no LC with the given
    // Name is found
    if (lcs.isEmpty()) {
      return null;
    } else {
      return lcs.get(0);
    }

  }

  @Override
  public List<Activity> getScalingActivitiesForAutoScalingGroup(String asgName)
      throws AmazonServiceException, AmazonClientException {

    // Check for invalid parameter
    if (GenericValidator.isBlankOrNull(asgName)) {
      return null;
    }

    // Check if the ASG with the provided name can be found; if not, return NULL
    if (getAutoScalingGroup(asgName) == null) {
      return null;
    }

    // Make API call to describe the Scaling Activities of this ASG
    DescribeScalingActivitiesRequest req = new
        DescribeScalingActivitiesRequest();
    req.withAutoScalingGroupName(asgName);

    // Return the list of Scaling Activities for this ASG
    return client.describeScalingActivities(req).getActivities();

  }

  @Override
  public Instance getNewestEc2InstanceInAsg(String asgName)
      throws AmazonServiceException, AmazonClientException {

    // Check for invalid parameter
    if (GenericValidator.isBlankOrNull(asgName)) {
      return null;
    }

    // Grab the ASG with the specified name
    AutoScalingGroup asg = getAutoScalingGroup(asgName);
    // If ASG not found, return null
    if (asg == null) {
      return null;
    }

    // Grab the list of EC2 instances in the ASG
    List<Instance> ec2Instances = new ArrayList<Instance>();
    List<com.amazonaws.services.autoscaling.model.Instance> asgInstances =
        asg.getInstances();
    for (com.amazonaws.services.autoscaling.model.Instance asgInstance :
        asgInstances) {
      Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance
          .getInstanceId());
      ec2Instances.add(ec2Instance);
    }

    // If there is no instances in the ASG, return null
    if (ec2Instances.isEmpty()) {
      return null;
    }

    // Sort the list based on Instances Launch Time in descending order to
    // grab the "newest" launched instance
    Collections.sort(ec2Instances, new Comparator<Instance>() {
      public int compare(Instance o1, Instance o2) {
        if (o1.getLaunchTime().after(o2.getLaunchTime())) {
          return -1;
        } else if (o1.getLaunchTime().before(o2.getLaunchTime())) {
          return 1;
        } else {
          return 0;
        }
      }
    });

    // Return the EC2 Instance at the Head of the List, as the "newest"
    // instance launched
    return ec2Instances.get(0);

  }

  @Override
  public void updateLaunchConfigurationInAutoScalingGroup(String asgName,
                                                          String lcName)
      throws AmazonServiceException, AmazonClientException {

    UpdateAutoScalingGroupRequest req = new UpdateAutoScalingGroupRequest();
    req.withAutoScalingGroupName(asgName).withLaunchConfigurationName(lcName);
    client.updateAutoScalingGroup(req);

  }

  @Override
  public void updateCapacityOfAsg(String asgName, int capacity)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid ASG name
    if (GenericValidator.isBlankOrNull(asgName)) {
      throw new IllegalArgumentException("No Auto Scaling Group name provided");
    }

    // Check if the ASG with the provided name can be found on AWS
    if (getAutoScalingGroup(asgName) == null) {
      throw new IllegalArgumentException("Auto Scaling Group with provided " +
          "Name does not exist");
    }

    // Check for valid Capacity value
    if (capacity < 0) {
      throw new IllegalArgumentException("ASG Capacity need to be " +
          "non-negative!");
    }

    // Invoke API to update new Capacity for ASG
    UpdateAutoScalingGroupRequest update = new UpdateAutoScalingGroupRequest();
    update.withAutoScalingGroupName(asgName)
        .withMinSize(capacity)
        .withDesiredCapacity(capacity)
        .withMaxSize(capacity);
    client.updateAutoScalingGroup(update);

  }

  @Override
  public String createLaunchConfiguration(
      CreateLaunchConfigurationRequest params)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (params == null) {
      throw new IllegalArgumentException("No parameter for LC provided");
    }

    // Invoke API to create new LC
    client.createLaunchConfiguration(params);

    return params.getLaunchConfigurationName();

  }

  @Override
  public String createAutoScalingGroup(CreateAutoScalingGroupRequest params)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (params == null) {
      throw new IllegalArgumentException("No parameter for ASG provided");
    }

    // Invoke API to create new ASG
    client.createAutoScalingGroup(params);

    try {
      Thread.sleep(20000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    // Check whether there are ongoing Scaling Activities
    // (i.e. wait until all Instances are launched)
    boolean isScalingActivitiesInProgress;
    do {
      isScalingActivitiesInProgress = false;

      List<Activity> scalingActivities =
          getScalingActivitiesForAutoScalingGroup(params
              .getAutoScalingGroupName());

      // This only happens when an ASG with the provided name cannot be found
      // on AWS
      if (scalingActivities == null) {
        throw new IllegalArgumentException("Auto Scaling Group with provided " +
            "Name does not exist");
      }

      // If there are "in progress" Scaling Activities, need to wait until
      // all activities completed
      // (i.e. activity status is other than "Successful", "Failed" or
      // "Cancelled")
      if (!scalingActivities.isEmpty()) {
        for (Activity a : scalingActivities) {
          if (!a.getStatusCode().equals("Successful")
              && !a.getStatusCode().equals("Failed")
              && !a.getStatusCode().equals("Cancelled")) {

            logger.log("Waiting for all Instances in ASG to complete " +
                "launching...");

            isScalingActivitiesInProgress = true;

            // Wait for 20 seconds until the next check
            try {
              Thread.sleep(20000);
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
            }

            break;
          }
        }
      }
    } while (isScalingActivitiesInProgress);

    // Sleep for several minutes for ELB to become InService for all instances
    //logger.
    // ("Sleep for several minutes for ELB
    // to complete Health Check on all instances...");
    //Time.sleepUninterruptible(120000);

    return params.getAutoScalingGroupName();
  }

  @Override
  public void deleteLaunchConfiguration(String lcName)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(lcName)) {
      throw new IllegalArgumentException("No Launch Configuration name " +
          "provided");
    }

    // Check whether a LC with the specified name actually exists on AWS
    if (client.describeLaunchConfigurations(
        new DescribeLaunchConfigurationsRequest()
            .withLaunchConfigurationNames(lcName))
        .getLaunchConfigurations().isEmpty()) {
      throw new IllegalArgumentException("Launch Configuration with provided " +
          "Name does not exist");
    }

    // Invoke API to delete LC
    DeleteLaunchConfigurationRequest req = new
        DeleteLaunchConfigurationRequest();
    req.withLaunchConfigurationName(lcName);
    client.deleteLaunchConfiguration(req);

  }

  @Override
  public void deleteAutoScalingGroup(String asgName)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(asgName)) {
      throw new IllegalArgumentException("No Auto Scaling Group name provided");
    }

    // Check if the ASG with the provided name can be found on AWS
    if (getAutoScalingGroup(asgName) == null) {
      throw new IllegalArgumentException("Auto Scaling Group with provided " +
          "Name does not exist");
    }

    // Reduce the MinSize, DesiredCapacity and MaxSize
    // of ASG to 0 (effectively kill all existing instances)
    updateCapacityOfAsg(asgName, 0);

    // Wait for the changes to kick in (i.e. for ASG to start deleting
    // Instances)
    try {
      Thread.sleep(60000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    // Check whether there are ongoing Scaling Activities
    // (since ASG cannot be deleted during this time)
    boolean isScalingActivitiesInProgress;
    do {
      isScalingActivitiesInProgress = false;

      List<Activity> scalingActivities =
          getScalingActivitiesForAutoScalingGroup(asgName);

      // This only happens when an ASG with the provided name cannot be found
      // on AWS
      if (scalingActivities == null) {
        throw new IllegalArgumentException("Auto Scaling Group with provided " +
            "Name does not exist");
      }

      // If there are "in progress" Scaling Activities, need to wait until
      // all activities completed
      // (i.e. activity status is other than "Successful", "Failed" or
      // "Cancelled")
      if (!scalingActivities.isEmpty()) {
        for (Activity a : scalingActivities) {
          if (!a.getStatusCode().equals("Successful")
              && !a.getStatusCode().equals("Failed")
              && !a.getStatusCode().equals("Cancelled")) {

            logger.log("Waiting for all Scaling Activities to finish before " +
                "deleting ASG...");

            isScalingActivitiesInProgress = true;

            // Wait for 20 seconds until the next check
            try {
              Thread.sleep(20000);
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
            }

            break;
          }
        }
      }
    } while (isScalingActivitiesInProgress);

    // Invoke API to delete ASG
    DeleteAutoScalingGroupRequest req = new DeleteAutoScalingGroupRequest();
    req.withAutoScalingGroupName(asgName);
    client.deleteAutoScalingGroup(req);

  }

  public void AmazonAutoScalingSetter(AmazonAutoScaling client) {
    this.client = client;
  }

  public void Ec2ServiceSetter(Ec2Service ec2Service) {
    this.ec2Service = ec2Service;
  }

}
