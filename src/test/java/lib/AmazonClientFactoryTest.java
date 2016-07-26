package lib;

import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Created by ashwin on 7/22/16.
 */
public class AmazonClientFactoryTest {
  public AmazonClientFactory obj;
  LogChanger log = new LogChanger();

  @Before
  public void setup() throws IOException{
    obj = new AmazonClientFactory();
    log.setupLogForTest();
  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }

  @Test
  public void testGetters() {
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
