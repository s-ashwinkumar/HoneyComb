package lib;

import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
/**
 * Created by ashwin on 7/22/16.
 */
public class AmazonClientFactoryTest {
  public AmazonClientFactory obj;

  @Before
  public void setup(){
    obj = new AmazonClientFactory();
  }

  @Test
  public void testGetters(){
    AmazonAutoScaling asgclient = AmazonClientFactory
        .getAmazonAutoScalingClient();
    assertNotNull(asgclient);
    asgclient = obj.getAmazonAutoScalingClientTest();
    assertNotNull(asgclient);

    AmazonEC2 ec2 = AmazonClientFactory.getAmazonEC2Client();
    assertNotNull(ec2);
    ec2 = obj.getAmazonEC2ClientTest();
    assertNotNull(ec2);

    AmazonElasticLoadBalancing elb = AmazonClientFactory
        .getAmazonElasticLoadBalancingClient();
    assertNotNull(elb);
    elb = obj.getAmazonElasticLoadBalancingClient();
    assertNotNull(asgclient);
    elb = obj.getAmazonElasticLoadBalancingClientTest();
    assertNotNull(asgclient);

  }
}
