package fault;

import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import lib.ELBService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import java.util.HashMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 6/17/16.
 */
public class ELBUnavailableFaultTest {
    private String asgName;
    private String elbName;
    private HashMap<String,String> params;
    private ELBService elbService;

    @Before
    public void setUp(){
        asgName = "hello";
        elbName = "hello";
        params = new HashMap<String,String>();
        elbService = mock(ELBService.class);
        LoadBalancerDescription lbDescription  = mock(LoadBalancerDescription.class);
        when(elbService.describeLoadBalancer(anyString())).thenReturn(lbDescription);
        doNothing().when(elbService).tagELB(elbName, "elb_experiment_status", "unavailable");
    }



    @Test
    public void faultTest() throws Exception{
        ELBUnavailableFault fault = new ELBUnavailableFault(asgName,elbName,params);
        fault.elbServiceSetter(elbService);
        fault.start();

        InOrder inOrder = inOrder(elbService);

        inOrder.verify(elbService).describeLoadBalancer(anyString());
        inOrder.verify(elbService).tagELB(elbName, "elb_experiment_status", "unavailable");


    }
}
