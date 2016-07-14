package mockAws;

import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeLoadBalancersResult {
  private static com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult
      describeLoadBalancersResult;
  public static com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult
  getDescribeLoadBalancersResult(){
      describeLoadBalancersResult
        = mock(com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult.class);
    LoadBalancerDescription elb = mock(LoadBalancerDescription.class);
    List<LoadBalancerDescription> elbs = new ArrayList<>();
    elbs.add(elb);
    when(describeLoadBalancersResult.getLoadBalancerDescriptions()).thenReturn(elbs);
    return describeLoadBalancersResult;

  }
}
