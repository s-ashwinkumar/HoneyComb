package lib;

import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class SshServiceImplTest {
  public SshServiceImpl obj;
  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    obj = new SshServiceImpl("testingthiswith1234");
    log.setupLogForTest();

  }

  @After
  public void tearDown() throws Exception {
    log.resetLogAfterTest();
  }

  @Test
  public void testConstructors() throws Exception {
    obj = new SshServiceImpl("testingthiswith1234");
    assertEquals("testingthiswith1234", obj.getFaultInstanceId());
  }

  @Test
  public void executeSshCommands() throws Exception {
//    TODO : Setup ssh server to test this
  }

  @Test
  public void executeSshCommandReturnOutput() throws Exception {
//    TODO : Setup ssh server to test this
  }

  @Test
  public void getSshOutput() throws Exception {
    String result;
    Method privateMethod = SshServiceImpl.class.getDeclaredMethod
        ("getSshOutput", InputStream.class);
    privateMethod.setAccessible(true);

    InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
    result = (String) privateMethod.invoke(obj, inputStream);
    assertEquals("test data", result);

    inputStream = new ByteArrayInputStream("test data \n test2".getBytes());
    result = (String) privateMethod.invoke(obj, inputStream);
    assertEquals("test data \n test2", result);

    inputStream = new ByteArrayInputStream("     \nspace line"
        .getBytes());
    result = (String) privateMethod.invoke(obj, inputStream);
    assertEquals("space line", result);
  }

}
