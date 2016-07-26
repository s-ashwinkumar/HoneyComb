package fault;

import lib.AsgService;
import lib.Ec2Service;
import lib.SshService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/16/16.
 */
public class ApplicationNotInstalledFaultTest {

  private String sshUser = "admin";
  private String asgName = "abc";
  private String sshKeyFilePath = "/hello/world";
  private HashMap<String, String> params;
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
    params = new HashMap<String, String>();
    params.put("sshUser", sshUser);
    params.put("asgName", asgName);
    params.put("sshKeyFilePath", sshKeyFilePath);
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    ApplicationNotInstalledFault fault = new ApplicationNotInstalledFault
        (params);
    SshService sshService = mockLib.SshService.getSshService();
    AsgService asgService = mockLib.AsgService.getAsgService();
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    fault.sshServiceSetter(sshService);
    fault.asgServiceSetter(asgService);
    fault.ec2ServiceSetter(ec2Service);
    fault.start();

    InOrder inOrder = inOrder(asgService, ec2Service, sshService);
    inOrder.verify(asgService).getAutoScalingGroup(any());
    inOrder.verify(ec2Service, atLeast(1)).describeEC2Instance(any());
    inOrder.verify(sshService).executeSshCommands(any(), any(), any(), any());

  }

  @Test
  public void faultTestNull() throws Exception {
    params = new HashMap<String, String>();
    params.put("sshUser", sshUser);
    params.put("asgName", asgName);
    params.put("sshKeyFilePath", sshKeyFilePath);
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    ApplicationNotInstalledFault fault = new ApplicationNotInstalledFault
        (params);
    SshService sshService = mockLib.SshService.getSshService();
    AsgService asgService = mockLib.AsgService.getAsgService();
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    thrown.expect(HoneyCombException.class);
    fault.start();


  }
}
