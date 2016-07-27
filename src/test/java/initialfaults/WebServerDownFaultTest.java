package initialfaults;

import lib.AsgService;
import lib.Ec2Service;
import lib.SshService;
import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/18/16.
 */
public class WebServerDownFaultTest {

  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    log.setupLogForTest();
  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void faultTest() throws Exception {
    HashMap<String, String> params = new HashMap<>();
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    params.put("sshUser", "user");
    params.put("sshKeyFilePath", "/hello");
    params.put("asgName", "asg");

    WebServerDownFault fault = new WebServerDownFault(params);
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    SshService sshService = mockLib.SshService.getSshService();
    fault.ec2ServiceSetter(ec2Service);
    fault.asgServiceSetter(asgService);
    fault.sshServiceSetter(sshService);

    fault.start();

    InOrder inOrder = inOrder(asgService, ec2Service, sshService);
    inOrder.verify(asgService).getAutoScalingGroup(anyString());
    inOrder.verify(ec2Service, atLeastOnce()).describeEC2Instance(anyString());
    inOrder.verify(sshService).executeSshCommands(anyString(), anyString(),
        anyString(), any());

  }

  @Test
  public void faultTestNull() throws Exception {
    HashMap<String, String> params = new HashMap<>();
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    params.put("sshUser", "user");
    params.put("sshKeyFilePath", "/hello");
    params.put("asgName", "asg");

    WebServerDownFault fault = new WebServerDownFault(params);
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    SshService sshService = mockLib.SshService.getSshService();
    thrown.expect(Exception.class);

    fault.start();


  }
}
