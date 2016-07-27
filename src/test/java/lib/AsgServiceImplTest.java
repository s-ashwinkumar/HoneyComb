package lib;


import com.amazonaws.services.autoscaling.model.Activity;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model
    .CreateLaunchConfigurationRequest;
import logmodifier.LogChanger;
import mockAws.AmazonAutoScaling;
import mockAws.DescribeAutoScalingGroupsResult;
import mockAws.DescribeLaunchConfigurationsResult;
import mockAws.DescribeScalingActivitiesResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by wilsoncao on 7/13/16.
 */
public class AsgServiceImplTest {
  public AsgServiceImpl obj;
  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    obj = new AsgServiceImpl("testinstanceidwithrandomstring");
    log.setupLogForTest();

  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }

  @Test
  public void testConstructors() throws Exception {
    obj = new AsgServiceImpl("testinstanceidwithrandomstring");
    assertEquals(obj.getFaultInstanceId(), "testinstanceidwithrandomstring");
    obj = new AsgServiceImpl("testinstanceidwithrandomstring", null, null);
    assertNull(obj.getClient());
    assertNull(obj.getEc2Service());
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    mockLib.Ec2Service service = new mockLib.Ec2Service();
    service.ec2Service = mock(Ec2Service.class);
    obj.Ec2ServiceSetter(service.ec2Service);
    assertEquals(obj.getEc2Service(), service.ec2Service);
    assertEquals(obj.getClient(), amazonasg.client);
  }

  @Test
  public void getAutoScalingGroup() throws Exception {
    com.amazonaws.services.autoscaling.model.AutoScalingGroup result;

    // Null or blank cases
    result = obj.getAutoScalingGroup(null);
    assertNull(result);
    result = obj.getAutoScalingGroup("");
    assertNull(result);

    // With group
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getAutoScalingGroup("sampleASGname");
    assertEquals(res.describeAutoScalingGroupsRes.getAutoScalingGroups().get
        (0), result);

    // Without group
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();

    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getAutoScalingGroup("sampleASGname");
    assertNull(result);
  }

  @Test
  public void getLaunchConfigurationForAutoScalingGroup() throws Exception {
    com.amazonaws.services.autoscaling.model.LaunchConfiguration result;
    // Null or blank cases
    result = obj.getLaunchConfigurationForAutoScalingGroup(null);
    assertNull(result);
    result = obj.getLaunchConfigurationForAutoScalingGroup("");
    assertNull(result);

    //Test No ASG with name found case
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getLaunchConfigurationForAutoScalingGroup("RandomASGname");
    assertNull(result);

    // ASG found code for next two scenarios:
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);


    // ASG with name found and LC found - happy case

    DescribeLaunchConfigurationsResult lcResult = new
        DescribeLaunchConfigurationsResult();
    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult.getDescribeLaunchConfigurationsRes();
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getLaunchConfigurationForAutoScalingGroup("RandomASGname");
    assertEquals(lcResult.describeLaunchConfigurationsRes
        .getLaunchConfigurations().get(0), result);

    // ASG with name found but LC not found
    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult
            .getDescribeLaunchConfigurationsResWithEmptyLCS();
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getLaunchConfigurationForAutoScalingGroup("RandomASGname");
    assertNull(result);
  }

  @Test
  public void getLaunchConfigurationByName() throws Exception {
    com.amazonaws.services.autoscaling.model.LaunchConfiguration result;
    // Null or blank cases
    result = obj.getLaunchConfigurationByName(null);
    assertNull(result);
    result = obj.getLaunchConfigurationByName("");
    assertNull(result);

    // LC available, happy case
    DescribeLaunchConfigurationsResult lcResult = new
        DescribeLaunchConfigurationsResult();
    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult.getDescribeLaunchConfigurationsRes();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getLaunchConfigurationByName("RandomASGname");
    assertEquals(lcResult.describeLaunchConfigurationsRes
        .getLaunchConfigurations().get(0), result);

    //LC unavailable
    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult
            .getDescribeLaunchConfigurationsResWithEmptyLCS();
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getLaunchConfigurationByName("RandomASGname");
    assertNull(result);

  }

  @Test
  public void getNewestEc2InstanceInAsg() throws Exception {
    com.amazonaws.services.ec2.model.Instance result;
    // Null or blank cases
    result = obj.getNewestEc2InstanceInAsg(null);
    assertNull(result);
    result = obj.getNewestEc2InstanceInAsg("");
    assertNull(result);

    //Test No ASG with name found case
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getNewestEc2InstanceInAsg("RandomASGname");
    assertNull(result);

    // Returning SG with no instances
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithNoInstances();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getNewestEc2InstanceInAsg("RandomASGname");
    assertNull(result);

    // Returning SG with instances and ec2 service instances
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithInstances();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    mockLib.Ec2Service service = new mockLib.Ec2Service();
    service.ec2Service = mock(Ec2Service.class);
    long timeNow = System.currentTimeMillis();
    mockAws.Instance inst = new mockAws.Instance();
    com.amazonaws.services.ec2.model.Instance[] instanceRes = new com
        .amazonaws.services.ec2.model.Instance[4];
    instanceRes[0] = mockAws.Instance.getInstanceWithGivenLaunchTime(new Date
        (timeNow));
    instanceRes[1] = mockAws.Instance.getInstanceWithGivenLaunchTime(new Date
        (timeNow + 10000));
    instanceRes[2] = mockAws.Instance.getInstanceWithGivenLaunchTime(new Date
        (timeNow + 5000));
    instanceRes[3] = mockAws.Instance.getInstanceWithGivenLaunchTime(new Date
        (timeNow));
    when(service.ec2Service.describeEC2Instance(any())).thenReturn
        (instanceRes[0], instanceRes[1], instanceRes[2], instanceRes[3]);
    obj.Ec2ServiceSetter(service.ec2Service);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getNewestEc2InstanceInAsg("RandomASGname");
    assertEquals(instanceRes[1], result);
  }

  @Test
  public void getScalingActivitiesForAutoScalingGroup() throws Exception {
    List<Activity> result;
    // Null or blank cases
    result = obj.getScalingActivitiesForAutoScalingGroup(null);
    assertNull(result);
    result = obj.getScalingActivitiesForAutoScalingGroup("");
    assertNull(result);

    //Test No ASG with name found case
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getScalingActivitiesForAutoScalingGroup("RandomASGname");
    assertNull(result);


    // Returning SG for other paths
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);

    // happy path with activities available
    DescribeScalingActivitiesResult activityRes = new
        DescribeScalingActivitiesResult();
    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResult();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes
            .scalingActivitiesResult);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getScalingActivitiesForAutoScalingGroup("RandomASGname");
    assertEquals(activityRes.scalingActivitiesResult.getActivities(), result);

    // path with no activities available
    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResultWithNoActivity();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes
            .scalingActivitiesResult);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.getScalingActivitiesForAutoScalingGroup("RandomASGname");
    assertEquals(0, result.size());
  }

  @Test
  public void updateLaunchConfigurationInAutoScalingGroup() throws Exception {
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    obj.updateLaunchConfigurationInAutoScalingGroup("test123456",
        "testing654321");
    Mockito.verify(amazonasg.client, times(1)).updateAutoScalingGroup(any());
  }

  @Test
  public void updateCapacityOfAsg() throws Exception {
    try {
      obj.updateCapacityOfAsg(null, 12345);
    } catch (Exception ex) {
      assertEquals("No Auto Scaling Group name provided", ex.getMessage());
    }

    try {
      obj.updateCapacityOfAsg("", 12345);
    } catch (Exception ex) {
      assertEquals("No Auto Scaling Group name provided", ex.getMessage());
    }

    //Test No ASG with name found case
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.updateCapacityOfAsg("testingthismethod", 5);
    } catch (Exception ex) {
      assertEquals("Auto Scaling Group with provided Name does not exist", ex
          .getMessage());
    }


    // ASG found code but capcity negative
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.updateCapacityOfAsg("testingthismethod", -5);
    } catch (Exception ex) {
      assertEquals("ASG Capacity need to be non-negative!", ex.getMessage());
    }

    obj.updateCapacityOfAsg("testingthismethod", 5);
    verify(amazonasg.client, times(1)).updateAutoScalingGroup(any());

  }

  @Test
  public void createLaunchConfiguration() throws Exception {
    String result;
    try {
      obj.createLaunchConfiguration(null);
    } catch (Exception ex) {
      assertEquals("No parameter for LC provided", ex.getMessage());
    }


    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);

    // request with stub
    CreateLaunchConfigurationRequest req = mock
        (CreateLaunchConfigurationRequest.class);
    when(req.getLaunchConfigurationName()).thenReturn("Sample name");

    obj.AmazonAutoScalingSetter(amazonasg.client);
    obj.createLaunchConfiguration(req);

    verify(amazonasg.client, times(1)).createLaunchConfiguration(any());

  }

  @Test
  public void createAutoScalingGroup() throws Exception {
    String result;
    try {
      obj.createAutoScalingGroup(null);
    } catch (Exception ex) {
      assertEquals("No parameter for ASG provided", ex.getMessage());
    }

    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    // request with stub
    CreateAutoScalingGroupRequest req = mock
        (CreateAutoScalingGroupRequest.class);
    when(req.getAutoScalingGroupName()).thenReturn("Sample name");

    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    DescribeScalingActivitiesResult activityRes = new
        DescribeScalingActivitiesResult();
    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResultWithNoActivityList();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes.scalingActivitiesResult);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.createAutoScalingGroup(req);
      verify(amazonasg.client, times(1)).createLaunchConfiguration(any());
    } catch (Exception ex) {
      assertEquals("Auto Scaling Group with provided Name does not exist", ex
          .getMessage());
    }

    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResultAlternating();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes.scalingActivitiesResult);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    result = obj.createAutoScalingGroup(req);
    assertEquals("Sample name", result);
  }

  @Test
  public void deleteLaunchConfiguration() throws Exception {
    try {
      obj.deleteLaunchConfiguration(null);
    } catch (Exception ex) {
      assertEquals("No Launch Configuration name provided", ex.getMessage());
    }
    try {
      obj.deleteLaunchConfiguration("");
    } catch (Exception ex) {
      assertEquals("No Launch Configuration name provided", ex.getMessage());
    }

    DescribeLaunchConfigurationsResult lcResult = new
        DescribeLaunchConfigurationsResult();
    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult
            .getDescribeLaunchConfigurationsResWithEmptyLCS();

    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.deleteLaunchConfiguration("test lc");
    } catch (Exception ex) {
      assertEquals("Launch Configuration with provided Name does not exist",
          ex.getMessage());
    }


    lcResult.describeLaunchConfigurationsRes =
        DescribeLaunchConfigurationsResult
            .getDescribeLaunchConfigurationsRes();
    when(amazonasg.client.describeLaunchConfigurations(any())).thenReturn
        (lcResult.describeLaunchConfigurationsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    obj.deleteLaunchConfiguration("test lc");
    verify(amazonasg.client, times(1)).deleteLaunchConfiguration(any());
  }

  @Test
  public void deleteAutoScalingGroup() throws Exception {
    try {
      obj.deleteAutoScalingGroup(null);
    } catch (Exception ex) {
      assertEquals("No Auto Scaling Group name provided", ex.getMessage());
    }
    try {
      obj.deleteAutoScalingGroup("");
    } catch (Exception ex) {
      assertEquals("No Auto Scaling Group name provided", ex.getMessage());
    }

    //Test No ASG with name found case
    DescribeAutoScalingGroupsResult res = new
        DescribeAutoScalingGroupsResult();
    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsResWithoutGroup();
    AmazonAutoScaling amazonasg = new AmazonAutoScaling();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.deleteAutoScalingGroup("RandomASGname");
    } catch (Exception ex) {
      assertEquals("Auto Scaling Group with provided Name does not exist", ex
          .getMessage());
    }

    res.describeAutoScalingGroupsRes = DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    amazonasg.client = mock(com.amazonaws.services.autoscaling
        .AmazonAutoScaling.class);
    when(amazonasg.client.describeAutoScalingGroups(any())).thenReturn(res
        .describeAutoScalingGroupsRes);


    DescribeScalingActivitiesResult activityRes = new
        DescribeScalingActivitiesResult();
    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResultWithNoActivityList();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes.scalingActivitiesResult);

    obj.AmazonAutoScalingSetter(amazonasg.client);
    try {
      obj.deleteAutoScalingGroup("RandomASGname");
    } catch (Exception ex) {
      assertEquals("Auto Scaling Group with provided Name does not exist", ex
          .getMessage());
    }


    activityRes.scalingActivitiesResult = DescribeScalingActivitiesResult
        .getScalingActivitiesResultAlternating();
    when(amazonasg.client.describeScalingActivities(any())).thenReturn
        (activityRes.scalingActivitiesResult);
    obj.AmazonAutoScalingSetter(amazonasg.client);
    obj.deleteAutoScalingGroup("RandomASGname");
    verify(amazonasg.client, times(1)).deleteAutoScalingGroup(any());

  }
}
