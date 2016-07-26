package fault;

import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import lib.ElbService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;

/**
 * Created by wilsoncao on 6/17/16.
 */
public class ELBUnavailableFaultTest {
  private HashMap<String, String> params;
  private ElbService elbService;
  private AmazonElasticLoadBalancing lb;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    params = new HashMap<String, String>();
    elbService = mockLib.ElbService.getElbService();
    lb = mockAws.AmazonElasticLoadBalancing.getLoadBalancer();
  }


  @Test
  public void faultTest() throws Exception {
    params.put("elbName", "true");
    params.put("faultInstanceId", "asdfjasldfkjasdf;");
    ElbUnavailableFault fault = new ElbUnavailableFault(params);
    fault.elbServiceSetter(elbService);
    fault.elbSetter(lb);
    fault.start();

    InOrder inOrder = inOrder(elbService, lb);

    inOrder.verify(elbService).describeLoadBalancer(anyString());
    inOrder.verify(lb).deleteLoadBalancer(any());


  }

  @Test
  public void elbNonExistsTest() throws Exception {
    HashMap<String, String> params = new HashMap<>();
    params.put("elbName", "false");
    params.put("faultInstanceId", "asdfjasldfkjasdf;");
    ElbUnavailableFault fault = new ElbUnavailableFault(params);
    fault.elbServiceSetter(elbService);
    fault.elbSetter(lb);
    thrown.expect(HoneyCombException.class);
    fault.start();

  }

  @Test
  public void faultTestNull() throws Exception {
    params.put("elbName", "true");
    params.put("faultInstanceId", "asdfjasldfkjasdf;");
    ElbUnavailableFault fault = new ElbUnavailableFault(params);
    thrown.expect(HoneyCombException.class);
    fault.start();
  }

}
