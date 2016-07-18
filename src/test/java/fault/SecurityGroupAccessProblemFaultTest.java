package fault;

import lib.AsgService;
import lib.Ec2Service;
import lib.ElbService;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;

/**
 * Created by wilsoncao on 7/16/16.
 */
public class SecurityGroupAccessProblemFaultTest {
  @Test
  public void faultTest() throws Exception {
    HashMap<String,String> params = new HashMap<>();
    params.put("faultInstanceId","asdfjasldfkjasdf");
    params.put("asgName", "asg");
    params.put("failedSecurityGroupName","failed");
    SecurityGroupAccessProblemFault fault = new SecurityGroupAccessProblemFault(params);
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    fault.ec2ServiceSetter(ec2Service);
    fault.start();

    InOrder inOrder = inOrder(ec2Service);
    inOrder.verify(ec2Service).revokeSecurityGroupInboundRule(anyString(),anyString(),anyInt(),anyString());
  }
}
