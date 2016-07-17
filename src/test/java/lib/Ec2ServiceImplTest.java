package lib;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.model.Instance;
import mockAws.*;
import mockAws.DescribeImagesResult;
import mockAws.DescribeInstancesResult;
import mockAws.DescribeKeyPairsResult;
import mockAws.DescribeSecurityGroupsResult;
import mockAws.RunInstancesResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class Ec2ServiceImplTest {
  public Ec2ServiceImpl obj;

  @Before
  public void setUp() throws Exception {
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring");

  }

  @Test
  public void testConstructors() throws Exception {
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring");
    assertEquals(obj.getFaultInstanceId(), "testinstanceidwithrandomstring");
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring", null);
    assertNull(obj.getClient());
  }

  @Test
  public void describeAmi() throws Exception {
    Image result;
    result = obj.describeAmi(null);
    assertNull(result);

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    DescribeImagesResult res = new DescribeImagesResult();

    // Valid parameter case
    when(ec2Obj.amazonEC2.describeImages()).thenReturn(null);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr");
    obj.AmazonEC2Setter(ec2Obj.amazonEC2);
    try {
      obj.describeAmi("testingthisrandom");
    } catch (Exception ex) {
      assertNull(ex.getMessage());
    }

    // A full happy path
    com.amazonaws.services.ec2.model.DescribeImagesResult describeImagesRes =
        mockAws.DescribeImagesResult.getDescribeImagesResult();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertEquals(result, describeImagesRes.getImages().get(0));

    // Path when AMI is not available in the region
    describeImagesRes = mockAws.DescribeImagesResult.
        getDescribeImagesResultWithEmptyList();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertNull(result);

    // Part when there is AmazonServiceException
    describeImagesRes = mockAws.DescribeImagesResult.
        getDescribeImagesResultWithException();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertNull(result);

  }

  @Test
  public void describeEC2Instance() throws Exception {
    // blank or null input
    Instance result;
    result = obj.describeEC2Instance(null);
    assertNull(result);
    result = obj.describeEC2Instance("");
    assertNull(result);

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    DescribeInstancesResult res = new DescribeInstancesResult();

    // Valid parameter case
    when(ec2Obj.amazonEC2.describeInstances()).thenReturn(null);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    try {
      obj.describeEC2Instance("testingthisrandom");
    } catch (Exception ex) {
      assertNull(ex.getMessage());
    }

    // path with no instances
    com.amazonaws.services.ec2.model.DescribeInstancesResult
        describeIinstancesRes = mockAws.DescribeInstancesResult
        .getDescribeInstancesResult();
    when(ec2Obj.amazonEC2.describeInstances(any())).thenReturn
        (describeIinstancesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeEC2Instance("testingthisrandom");
    assertEquals(null, result);

    // happy
    describeIinstancesRes = mockAws.DescribeInstancesResult
        .getDescribeInstancesResultWithInstances();
    when(ec2Obj.amazonEC2.describeInstances(any())).thenReturn
        (describeIinstancesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeEC2Instance("testingthisrandom");
    assertEquals(result, describeIinstancesRes.getReservations().get(0)
        .getInstances().get(0));

    // no reservations path
    describeIinstancesRes = mockAws.DescribeInstancesResult
        .getDescribeInstancesResultWithoutReservation();
    when(ec2Obj.amazonEC2.describeInstances(any())).thenReturn
        (describeIinstancesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeEC2Instance("testingthisrandom");
    assertEquals(null, result);


    // Part when there is AmazonServiceException
    describeIinstancesRes = mockAws.DescribeInstancesResult.
        getDescribeInstancesResultWithoutException();
    when(ec2Obj.amazonEC2.describeInstances(any())).thenReturn
        (describeIinstancesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeEC2Instance("testingthisrandom");
    assertNull(null);
  }

  @Test
  public void tagEC2Instance() throws Exception {

    try {
      obj.tagEC2Instance("", "key", "value");
    } catch (Exception ex) {
      assertEquals("No Instance ID provided for tagging", ex.getMessage());
    }

    try {
      obj.tagEC2Instance(null, "key", "value");
    } catch (Exception ex) {
      assertEquals("No Instance ID provided for tagging", ex.getMessage());
    }

    try {
      obj.tagEC2Instance("Testingwithrandomkid", "", "value");
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }

    try {
      obj.tagEC2Instance("Testingwithrandomkid", null, "value");
    } catch (Exception ex) {
      assertEquals("Tag Key cannot be null or empty", ex.getMessage());
    }

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    obj.tagEC2Instance("Testingwithrandomkid", "Asd", "value");
    Mockito.verify(ec2Obj.amazonEC2, times(1)).createTags(any());

  }

  @Test
  public void describeKeyPair() throws Exception {
    //blank null scenarios
    KeyPairInfo result;
    result = obj.describeKeyPair(null);
    assertNull(result);
    result = obj.describeKeyPair("");
    assertNull(result);

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    DescribeKeyPairsResult res = new DescribeKeyPairsResult();

    // Valid parameter case
    when(ec2Obj.amazonEC2.describeKeyPairs()).thenReturn(null);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    try {
      obj.describeKeyPair("testingthisrandom");
    } catch (Exception ex) {
      assertNull(ex.getMessage());
    }

    // A full happy path
    com.amazonaws.services.ec2.model.DescribeKeyPairsResult
        describeKeyPairsRes = mockAws.DescribeKeyPairsResult
        .getDescribeKeyPairsResult();
    when(ec2Obj.amazonEC2.describeKeyPairs(any())).thenReturn
        (describeKeyPairsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeKeyPair("testingthisrandom");
    assertEquals(result, describeKeyPairsRes.getKeyPairs().get(0));

    // Empty keypair case
    describeKeyPairsRes = mockAws.DescribeKeyPairsResult
        .getDescribeKeyPairsResultWithEmptyKeyPairs();
    when(ec2Obj.amazonEC2.describeKeyPairs(any())).thenReturn
        (describeKeyPairsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeKeyPair("testingthisrandom");
    assertEquals(null, result);

    // Part when there is AmazonServiceException
    describeKeyPairsRes = mockAws.DescribeKeyPairsResult
        .getDescribeKeyPairsResultWithException();
    when(ec2Obj.amazonEC2.describeKeyPairs(any())).thenReturn
        (describeKeyPairsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeKeyPair("testingthisrandom");
    assertEquals(null, result);

  }

  @Test
  public void describeSecurityGroup() throws Exception {
    //blank null scenarios
    SecurityGroup result;
    result = obj.describeSecurityGroup(null);
    assertNull(result);
    result = obj.describeSecurityGroup("");
    assertNull(result);

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    DescribeSecurityGroupsResult res = new DescribeSecurityGroupsResult();

    // Valid parameter case
    when(ec2Obj.amazonEC2.describeSecurityGroups()).thenReturn(null);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    try {
      obj.describeSecurityGroup("testingthisrandom");
    } catch (Exception ex) {
      assertNull(ex.getMessage());
    }

    // A full happy path
    com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
        describeSecurityGroupsRes = mockAws.DescribeSecurityGroupsResult
        .getDescribeSecurityGroupsResult();
    when(ec2Obj.amazonEC2.describeSecurityGroups(any())).thenReturn
        (describeSecurityGroupsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeSecurityGroup("testingthisrandom");
    assertEquals(result, describeSecurityGroupsRes.getSecurityGroups().get(0));

    // Empty security group case
    describeSecurityGroupsRes = mockAws.DescribeSecurityGroupsResult
        .getDescribeSecurityGroupsResultWithEmptySecurityGroups();
    when(ec2Obj.amazonEC2.describeSecurityGroups(any())).thenReturn
        (describeSecurityGroupsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeSecurityGroup("testingthisrandom");
    assertEquals(null, result);

    // Part when there is AmazonServiceException
    describeSecurityGroupsRes = mockAws.DescribeSecurityGroupsResult
        .getDescribeSecurityGroupsResultWithException();
    when(ec2Obj.amazonEC2.describeSecurityGroups(any())).thenReturn
        (describeSecurityGroupsRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeSecurityGroup("testingthisrandom");
    assertEquals(null, result);

  }

  @Test
  public void revokeSecurityGroupInboundRule() throws Exception {
    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    obj.revokeSecurityGroupInboundRule("Testingwithrandomkid", "Asd", 1234,
        "value");
    Mockito.verify(ec2Obj.amazonEC2, times(1)).revokeSecurityGroupIngress(any
        ());

  }

  @Test
  public void addSecurityGroupInboundRule() throws Exception {
    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    obj.addSecurityGroupInboundRule("Testingwithrandomkid", "Asd", 1234,
        "value");
    Mockito.verify(ec2Obj.amazonEC2, times(1)).authorizeSecurityGroupIngress
        (any());

  }

  @Test
  public void runInstance() throws Exception {
    //blank null scenarios
    String result;
    try {
      obj.runInstance(null);
    } catch (Exception ex) {
      assertEquals("No parameter for EC2 Instance provided", ex.getMessage());
    }

//    AmazonEC2 ec2Obj = new AmazonEC2();
//    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
//    com.amazonaws.services.ec2.model.RunInstancesResult runInstanceResult =
//        mock(com.amazonaws.services.ec2.model.RunInstancesResult.class);
//    RunInstancesResult.runInstancesResult = RunInstancesResult
//        .getrunInstancesResultWithAlternatingInstanceState();
//    when(ec2Obj.amazonEC2.runInstances(any())).thenReturn(RunInstancesResult.runInstancesResult);
//    when(obj.describeEC2Instance(any())).thenReturn(RunInstancesResult
//        .runInstancesResult.getReservation().getInstances().get(0));
//    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
//    result = obj.runInstance(mock(RunInstancesRequest.class));
//    assertEquals("1234",result);

  }
}
