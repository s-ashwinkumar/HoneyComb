package mockAws;

import com.amazonaws.services.autoscaling.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeAutoScalingGroupsResult {
  private static com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult describeAutoScalingGroupsRes;

  public static com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult
  getDescribeAutoScalingGroupsRes(){
    describeAutoScalingGroupsRes = mock(com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult.class);
    List<com.amazonaws.services.autoscaling.model.AutoScalingGroup> asgs = new ArrayList<>();
    com.amazonaws.services.autoscaling.model.AutoScalingGroup group =
        mock(com.amazonaws.services.autoscaling.model.AutoScalingGroup.class);
    asgs.add(group);
    when(describeAutoScalingGroupsRes.getAutoScalingGroups()).thenReturn(asgs);
    return describeAutoScalingGroupsRes;
  }

}
