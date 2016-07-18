package mockLib;

import com.amazonaws.services.ec2.model.Instance;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class Ec2Service {
  private static lib.Ec2Service ec2Service;

  public static lib.Ec2Service getEc2Service(){
    ec2Service = mock(lib.Ec2Service.class);
    Instance instance = mockAws.Instance.getInstance();
    when(ec2Service.describeEC2Instance(anyString())).thenReturn(instance);
    try {
      doNothing().when(ec2Service).terminateInstance(anyString());
      doNothing().when(ec2Service).revokeSecurityGroupInboundRule(anyString(), anyString(), anyInt(), anyString());
    }catch(Exception e){
      e.printStackTrace();
    }
    return ec2Service;
  }
}
