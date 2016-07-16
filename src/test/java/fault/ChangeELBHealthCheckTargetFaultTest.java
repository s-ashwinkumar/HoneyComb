package fault;

import lib.AsgService;
import lib.ElbService;
import org.junit.Test;

import java.util.HashMap;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;
/**
 * Created by wilsoncao on 7/16/16.
 */
public class ChangeELBHealthCheckTargetFaultTest {

  @Test
  public void faultTest() throws Exception {
//    HashMap<String,String> params = new HashMap<>();
//    params.put("faultInstanceId","asdfjasldfkjasdf");
//    params.put("asgName", "asg");
//    params.put("faultyHealthCheckTarget","\\health");
//    ChangeELBHealthCheckTargetFault fault = new ChangeELBHealthCheckTargetFault(params);
//    AsgService asgService = mockLib.AsgService.getAsgService();
//    ElbService elbService = mockLib.ElbService.getElbService();
//    fault.asgServiceSetter(asgService);
//    fault.elbServiceSetter(elbService);
//    fault.start();
//
//    InOrder inOrder = inOrder(asgService,elbService);
//    inOrder.verify(asgService).getAutoScalingGroup(any());
//    inOrder.verify(elbService).updateElbHealthCheckTarget(any(),any());
//
  }
}
