package fault;

import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import lib.ASGService;
import lib.EC2Service;
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
public class InstanceUnavailableFaultTest {
    private String instanceName;
    private HashMap<String,String> params;
    private EC2Service ec2Service;
    private ASGService asgService;

    @Before
    public void setUp(){
        instanceName = "hello";
        params = new HashMap<String,String>();
        ec2Service = mock(EC2Service.class);
        asgService = mock(ASGService.class);
        doNothing().when(ec2Service).terminateInstance(anyString());
    }



    @Test
    public void faultTest() throws Exception{
        InstanceUnavailableFault fault = new InstanceUnavailableFault(instanceName,params);
        fault.ec2ServiceSetter(ec2Service);
        fault.start();

        InOrder inOrder = inOrder(ec2Service);

        inOrder.verify(ec2Service).terminateInstance(anyString());

    }
}
