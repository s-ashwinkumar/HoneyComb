package fault;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
import lib.AsgService;
import lib.Ec2Service;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;


import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 6/14/16.
 */
public class ChangeAMIInLCFaultTest {
    private static Ec2Service ec2Service;
    private static AsgService asgService;
    private static LaunchConfiguration lc;
    private static CreateLaunchConfigurationRequest req;
    private static Instance instance;
    private static String asgName;
    private static String faultyAmiId;
    private static AutoScalingGroup asg;
    private static HashMap<String,String> params;

    @Rule
    public ExpectedException thrown= ExpectedException.none();


    @Before
    public void setUp() {
        ec2Service = mockLib.Ec2Service.getEc2Service();
        asgService = mockLib.AsgService.getAsgService();
        asg = mockAws.AutoScalingGroup.getAsg();
        asgName = "asg";

        faultyAmiId = "faultyAmiId";
        params = new HashMap<String,String>();

    }

    @Test
    public void faultTest() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("asgName",asgName);
        params.put("faultyAmiId",faultyAmiId);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        ChangeAmiInLcFault fault = new ChangeAmiInLcFault(params);
        fault.ec2ServiceSetter(ec2Service);
        fault.asgServiceSetter(asgService);
        fault.asgSetter(asg);
        fault.start();

        InOrder inOrder = inOrder(asgService,ec2Service);
        inOrder.verify(asgService).getLaunchConfigurationForAutoScalingGroup(asgName);
        inOrder.verify(asgService).createLaunchConfiguration(any());
        inOrder.verify(asgService).updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");
        inOrder.verify(ec2Service).terminateInstance(anyString());


    }

    @Test
    public void faultTestNull() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("asgName",asgName);
        params.put("faultyAmiId",faultyAmiId);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        ChangeAmiInLcFault fault = new ChangeAmiInLcFault(params);
        thrown.expect(HoneyCombException.class);
        fault.start();



    }


}
