package lib;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.model.*;
import logmodifier.LogChanger;
import mockAws.AmazonElasticLoadBalancing;
import mockAws.DescribeInstanceHealthResult;
import mockAws.DescribeLoadBalancersResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by wilsoncao on 7/13/16.
 */
public class ElbServiceImplTest {
  public ElbServiceImpl obj;
  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    obj = new ElbServiceImpl("testinstanceidwithrandomstring");
    log.setupLogForTest();

  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }
  
  @Test
  public void testConstructors() throws Exception {
    obj = new ElbServiceImpl("sampleinstanceid");
    assertEquals("sampleinstanceid", obj.getFaultInstanceId());
    AmazonElasticLoadBalancing amazonELB = new AmazonElasticLoadBalancing();
    amazonELB.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    obj = new ElbServiceImpl("sampleinstanceid2", amazonELB.loadBalancer);
    assertEquals("sampleinstanceid2", obj.getFaultInstanceId());
    assertEquals(amazonELB.loadBalancer, obj.getClient());
  }

  @Test
  public void describeLoadBalancer() throws Exception {
    LoadBalancerDescription result;
    result = obj.describeLoadBalancer("");
    assertNull(result);
    result = obj.describeLoadBalancer(null);
    assertNull(result);

    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    DescribeLoadBalancersResult lbResult = new DescribeLoadBalancersResult();
    lbResult.describeLoadBalancersResult = DescribeLoadBalancersResult
        .getDescribeLoadBalancersResultWithException();
    when(client.loadBalancer.describeLoadBalancers(any())).thenReturn
        (lbResult.describeLoadBalancersResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeLoadBalancer("testnameforthis");
    assertNull(result);

    lbResult.describeLoadBalancersResult = DescribeLoadBalancersResult
        .getDescribeLoadBalancersResultWithEmptyList();
    when(client.loadBalancer.describeLoadBalancers(any())).thenReturn
        (lbResult.describeLoadBalancersResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeLoadBalancer("testnameforthis");
    assertNull(result);

    lbResult.describeLoadBalancersResult = DescribeLoadBalancersResult
        .getDescribeLoadBalancersResult();
    when(client.loadBalancer.describeLoadBalancers(any())).thenReturn
        (lbResult.describeLoadBalancersResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeLoadBalancer("testnameforthis");
    assertEquals(lbResult.describeLoadBalancersResult
        .getLoadBalancerDescriptions().get(0), result);

  }

  @Test
  public void describeInstanceHealthInLoadBalancer() throws Exception {
    List<InstanceState> result;
    result = obj.describeInstanceHealthInLoadBalancer("");
    assertNull(result);
    result = obj.describeInstanceHealthInLoadBalancer(null);
    assertNull(result);

    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    DescribeInstanceHealthResult lbhResult = new DescribeInstanceHealthResult();
    lbhResult.describeInstanceHealthResult = DescribeInstanceHealthResult
        .getDescribeInstanceHealthResultWithException();
    when(client.loadBalancer.describeInstanceHealth(any())).thenReturn
        (lbhResult.describeInstanceHealthResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeInstanceHealthInLoadBalancer("testnameforthis");
    assertNull(result);

    lbhResult.describeInstanceHealthResult = DescribeInstanceHealthResult
        .getDescribeInstanceHealthResultWithEmptyList();
    when(client.loadBalancer.describeInstanceHealth(any())).thenReturn
        (lbhResult.describeInstanceHealthResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeInstanceHealthInLoadBalancer("testnameforthis");
    assertEquals(0, result.size());

    lbhResult.describeInstanceHealthResult = DescribeInstanceHealthResult
        .getDescribeInstanceHealthResult();
    when(client.loadBalancer.describeInstanceHealth(any())).thenReturn
        (lbhResult.describeInstanceHealthResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    result = obj.describeInstanceHealthInLoadBalancer("testnameforthis");
    assertEquals(lbhResult.describeInstanceHealthResult.getInstanceStates(),
        result);

  }

  @Test
  public void updateElbHealthCheckTarget() throws Exception {
    try {
      obj.updateElbHealthCheckTarget(null, null);
    } catch (Exception ex) {
      assertEquals("No ELB Name and/or Health Check target provided", ex
          .getMessage());
    }
    try {
      obj.updateElbHealthCheckTarget("", null);
    } catch (Exception ex) {
      assertEquals("No ELB Name and/or Health Check target provided", ex
          .getMessage());
    }
    try {
      obj.updateElbHealthCheckTarget("Testingthiswithname", null);
    } catch (Exception ex) {
      assertEquals("No ELB Name and/or Health Check target provided", ex
          .getMessage());
    }
    try {
      obj.updateElbHealthCheckTarget("Testingthiswithname", "");
    } catch (Exception ex) {
      assertEquals("No ELB Name and/or Health Check target provided", ex
          .getMessage());
    }

    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    DescribeLoadBalancersResult lbResult = new DescribeLoadBalancersResult();
    lbResult.describeLoadBalancersResult = DescribeLoadBalancersResult
        .getDescribeLoadBalancersResultWithEmptyList();
    when(client.loadBalancer.describeLoadBalancers(any())).thenReturn
        (lbResult.describeLoadBalancersResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    try {
      obj.updateElbHealthCheckTarget("testnameforthis", "testnameforthis");
    } catch (Exception ex) {
      assertEquals("No ELB with given Name can be found", ex.getMessage());
    }

    lbResult.describeLoadBalancersResult = DescribeLoadBalancersResult
        .getDescribeLoadBalancersResult();
    when(client.loadBalancer.describeLoadBalancers(any())).thenReturn
        (lbResult.describeLoadBalancersResult);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    obj.updateElbHealthCheckTarget("testnameforthis", "testnameforthis");
    verify(client.loadBalancer, times(1)).configureHealthCheck(any());

  }

  @Test
  public void tagElb() throws Exception {
    try {
      obj.tagElb("", "whatever123", "whatever234");
    } catch (Exception ex) {
      assertEquals("No ELB Name provided for tagging", ex.getMessage());
    }
    try {
      obj.tagElb(null, "whatever123", "whatever234");
    } catch (Exception ex) {
      assertEquals("No ELB Name provided for tagging", ex.getMessage());
    }
    try {
      obj.tagElb("whatever123", "", "whatever234");
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }
    try {
      obj.tagElb("whatever123", null, "whatever234");
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }
    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    obj.tagElb("whatever123", "whatever234", "whatever345");
    verify(client.loadBalancer, times(1)).addTags(any());

  }

  @Test
  public void describeElbTags() throws Exception {

    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    DescribeTagsResult tagRes = mock(DescribeTagsResult.class);
    when(tagRes.getTagDescriptions()).thenThrow(AmazonServiceException.class);
    when(client.loadBalancer.describeTags(any())).thenReturn(tagRes);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    assertNull(obj.describeElbTags("testing1234"));

    tagRes = mock(DescribeTagsResult.class);
    List<TagDescription> tagsdesc = new ArrayList<>();
    when(tagRes.getTagDescriptions()).thenReturn(tagsdesc);
    when(client.loadBalancer.describeTags(any())).thenReturn(tagRes);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    assertNull(obj.describeElbTags("testing1234"));

    TagDescription tagDesc = mock(TagDescription.class);
    List<Tag> tags = new ArrayList<>();
    Tag t = mock(Tag.class);
    tags.add(t);
    when(tagDesc.getTags()).thenReturn(tags);
    tagsdesc.add(tagDesc);
    when(tagRes.getTagDescriptions()).thenReturn(tagsdesc);
    when(client.loadBalancer.describeTags(any())).thenReturn(tagRes);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    assertEquals(tags, obj.describeElbTags("testin1234"));
  }

  @Test
  public void removeTagFromElb() throws Exception {
    try {
      obj.removeTagFromElb("", "whatever234");
    } catch (Exception ex) {
      assertEquals("No ELB Name provided for removing tag", ex.getMessage());
    }
    try {
      obj.removeTagFromElb(null, "whatever234");
    } catch (Exception ex) {
      assertEquals("No ELB Name provided for removing tag", ex.getMessage());
    }
    try {
      obj.removeTagFromElb("whatever123", "");
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }
    try {
      obj.removeTagFromElb("whatever123", null);
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }

    AmazonElasticLoadBalancing client = new AmazonElasticLoadBalancing();
    client.loadBalancer = mock(com.amazonaws.services.elasticloadbalancing
        .AmazonElasticLoadBalancing.class);
    obj.AmazonElasticLoadBalancingSetter(client.loadBalancer);
    obj.removeTagFromElb("whatever1234", "whatever2345");
    verify(client.loadBalancer, times(1)).removeTags(any());

  }
}
