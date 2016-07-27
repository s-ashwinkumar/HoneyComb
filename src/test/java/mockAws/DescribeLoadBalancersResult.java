package mockAws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.model.HealthCheck;
import com.amazonaws.services.elasticloadbalancing.model
    .LoadBalancerDescription;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeLoadBalancersResult {
  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeLoadBalancersResult
      describeLoadBalancersResult;

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeLoadBalancersResult
  getDescribeLoadBalancersResult() {
    describeLoadBalancersResult
        = mock(com.amazonaws.services.elasticloadbalancing.model
        .DescribeLoadBalancersResult.class);
    LoadBalancerDescription elb = mock(LoadBalancerDescription.class);
    HealthCheck hc = mock(HealthCheck.class);
    when(elb.getHealthCheck()).thenReturn(hc);
    List<LoadBalancerDescription> elbs = new ArrayList<>();
    elbs.add(elb);

    when(describeLoadBalancersResult.getLoadBalancerDescriptions())
        .thenReturn(elbs);
    return describeLoadBalancersResult;

  }

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeLoadBalancersResult
  getDescribeLoadBalancersResultWithEmptyList() {
    describeLoadBalancersResult
        = mock(com.amazonaws.services.elasticloadbalancing.model
        .DescribeLoadBalancersResult.class);
    List<LoadBalancerDescription> elbs = new ArrayList<>();
    when(describeLoadBalancersResult.getLoadBalancerDescriptions())
        .thenReturn(elbs);
    return describeLoadBalancersResult;

  }

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeLoadBalancersResult
  getDescribeLoadBalancersResultWithException() {
    describeLoadBalancersResult
        = mock(com.amazonaws.services.elasticloadbalancing.model
        .DescribeLoadBalancersResult.class);
    when(describeLoadBalancersResult.getLoadBalancerDescriptions()).thenThrow
        (AmazonServiceException.class);
    return describeLoadBalancersResult;

  }
}
