package fault;

import org.junit.Test;

/**
 * Created by wilsoncao on 6/22/16.
 */
public class HoneyCombExceptionTest {
  @Test
  public void exceptionTest() throws Exception {
    HoneyCombException exception = new HoneyCombException("this is an " +
        "exception");
  }
}
