package mockAws;

import com.amazonaws.services.autoscaling.model.Instance;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
/**
 * Created by wilsoncao on 7/13/16.
 */
public class AutoScalingGroup {
  public static com.amazonaws.services.autoscaling.model.AutoScalingGroup asg;

  public static com.amazonaws.services.autoscaling.model.AutoScalingGroup getAsg(){
    asg = mock(com.amazonaws.services.autoscaling.model.AutoScalingGroup.class);
    List<Instance> list = new ArrayList<>();
    list.add(AsgInstance.getInstance());
    list.add(AsgInstance.getInstance());
    list.add(AsgInstance.getInstance());
    when(asg.getInstances()).thenReturn(list);

    List<String> lbs = new ArrayList<>();
    lbs.add("lb");
    when(asg.getLoadBalancerNames()).thenReturn(lbs);
    return asg;
  }

  public static com.amazonaws.services.autoscaling.model.AutoScalingGroup
  getAsgWithNoInstance(){
    asg = mock(com.amazonaws.services.autoscaling.model.AutoScalingGroup.class);
    List<Instance> list = new ArrayList<>();
    when(asg.getInstances()).thenReturn(list);

    List<String> lbs = new ArrayList<>();
    lbs.add("lb");
    when(asg.getLoadBalancerNames()).thenReturn(lbs);
    return asg;
  }
}
