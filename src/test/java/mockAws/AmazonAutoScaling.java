package mockAws;

import com.amazonaws.services.autoscaling.model.Activity;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult;
import com.amazonaws.services.autoscaling.model
    .DescribeLaunchConfigurationsResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AmazonAutoScaling {
  public static com.amazonaws.services.autoscaling.AmazonAutoScaling client;

  public static com.amazonaws.services.autoscaling.AmazonAutoScaling
  getClient() {
    client = mock(com.amazonaws.services.autoscaling.AmazonAutoScaling.class);
    DescribeAutoScalingGroupsResult asgRes = mockAws
        .DescribeAutoScalingGroupsResult
        .getDescribeAutoScalingGroupsRes();
    when(client.describeAutoScalingGroups(any())).thenReturn(asgRes);

    DescribeLaunchConfigurationsResult lcRes = mockAws
        .DescribeLaunchConfigurationsResult
        .getDescribeLaunchConfigurationsRes();
    when(client.describeLaunchConfigurations(any())).thenReturn(lcRes);

    List<Activity> activityRes = new ArrayList<>();

    Activity activity = mock(Activity.class);
    activityRes.add(activity);
    when(client.describeScalingActivities(any()).getActivities()).thenReturn
        (activityRes);

    doNothing().when(client).updateAutoScalingGroup(any());
    doNothing().when(client).createLaunchConfiguration(any());
    doNothing().when(client).createAutoScalingGroup(any());
    doNothing().when(client).deleteLaunchConfiguration(any());
    doNothing().when(client).deleteAutoScalingGroup(any());
    return client;
  }
}
