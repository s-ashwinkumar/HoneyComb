package fault;

import lib.AsgService;
import lib.Ec2Service;
import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.util.HashMap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;

/**
 * Created by wilsoncao on 7/21/16.
 */
public class LaunchPendingFaultTest {
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
    params.put("asgName", "asg");
    LaunchPendingFault fault = new LaunchPendingFault(params);
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    fault.ec2ServiceSetter(ec2Service);
    fault.asgServiceSetter(asgService);
    fault.start();

    InOrder inOrder = inOrder(asgService, ec2Service);
    inOrder.verify(asgService).getAutoScalingGroup(anyString());
    inOrder.verify(ec2Service, atLeastOnce()).describeEC2Instance(anyString());
    inOrder.verify(ec2Service).tagEC2Instance(anyString(), anyString(),
        anyString());
  }

  @Test
  public void faultTestNull() throws Exception {
    HashMap<String, String> params = new HashMap<>();
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    params.put("asgName", "asg");
    LaunchPendingFault fault = new LaunchPendingFault(params);
    Ec2Service ec2Service = mockLib.Ec2Service.getEc2Service();
    AsgService asgService = mockLib.AsgService.getAsgService();
    thrown.expect(Exception.class);
    fault.start();

  }
}
