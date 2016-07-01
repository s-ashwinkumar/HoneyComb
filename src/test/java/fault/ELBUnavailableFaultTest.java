package fault;

import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import lib.ElbService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import java.util.HashMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 6/17/16.
 */
public class ELBUnavailableFaultTest {
    private String elbName;
    private HashMap<String,String> params;
    private ElbService elbService;
    private AmazonElasticLoadBalancing lb;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setUp(){
        elbName = "lb";
        params = new HashMap<String,String>();
        elbService = mock(ElbService.class);
        lb = mock(AmazonElasticLoadBalancing.class);
        doNothing().when(lb).deleteLoadBalancer(any());
    }



    @Test
    public void faultTest() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("elbName",elbName);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        ElbUnavailableFault fault = new ElbUnavailableFault(params);
        LoadBalancerDescription lbDescription  = mock(LoadBalancerDescription.class);
        when(elbService.describeLoadBalancer(anyString())).thenReturn(lbDescription);
        fault.elbServiceSetter(elbService);
        fault.elbSetter(lb);
        fault.start();

        InOrder inOrder = inOrder(elbService,lb);

        inOrder.verify(elbService).describeLoadBalancer(anyString());
        inOrder.verify(lb).deleteLoadBalancer(any());


    }

    @Test
    public void elbNonExistsTest() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("elbName",elbName);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        ElbUnavailableFault fault = new ElbUnavailableFault(params);
        when(elbService.describeLoadBalancer(anyString())).thenReturn(null);
        fault.elbServiceSetter(elbService);
        fault.elbSetter(lb);
        thrown.expect(HoneyCombException.class);
        fault.start();

    }
}
