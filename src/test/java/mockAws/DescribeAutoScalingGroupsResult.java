package mockAws;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeAutoScalingGroupsResult {
  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult describeAutoScalingGroupsRes;

  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsRes() {
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling
        .model.DescribeAutoScalingGroupsResult.class);
    List<com.amazonaws.services.autoscaling.model.AutoScalingGroup> asgs =
        new ArrayList<>();
    com.amazonaws.services.autoscaling.model.AutoScalingGroup group =
        mock(com.amazonaws.services.autoscaling.model.AutoScalingGroup.class);
    asgs.add(group);
    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(asgs);
    return describeAutoScalingGroupsRes;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsResWithoutGroup() {
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling
        .model.DescribeAutoScalingGroupsResult.class);
    List<com.amazonaws.services.autoscaling.model.AutoScalingGroup> asgs =
        new ArrayList<>();
    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(asgs);
    return describeAutoScalingGroupsRes;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsResWithInstances() {
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling
        .model.DescribeAutoScalingGroupsResult.class);

    List<com.amazonaws.services.autoscaling.model.AutoScalingGroup> asgs =
        new ArrayList<>();
    AutoScalingGroup asgGroup = new AutoScalingGroup();
    asgGroup.asg = AutoScalingGroup.getAsg();
    asgs.add(asgGroup.asg);
    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(asgs);
    return describeAutoScalingGroupsRes;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsResWithNoInstances() {
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling
        .model.DescribeAutoScalingGroupsResult.class);

    List<com.amazonaws.services.autoscaling.model.AutoScalingGroup> asgs =
        new ArrayList<>();
    AutoScalingGroup asgGroup = new AutoScalingGroup();
    asgGroup.asg = AutoScalingGroup.getAsgWithNoInstance();
    asgs.add(asgGroup.asg);
    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(asgs);
    return describeAutoScalingGroupsRes;
  }

  public static com.amazonaws.services.autoscaling.model
      .DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsResWithNull() {
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling
        .model.DescribeAutoScalingGroupsResult.class);

    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(null);
    return describeAutoScalingGroupsRes;
  }

}
