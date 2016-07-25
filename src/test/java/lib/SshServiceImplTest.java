package lib;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
/**
 * Created by wilsoncao on 7/13/16.
 */
public class SshServiceImplTest {
  public SshServiceImpl obj;

  @Before
  public void setUp() throws Exception {
    obj = new SshServiceImpl("testingthiswith1234");
  }

  @Test
  public void testConstructors() throws Exception {
    obj = new SshServiceImpl("testingthiswith1234");
    assertEquals("testingthiswith1234",obj.getFaultInstanceId());
  }

  @Test
  public void executeSshCommands() throws Exception {

  }

}
