package fault;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
import com.amazonaws.services.ec2.model.InstanceState;
import lib.ASGService;
import lib.EC2Service;
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
    private static EC2Service ec2Service;
    private static ASGService asgService;
    private static LaunchConfiguration lc;
    private static CreateLaunchConfigurationRequest req;
    private static Instance instance;
    private static String asgName;
    private static String faultyAmiId;
    private static AutoScalingGroup asg;
    private static HashMap<String,String> params;



    @Before
    public void setUp() {
        ec2Service = mock(EC2Service.class);
        asgService = mock(ASGService.class);
        lc = mock(LaunchConfiguration.class);
        instance = mock(Instance.class);
        asg = mock(AutoScalingGroup.class);
        asgName = "asg";
        faultyAmiId = "faultyAmiId";
        params = new HashMap<String,String>();
        com.amazonaws.services.autoscaling.model.Instance asgInstance =
                mock(com.amazonaws.services.autoscaling.model.Instance.class);

        // Setting the behavior for mock
        when(asgService.getLaunchConfigurationForAutoScalingGroup(asgName)).thenReturn(lc);
        when(asgService.createLaunchConfiguration(any())).thenReturn("ok");
        when(lc.getInstanceType()).thenReturn("ok");
        when(lc.getKeyName()).thenReturn("ok");
        when(lc.getSecurityGroups()).thenReturn(null);
        doNothing().when(asgService).updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");

        List<com.amazonaws.services.autoscaling.model.Instance> list = new ArrayList<>();
        list.add(asgInstance);
        list.add(asgInstance);

        when(asg.getInstances()).thenReturn(list);

        when(ec2Service.describeEC2Instance(anyString())).thenReturn(instance);
        InstanceState state = mock(InstanceState.class);
        when(instance.getState()).thenReturn(state);
        when(state.getName()).thenReturn("running");
        doNothing().when(ec2Service).terminateInstance(anyString());

    }

    @Test
    public void faultTest() throws Exception{
        ChangeAMIInLCFault fault = new ChangeAMIInLCFault(asgName,faultyAmiId,params);
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
