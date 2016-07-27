package mockLib;

import com.amazonaws.services.elasticloadbalancing.model
    .LoadBalancerDescription;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class ElbService {
  public static lib.ElbService elbService;

  public static lib.ElbService getElbService() {
    elbService = mock(lib.ElbService.class);
    LoadBalancerDescription lbDescription = mock(LoadBalancerDescription.class);
    when(elbService.describeLoadBalancer(eq("true"))).thenReturn(lbDescription);
    when(elbService.describeLoadBalancer(eq("false"))).thenReturn(null);
    return elbService;
  }
}
