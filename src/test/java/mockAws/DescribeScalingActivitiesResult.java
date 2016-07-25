package mockAws;

import com.amazonaws.services.autoscaling.model.Activity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ashwin on 7/19/16.
 */
public class DescribeScalingActivitiesResult {
  public static com.amazonaws.services.autoscaling.model
      .DescribeScalingActivitiesResult scalingActivitiesResult;

  public static com.amazonaws.services.autoscaling.model
      .DescribeScalingActivitiesResult getScalingActivitiesResult() {
    scalingActivitiesResult = mock(com.amazonaws.services.autoscaling.model
        .DescribeScalingActivitiesResult.class);
    Activity activity = mock(Activity.class);
    when(activity.getStatusCode()).thenReturn("In Progress");
    List<Activity> activities = new ArrayList<>();
    activities.add(activity);
    when(scalingActivitiesResult.getActivities()).thenReturn(activities);
    return scalingActivitiesResult;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeScalingActivitiesResult
  getScalingActivitiesResultWithNoActivity() {
    scalingActivitiesResult = mock(com.amazonaws.services.autoscaling.model
        .DescribeScalingActivitiesResult.class);
    List<Activity> activities = new ArrayList<>();
    when(scalingActivitiesResult.getActivities()).thenReturn(activities);
    return scalingActivitiesResult;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeScalingActivitiesResult
  getScalingActivitiesResultWithNoActivityList() {
    scalingActivitiesResult = mock(com.amazonaws.services.autoscaling.model
        .DescribeScalingActivitiesResult.class);
    when(scalingActivitiesResult.getActivities()).thenReturn(null);
    return scalingActivitiesResult;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeScalingActivitiesResult getScalingActivitiesResultAlternating() {
    scalingActivitiesResult = mock(com.amazonaws.services.autoscaling.model
        .DescribeScalingActivitiesResult.class);
    Activity activity = mock(Activity.class);
    when(activity.getStatusCode()).thenReturn("In Progress");
    List<Activity> activities1 = new ArrayList<>();
    activities1.add(activity);
    List<Activity> activities2 = new ArrayList<>();
    when(scalingActivitiesResult.getActivities()).thenReturn(activities1,
        activities2);
    return scalingActivitiesResult;
  }

}
