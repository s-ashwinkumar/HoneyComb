package fault;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
import com.amazonaws.services.ec2.model.InstanceState;
import lib.AsgService;
import lib.Ec2Service;
import org.junit.Before;
import org.junit.Test;
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



    @Before
    public void setUp() {
//        Ec2Service = mock(Ec2Service.class);
        ec2Service = mockLib.Ec2Service.getEc2Service();
//        asgService = mock(AsgService.class);
        asgService = mockLib.AsgService.getAsgService();
//        lc = mockAws(LaunchConfiguration.class);
//        lc = mockAws.LaunchConfiguration.getLc();
//        instance = mock(Instance.class);
//        instance = mockAws.Instance.getInstance();
//        asg = mockAws(AutoScalingGroup.class);
        asg = mockAws.AutoScalingGroup.getAsg();
        asgName = "asg";

        faultyAmiId = "faultyAmiId";
        params = new HashMap<String,String>();
//        com.amazonaws.services.autoscaling.model.Instance asgInstance =
//                mockAws(com.amazonaws.services.autoscaling.model.Instance.class);

        // Setting the behavior for mockAws
//        when(AsgService.getLaunchConfigurationForAutoScalingGroup(asgName)).thenReturn(lc);
//        when(AsgService.createLaunchConfiguration(any())).thenReturn("ok");
//        when(lc.getInstanceType()).thenReturn("ok");
//        when(lc.getKeyName()).thenReturn("ok");
//        when(lc.getSecurityGroups()).thenReturn(null);
//        doNothing().when(AsgService).updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");

//        List<com.amazonaws.services.autoscaling.model.Instance> list = new ArrayList<>();
//        list.add(asgInstance);
//        list.add(asgInstance);

//        when(asg.getInstances()).thenReturn(list);

//        when(Ec2Service.describeEC2Instance(anyString())).thenReturn(instance);
//        InstanceState state = mock(InstanceState.class);
//        when(instance.getState()).thenReturn(state);
//        when(state.getName()).thenReturn("running");
//        doNothing().when(Ec2Service).terminateInstance(anyString());

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


}
