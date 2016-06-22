package fault;

import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import lib.AsgService;
import lib.Ec2Service;
import lib.ElbService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 6/17/16.
 */
public class InstanceUnavailableFaultTest {
    private String instanceName;
    private HashMap<String,String> params;
    private Ec2Service ec2Service;

    @Before
    public void setUp(){
        instanceName = "hello";
        params = new HashMap<String,String>();
        ec2Service = mock(Ec2Service.class);
        doNothing().when(ec2Service).terminateInstance(anyString());
    }



    @Test
    public void faultTest() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("instanceId",instanceName);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        InstanceUnavailableFault fault = new InstanceUnavailableFault(params);
        fault.ec2ServiceSetter(ec2Service);
        fault.start();

        InOrder inOrder = inOrder(ec2Service);

        inOrder.verify(ec2Service).terminateInstance(anyString());

    }
}
