package lib;

import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.model.*;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by wilsoncao on 7/13/16.
 */
public class AsgServiceImplTest {
  private static Ec2Service ec2Service;
  private static String faultInstanceId;
  private static String asgName;
  private static AmazonAutoScaling client;



  @Before
  public void setUp() {
    ec2Service = mock(Ec2Service.class);
    client = mock(AmazonAutoScaling.class);
    DescribeAutoScalingGroupsResult asgRes = mock(DescribeAutoScalingGroupsResult.class);
    when(client.describeAutoScalingGroups(any())).thenReturn(asgRes);
    List<AutoScalingGroup> asgs = new ArrayList<>();
    AutoScalingGroup group = mock(AutoScalingGroup.class);
    asgs.add(group);
    when(asgRes.getAutoScalingGroups()).thenReturn(asgs);

    DescribeLaunchConfigurationsResult lcRes = mock(DescribeLaunchConfigurationsResult.class);
    when(client.describeLaunchConfigurations(any())).thenReturn(lcRes);
    List<LaunchConfiguration> lcs = new ArrayList<>();
    LaunchConfiguration lc = mock(LaunchConfiguration.class);
    lcs.add(lc);
    when(lcRes.getLaunchConfigurations()).thenReturn(lcs);

    List<Activity> activityRes = new ArrayList<>();
    when(client.describeScalingActivities(any()).getActivities()).thenReturn(activityRes);


  }


}
