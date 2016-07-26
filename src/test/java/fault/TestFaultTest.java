package fault;

import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by wilsoncao on 7/18/16.
 */
public class TestFaultTest {

  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    log.setupLogForTest();
  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }

  @Test
  public void faultTest() throws Exception {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    TestFault fault = new TestFault(params);
    fault.start();

  }


  @Test
  public void cancelTest() throws Exception {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("faultInstanceId", "asdfjasldfkjasdf");
    TestFault fault = new TestFault(params);
    fault.start();
    Thread.sleep(5000);
    fault.terminate();

  }
}
