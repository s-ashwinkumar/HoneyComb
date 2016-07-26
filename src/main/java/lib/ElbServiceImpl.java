package lib;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model.*;
import loggi.faultinjection.Loggi;
import org.apache.commons.validator.GenericValidator;

import java.io.IOException;
import java.util.List;


public class ElbServiceImpl implements ElbService {

  static Loggi logger;
  private String faultInstanceId;
  AmazonElasticLoadBalancing client;

  /**
   * A constructor of this class.
   *
   * @param faultInstanceId A String.
   */
  public ElbServiceImpl(String faultInstanceId) throws IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, ElbServiceImpl.class.getName());
    client = AmazonClientFactory.getAmazonElasticLoadBalancingClient();

  }

  public String getFaultInstanceId() {
    return faultInstanceId;
  }

  public AmazonElasticLoadBalancing getClient() {
    return client;
  }

  public ElbServiceImpl(String faultInstanceId, AmazonElasticLoadBalancing
      client) throws IOException {
    this.faultInstanceId = faultInstanceId;
    logger = new Loggi(faultInstanceId, ElbServiceImpl.class.getName());
    this.client = client;

  }

  /**
   * The AmazonElasticLoadBalancing client for calling AWS API.
   */

  @Override
  public LoadBalancerDescription describeLoadBalancer(String elbName)
      throws AmazonServiceException, AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(elbName)) {
      return null;
    }

    // Invoke API to get details of the ELB
    DescribeLoadBalancersRequest req = new DescribeLoadBalancersRequest();
    req.withLoadBalancerNames(elbName);
    List<LoadBalancerDescription> elbs = null;
    try {
      elbs = client.describeLoadBalancers(req).getLoadBalancerDescriptions();
    } catch (AmazonServiceException exception) {
      logger.log("The ELB with name = " + elbName + " cannot be found. Caused" +
          " by: " + exception);
      return null;
    }

    // Return the ELB Description, or NULL if the ELB does not exist in the
    // region
    if (elbs.isEmpty()) {
      return null;
    } else {
      return elbs.get(0);
    }

  }

  @Override
  public List<InstanceState> describeInstanceHealthInLoadBalancer(
      String elbName) throws AmazonServiceException,
      AmazonClientException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(elbName)) {
      return null;
    }

    // Invoke API to describe instance health status in ELB
    DescribeInstanceHealthRequest req = new DescribeInstanceHealthRequest();
    req.withLoadBalancerName(elbName);
    List<InstanceState> instances = null;
    try {
      instances = client.describeInstanceHealth(req).getInstanceStates();
    } catch (AmazonServiceException exception) {
      logger.log("The ELB with name = " + elbName + " cannot be found. Caused" +
          " by: " + exception);
      return null;
    }

    // Return the list of Instance Health
    return instances;

  }

  @Override
  public void updateElbHealthCheckTarget(String elbName, String target)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameter
    if (GenericValidator.isBlankOrNull(elbName) || GenericValidator
        .isBlankOrNull(target)) {
      throw new IllegalArgumentException("No ELB Name and/or Health Check " +
          "target provided");
    }

    // Grab the ELB to get current Health Check settings
    LoadBalancerDescription elb = describeLoadBalancer(elbName);
    if (elb == null) {
      throw new IllegalArgumentException("No ELB with given Name can be found");
    }
    HealthCheck hc = elb.getHealthCheck();

    // Modify the current ELB Health Check with new URL Target
    hc.withTarget(target);

    // Invoke API to update new Health Check setting to the ELB
    ConfigureHealthCheckRequest req = new ConfigureHealthCheckRequest();
    req.withLoadBalancerName(elbName).withHealthCheck(hc);
    client.configureHealthCheck(req);

  }

  @Override
  public void tagElb(String elbName, String tagKey, String tagValue)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (GenericValidator.isBlankOrNull(elbName)) {
      throw new IllegalArgumentException("No ELB Name provided for tagging");
    }
    if (GenericValidator.isBlankOrNull(tagKey)) {
      throw new IllegalArgumentException("Tag Key cannot be null or empty");
    }

    // Invoke API to add new Tag to ELB
    Tag tag = new Tag();
    tag.withKey(tagKey).withValue(tagValue);
    AddTagsRequest req = new AddTagsRequest();
    req.withLoadBalancerNames(elbName).withTags(tag);
    client.addTags(req);

  }

  @Override
  public List<Tag> describeElbTags(String elbName)
      throws AmazonServiceException, AmazonClientException {

    DescribeTagsRequest req = new DescribeTagsRequest();
    req.withLoadBalancerNames(elbName);
    List<TagDescription> tagDescriptions = null;
    try {
      tagDescriptions = client.describeTags(req).getTagDescriptions();
    } catch (AmazonServiceException exception) {
      logger.log("ELB with name = " + elbName + " cannot be found. Caused by:" +
          " " + exception);
      return null;
    }

    if (tagDescriptions.isEmpty()) {
      return null;
    } else {
      return tagDescriptions.get(0).getTags();
    }

  }

  @Override
  public void removeTagFromElb(String elbName, String tagKey)
      throws AmazonServiceException, AmazonClientException,
      IllegalArgumentException {

    // Check for valid parameters
    if (GenericValidator.isBlankOrNull(elbName)) {
      throw new IllegalArgumentException("No ELB Name provided for removing " +
          "tag");
    }
    if (GenericValidator.isBlankOrNull(tagKey)) {
      throw new IllegalArgumentException("Tag Key cannot be null or empty");
    }

    // Invoke API to remove tag
    TagKeyOnly tagToRemove = new TagKeyOnly();
    tagToRemove.withKey(tagKey);
    RemoveTagsRequest req = new RemoveTagsRequest();
    req.withLoadBalancerNames(elbName).withTags(tagToRemove);
    client.removeTags(req);

  }

  public void AmazonElasticLoadBalancingSetter(AmazonElasticLoadBalancing
                                                   client) {
    this.client = client;
  }

}
