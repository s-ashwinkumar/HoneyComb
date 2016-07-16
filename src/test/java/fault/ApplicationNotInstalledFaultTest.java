package fault;

import lib.AsgService;
import lib.Ec2Service;
import lib.SshService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;
import java.util.HashMap;

/**
 * Created by wilsoncao on 7/16/16.
 */
public class ApplicationNotInstalledFaultTest {

  private String sshUser = "admin";
  private String asgName = "abc";
  private String sshKeyFilePath = "/hello/world";
  private HashMap<String,String> params;

  @Test
  public void faultTest() throws Exception{
    params = new HashMap<String,String>();
    params.put("sshUser",sshUser);
    params.put("asgName",asgName);
    params.put("sshKeyFilePath",sshKeyFilePath);
    params.put("faultInstanceId","asdfjasldfkjasdf");
    ApplicationNotInstalledFault fault = new ApplicationNotInstalledFault(params);
    SshService sshService = mockLib.SshService.getSshService();
    AsgService asgService = mockLib.AsgService.getAsgService();
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    fault.sshServiceSetter(sshService);
    fault.asgServiceSetter(asgService);
    fault.ec2ServiceSetter(ec2Service);
    fault.start();

    InOrder inOrder = inOrder(asgService,ec2Service,sshService);
    inOrder.verify(asgService).getAutoScalingGroup(any());
    inOrder.verify(ec2Service,atLeast(1)).describeEC2Instance(any());
    inOrder.verify(sshService).executeSshCommands(any(),any(),any(),any());


  }
}
