package fault;

import lib.AsgService;
import mockLib.Ec2Service;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.junit.rules.ExpectedException;
import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;


/**
 * Created by wilsoncao on 7/16/16.
 */
public class ChangeSecurityGroupInLcFaultTest {
  @Rule
  public ExpectedException thrown= ExpectedException.none();
  @Test
  public void faultTest() throws Exception {
    HashMap<String,String> params = new HashMap<>();
    params.put("faultySecurityGroupName","faulty");
    params.put("asgName", "asg");
    params.put("faultInstanceId","asdfjasldfkjasdf");
    lib.Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    ChangeSecurityGroupInLcFault fault = new ChangeSecurityGroupInLcFault(params);
    fault.asgServiceSetter(asgService);
    fault.ec2ServiceSetter(ec2Service);
    fault.start();

    InOrder inOrder = inOrder(asgService,ec2Service);
    inOrder.verify(asgService).getLaunchConfigurationForAutoScalingGroup(any());
    inOrder.verify(asgService).createLaunchConfiguration(any());
    inOrder.verify(asgService).updateLaunchConfigurationInAutoScalingGroup(any(),any());
    inOrder.verify(asgService).getAutoScalingGroup(any());
    inOrder.verify(ec2Service,atLeast(1)).describeEC2Instance(any());

  }


  @Test
  public void faultTestEmpty() throws Exception {
    HashMap<String,String> params = new HashMap<>();
    params.put("faultySecurityGroupName","faulty");
    params.put("asgName", "null");
    params.put("faultInstanceId","asdfjasldfkjasdf");
    lib.Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    ChangeSecurityGroupInLcFault fault = new ChangeSecurityGroupInLcFault(params);
    fault.asgServiceSetter(asgService);
    fault.ec2ServiceSetter(ec2Service);
    thrown.expect(HoneyCombException.class);
    fault.start();


  }
}
