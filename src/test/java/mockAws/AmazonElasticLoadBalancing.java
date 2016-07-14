package mockAws;

import com.amazonaws.services.elasticloadbalancing.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AmazonElasticLoadBalancing {
  private static com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing loadBalancer;

  public static com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing getLoadBalancer(){
    loadBalancer = mock(com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing.class);
    doNothing().when(loadBalancer).deleteLoadBalancer(any());
    com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult describeLoadBalancersResult = mockAws
        .DescribeLoadBalancersResult.getDescribeLoadBalancersResult();
    when(loadBalancer.describeLoadBalancers(any())).thenReturn(describeLoadBalancersResult);
    com.amazonaws.services.elasticloadbalancing.model.DescribeInstanceHealthResult describeInstanceHealthResult = mockAws
        .DescribeInstanceHealthResult.getDescribeInstanceHealthResult();
    when(loadBalancer.describeInstanceHealth(any())).thenReturn(describeInstanceHealthResult);

    when(loadBalancer.configureHealthCheck(any())).thenReturn(null);

//    doNothing().when(loadBalancer).configureHealthCheck(any());
    when(loadBalancer.addTags(any())).thenReturn(null);
//    doNothing().when(loadBalancer).addTags(any());

    List<TagDescription> tagDescriptions = new ArrayList<>();
    TagDescription tagDescription = mock(TagDescription.class);
    tagDescriptions.add(tagDescription);

//    when(loadBalancer.describeTags(any()).getTagDescriptions()).thenReturn(tagDescriptions);

//    doNothing().when(loadBalancer).removeTags(any());
    when(loadBalancer.removeTags(any())).thenReturn(null);
    return loadBalancer;
  }

}
