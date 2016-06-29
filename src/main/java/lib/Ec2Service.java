package lib;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.SecurityGroup;


/**
 * EC2Service interface.
 * Provide wrapper services for various operations on AWS EC2 API.
 *
 */
public interface Ec2Service {

  /**
   * Describe the details of an AMI image in AWS from the provided AMI ID.
   *
   * @param amiId the AMI ID of the Image we want to describe
   * @return the Image object representing the AMI
   *     , or NULL if the AMI with the provided ID cannot be found in the current AWS region
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public Image describeAmi(String amiId) throws AmazonServiceException, AmazonClientException;

  /**
   * Describe the details of an EC2 Instance from an Instance ID.
   *
   * @param instanceId the instance ID we want to describe
   * @return an EC2 Instance object representing the Instance,
   *     or NULL if the Instance with the given ID cannot be found
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public Instance describeEC2Instance(String instanceId)
      throws AmazonServiceException, AmazonClientException;

  /**
   * Add a Tag to the EC2 instance.
   *
   * @param instanceId the instance ID we want to tag
   * @param tagKey     the key of the new Tag
   * @param tagValue   the value of the new Tag
   * @throws AmazonServiceException   if error occurred while calling AWS API
   * @throws AmazonClientException    if error occurred while calling AWS API
   * @throws IllegalArgumentException if instanceId or tagKey not provided
   */
  public void tagEC2Instance(String instanceId, String tagKey, String tagValue)
      throws AmazonServiceException, AmazonClientException, IllegalArgumentException;

  /**
   * Describe the details of an EC2 Key Pair with a given Name.
   *
   * @param keyPairName the name of the Key Pair
   * @return a KeyPairInfo object containing the details of the KeyPair,
   *     or NULL if the KeyPair with given Name cannot be found
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public KeyPairInfo describeKeyPair(String keyPairName)
      throws AmazonServiceException, AmazonClientException;

  /**
   * Describe the details of a Security Group with a given Name.
   *
   * @param securityGroupName the name of the Security Group
   * @return a SecurityGroup object representing the Security Group,
   *     or NULL if the SG with the given Name cannot be found
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public SecurityGroup describeSecurityGroup(String securityGroupName)
      throws AmazonServiceException, AmazonClientException;

  /**
   * Delete a particular Security Group Inbound rule.
   *
   * @param securityGroupName the name of the Security Group
   * @param protocol          the protocol of the rule we want to remove (HTTP, HTTPS, SSH)
   * @param port              the port of the rule
   * @param address           the inbound address of the rule
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public void revokeSecurityGroupInboundRule(String securityGroupName,
                                             String protocol, int port, String address)
      throws AmazonServiceException, AmazonClientException;

  /**
   * Add an Inbound rule to a Security Group.
   *
   * @param securityGroupName the name of the Security Group
   * @param protocol          the protocol of the rule we want to add (HTTP, HTTPS, SSH)
   * @param port              the port of the rule
   * @param address           the inbound address of the rule
   * @throws AmazonServiceException if error occurred while calling AWS API
   * @throws AmazonClientException  if error occurred while calling AWS API
   */
  public void addSecurityGroupInboundRule(String securityGroupName,
                                          String protocol, int port, String address)
      throws AmazonServiceException, AmazonClientException;

	
  /* The following methods will mostly be used by test cases */

  /**
   * Create an EC2 Instance, using specified parameters.
   *
   * @param params a set of parameters for creating the EC2 Instance
   * @return the newly created EC2 Instance ID
   * @throws AmazonServiceException   if error occurred while calling AWS API
   * @throws AmazonClientException    if error occurred while calling AWS API
   * @throws IllegalArgumentException if null parameters provided
   */
  public String runInstance(RunInstancesRequest params)
      throws AmazonServiceException, AmazonClientException, IllegalArgumentException;

  /**
   * Terminate an EC2 Instance with the specified Instance ID.
   *
   * @param instanceId the Instance ID of the EC2 instance we want to terminate
   * @throws AmazonServiceException   if error occurred while calling AWS API
   * @throws AmazonClientException    if error occurred while calling AWS API
   * @throws IllegalArgumentException if invalid Instance ID provided
   */
  public void terminateInstance(String instanceId)
      throws AmazonServiceException, AmazonClientException, IllegalArgumentException;

}
