package mockAws;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AmazonElasticLoadBalancing {
  private static com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing loadBalancer;

  public static com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing getLoadBalancer(){
    doNothing().when(loadBalancer).deleteLoadBalancer(any());
    return loadBalancer;
  }

}
