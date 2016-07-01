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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 6/14/16.
 */
public class ChangeAMIInLCFaultTest {
    private static Ec2Service Ec2Service;
    private static AsgService AsgService;
    private static LaunchConfiguration lc;
    private static CreateLaunchConfigurationRequest req;
    private static Instance instance;
    private static String asgName;
    private static String faultyAmiId;
    private static AutoScalingGroup asg;
    private static HashMap<String,String> params;



    @Before
    public void setUp() {
        Ec2Service = mock(Ec2Service.class);
        AsgService = mock(AsgService.class);
        lc = mock(LaunchConfiguration.class);
        instance = mock(Instance.class);
        asg = mock(AutoScalingGroup.class);
        asgName = "asg";
        faultyAmiId = "faultyAmiId";
        params = new HashMap<String,String>();
        com.amazonaws.services.autoscaling.model.Instance asgInstance =
                mock(com.amazonaws.services.autoscaling.model.Instance.class);

        // Setting the behavior for mock
        when(AsgService.getLaunchConfigurationForAutoScalingGroup(asgName)).thenReturn(lc);
        when(AsgService.createLaunchConfiguration(any())).thenReturn("ok");
        when(lc.getInstanceType()).thenReturn("ok");
        when(lc.getKeyName()).thenReturn("ok");
        when(lc.getSecurityGroups()).thenReturn(null);
        doNothing().when(AsgService).updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");

        List<com.amazonaws.services.autoscaling.model.Instance> list = new ArrayList<>();
        list.add(asgInstance);
        list.add(asgInstance);

        when(asg.getInstances()).thenReturn(list);

        when(Ec2Service.describeEC2Instance(anyString())).thenReturn(instance);
        InstanceState state = mock(InstanceState.class);
        when(instance.getState()).thenReturn(state);
        when(state.getName()).thenReturn("running");
        doNothing().when(Ec2Service).terminateInstance(anyString());

    }

    @Test
    public void faultTest() throws Exception{
        HashMap<String,String> params = new HashMap<>();
        params.put("asgName",asgName);
        params.put("faultyAmiId",faultyAmiId);
        params.put("faultInstanceId", "asdfjasldfkjasdf;");
        ChangeAmiInLcFault fault = new ChangeAmiInLcFault(params);
        fault.ec2ServiceSetter(Ec2Service);
        fault.asgServiceSetter(AsgService);
        fault.asgSetter(asg);
        fault.start();

        InOrder inOrder = inOrder(AsgService,Ec2Service);
        inOrder.verify(AsgService).getLaunchConfigurationForAutoScalingGroup(asgName);
        inOrder.verify(AsgService).createLaunchConfiguration(any());
        inOrder.verify(AsgService).updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");
        inOrder.verify(Ec2Service).terminateInstance(anyString());


    }


}
