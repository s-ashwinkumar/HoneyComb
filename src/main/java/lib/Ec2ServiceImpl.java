package lib;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import loggi.faultinjection.Loggi;
import org.apache.commons.validator.GenericValidator;

import java.io.IOException;
import java.util.List;

/**
 * Implementation class for EC2-related services.
 */
public class Ec2ServiceImpl implements Ec2Service {


  private String faultInstanceId;
  static Loggi logger;
  AmazonEC2 client = AmazonClientFactory.getAmazonEC2Client();

  /**
   * A constructor of this class.
   *
   * @param faultInstanceId A String.
   */
  public Ec2ServiceImpl(String faultInstanceId) throws IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, Ec2ServiceImpl.class.getName());
  }

  public Ec2ServiceImpl(String faultInstanceId, AmazonEC2 client) throws
      IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, Ec2ServiceImpl.class.getName());
    this.client = client;
  }

  public AmazonEC2 getClient() {
    return client;
  }

  public String getFaultInstanceId() {
    return faultInstanceId;
  }

  @Override
  public Image describeAmi(String amiId) throws AmazonServiceException,
      AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(amiId)) {
      return null;
    }

    // Make API call to describe the AMI
    DescribeImagesRequest req = new DescribeImagesRequest();
    req.withImageIds(amiId);
    List<Image> images = null;
    try {
      images = client.describeImages(req).getImages();
    } catch (AmazonServiceException exception) {
      logger.log("The AMI with id = " + amiId + " cannot be found. Caused by:" +
          " " + exception);
      return null;
    }

    // Return the AMI Image object, or NULL if the AMI is not available in
    // the region
    if (images.isEmpty()) {
      return null;
    } else {
      return images.get(0);
    }

  }

  @Override
  public Instance describeEC2Instance(String instanceId)
      throws AmazonServiceException, AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(instanceId)) {
      return null;
    }

    // Make API call to describe the EC2 Instance
    DescribeInstancesRequest req = new DescribeInstancesRequest();
    req.withInstanceIds(instanceId);
    List<Reservation> reservations = null;
    try {
      reservations = client.describeInstances(req).getReservations();
    } catch (AmazonServiceException exception) {
      logger.error("The EC2 Instance with id = " + instanceId
          + " cannot be found. Caused by: " + exception.getErrorMessage());
      return null;
    }

    if (reservations.isEmpty()) {
      return null;
    } else {
      List<Instance> instances = reservations.get(0).getInstances();
      if (instances.isEmpty()) {
        return null;
      } else {
        return instances.get(0);
      }
    }
  }

  @Override
  public void tagEC2Instance(String instanceId, String tagKey, String tagValue)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (GenericValidator.isBlankOrNull(instanceId)) {
      throw new IllegalArgumentException("No Instance ID provided for tagging");
    }
    if (GenericValidator.isBlankOrNull(tagKey)) {
      throw new IllegalArgumentException("Tag Key cannot be null or empty");
    }

    Tag tag = new Tag(tagKey, tagValue);
    CreateTagsRequest req = new CreateTagsRequest();
    req.withResources(instanceId).withTags(tag);
    client.createTags(req);
  }

  @Override
  public KeyPairInfo describeKeyPair(String keyPairName)
      throws AmazonServiceException, AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(keyPairName)) {
      return null;
    }

    // Invoke API to get KeyPair details
    DescribeKeyPairsRequest req = new DescribeKeyPairsRequest();
    req.withKeyNames(keyPairName);
    List<KeyPairInfo> keyPairs = null;
    try {
      keyPairs = client.describeKeyPairs(req).getKeyPairs();
    } catch (AmazonServiceException exception) {
      logger.error("The Key Pair with name = " + keyPairName
          + " cannot be found. Caused by: " + exception);
      return null;
    }

    // Return the Key Pair if found
    if (keyPairs.isEmpty()) {
      return null;
    } else {
      return keyPairs.get(0);
    }

  }

  @Override
  public SecurityGroup describeSecurityGroup(String securityGroupName)
      throws AmazonServiceException, AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(securityGroupName)) {
      return null;
    }

    // Invoke API to get SecurityGroup details
    DescribeSecurityGroupsRequest req = new DescribeSecurityGroupsRequest();
    req.withGroupNames(securityGroupName);
    List<SecurityGroup> sgs = null;
    try {
      sgs = client.describeSecurityGroups(req).getSecurityGroups();
    } catch (AmazonServiceException exception) {
      logger.error("The Security Group with name = "
          + securityGroupName + " cannot be found. Caused by: " + exception);
      return null;
    }

    // Return the SecurityGroup if found
    if (sgs.isEmpty()) {
      return null;
    } else {
      return sgs.get(0);
    }

  }

  @Override
  public void revokeSecurityGroupInboundRule(String securityGroupName,
                                             String protocol, int port,
                                             String address)
      throws AmazonServiceException, AmazonClientException {

    // Create request
    RevokeSecurityGroupIngressRequest req = new
        RevokeSecurityGroupIngressRequest();
    req.withGroupName(securityGroupName)
        .withIpProtocol(protocol)
        .withFromPort(port)
        .withToPort(port)
        .withCidrIp(address);
    client.revokeSecurityGroupIngress(req);

  }

  @Override
  public void addSecurityGroupInboundRule(String securityGroupName,
                                          String protocol, int port, String
                                              address)
      throws AmazonServiceException, AmazonClientException {

    AuthorizeSecurityGroupIngressRequest req = new
        AuthorizeSecurityGroupIngressRequest();
    req.withGroupName(securityGroupName)
        .withIpProtocol(protocol)
        .withFromPort(port)
        .withToPort(port)
        .withCidrIp(address);
    client.authorizeSecurityGroupIngress(req);

  }

  @Override
  public String runInstance(RunInstancesRequest params)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (params == null) {
      throw new IllegalArgumentException("No parameter for EC2 Instance " +
          "provided");
    }

    // Invoke API to create and run new EC2 Instance
    Instance instance = client.runInstances(params).getReservation()
        .getInstances().get(0);
    String instanceId = instance.getInstanceId();

    // Periodically check until the instance becomes RUNNING
    while (!instance.getState().getName().equals("running")) {
      // Interval for checking is 30 seconds
      try {
        Thread.sleep(30000);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
      instance = describeEC2Instance(instanceId);
    }

    return instanceId;

  }

  @Override
  public void terminateInstance(String instanceId)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(instanceId)) {
      throw new IllegalArgumentException("No Instance ID provided for " +
          "termination");
    }

    // Invoke API to terminate the specified EC2 Instance ID
    TerminateInstancesRequest req = new TerminateInstancesRequest();
    req.withInstanceIds(instanceId);
    try {
      client.terminateInstances(req);
    } catch (AmazonServiceException exception) {
      throw new IllegalArgumentException("EC2 Instance with id = "
          + instanceId + " not found. Caused by: " + exception);
    }
  }

  public void AmazonEC2Setter(AmazonEC2 client) {
    this.client = client;
  }

}
