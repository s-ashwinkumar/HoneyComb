package initialfaults;

import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by wilsoncao on 6/22/16.
 */
public class HoneyCombExceptionTest {
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
  public void exceptionTest() throws Exception {
    HoneyCombException exception = new HoneyCombException("this is an " +
        "exception");
  }
}
