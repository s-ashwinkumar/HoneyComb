package fi.core;

import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import logmodifier.LogChanger;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by joe on 16/6/13.
 */
public class FaultInjectorTest {

  /**
   * Test mysql.
   */
  private static final String testMysql = "MySQLTest.properties";


  /**
   * mysql
   */
  private static final String Mysql = "MySQL.properties";

  DbConnection obj;
  LogChanger log = new LogChanger();

  @Before
  public void setUp() throws Exception {
    // for the test log purpose
    File oldName = new File("src/main/resources/log_test");

    if (!oldName.exists())
      oldName.createNewFile();

    Properties props = new Properties();
    try {
      InputStream configStream = getClass().getClassLoader()
          .getResourceAsStream("log4j.properties");
      props.load(configStream);
      configStream.close();
    } catch (IOException e) {
      System.out.println("Errornot laod configuration file ");
    }

    props.setProperty("log4j.appender.file.File",
        "src/main/resources/log_test");
    LogManager.resetConfiguration();
    PropertyConfigurator.configure(props);


    obj = new DbConnection();
    obj.setConn("MySQLTest.properties");
    obj.setStmt(obj.getConn().createStatement());
    // create the fault table
    obj.getStmt().execute("CREATE TABLE fault\n" +
        "(\n" +
        "faultID int NOT NULL AUTO_INCREMENT,\n" +
        "name varchar(50) NOT NULL,\n" +
        "description TEXT,\n" +
        "location varchar(255) NOT NULL,\n" +
        "arguments varchar(500),\n" +
        "active BOOLEAN NOT NULL,\n" +
        "PRIMARY KEY (faultID)\n" +
        ");");
    // set auto increment
    obj.getStmt().execute("ALTER TABLE fault AUTO_INCREMENT = 10000;");
    // insert test data.
    obj.getStmt().execute("Insert into fault (name,description,location," +
        "arguments,active) values ('Test Fault','Description of test fault'," +
        "'/var/whatever','instancename;secondargument;thirdargument',true);");
    //insert ChangeAMIInLCFFault.jar
    obj.getStmt().execute("Insert into fault (name,description,location," +
        "arguments,active) values ('TestFault','test for fault injection'," +
        "'faults/TestFault.jar','nothing',true);");
    obj.getConn().close();
    log.setupLogForTest();
  }

  @After
  public void tearDown() throws Exception {
    // restore the log file
    Properties props = new Properties();
    try {
      InputStream configStream = getClass().getClassLoader()
          .getResourceAsStream("log4j.properties");
      props.load(configStream);
      configStream.close();
    } catch (IOException e) {
      System.out.println("Errornot laod configuration file ");
    }

    props.setProperty("log4j.appender.file.File", "src/main/resources/log");
    LogManager.resetConfiguration();
    PropertyConfigurator.configure(props);

    obj = new DbConnection();
    obj.setConn("MySQLTest.properties");
    obj.setStmt(obj.getConn().createStatement());

    obj.getStmt().execute("drop table fault;");
    obj.getConn().close();
    log.resetLogAfterTest();
  }

  @Test
  public void testValidateFault() throws Exception {
    /**
     * we assume token validation is passed.
     * This token validation will not handle it at all.
     */

    /**
     * We use faultId = 10000 to validate the correctness.
     * 10000 contains arguments such as instancename,
     * secondargument, thirdargument.
     */
    String faultId = "10000";
    /**
     * missing arguments (why cannot pass the validation)
     */
    StringBuilder reason = new StringBuilder();
    /**
     * vertex MultiMap.
     */
    MultiMap map = new CaseInsensitiveHeaders();

    /**
     * the first test, we don't give any arguments
     */
    FaultInjector object1 = new FaultInjector(faultId, map);
    assertFalse(object1.validate(reason, testMysql));
    assertNotEquals("", reason.toString());

    /**
     * the second test, we miss thirdargument
     */
    reason.delete(0, reason.length());
    map.add("instancename", "test2");
    map.add("secondargument", "test2");
    FaultInjector object2 = new FaultInjector(faultId, map);
    assertFalse(object2.validate(reason, testMysql));
    assertEquals("thirdargument", reason.toString());

    /**
     * the third test, we miss nothing.
     */
    reason.delete(0, reason.length());
    map.add("thirdargument", "test3");
    FaultInjector object3 = new FaultInjector(faultId, map);
    assertTrue(object3.validate(reason, testMysql));
    assertEquals("", reason.toString());


  }

  @Test
  public void testInjection() throws Exception {
    /**
     * we assume token validation is passed.
     * This validation will not handle it at all.
     *
     * We also assume the validation of the arguments are passed
     */

    /**
     * We use faultId = 10001 to inject.
     * 10001 contains arguments such as asgName;faultyAmiId.
     */
    String faultId = "10001";

    /**
     * missing arguments (why cannot pass the validation)
     */
    StringBuilder reason = new StringBuilder();

    /**
     * vertex MultiMap.
     */
    MultiMap map = new CaseInsensitiveHeaders();

    /**
     * make a fault and pass the arguments validation
     */
    map.add("nothing", "hehe");
    //map.add("faultyAmiId", "ami-fce3c696");
    FaultInjector object1 = new FaultInjector(faultId, map);

    assertTrue(object1.validate(reason, testMysql));

    String faultInstanceId = object1.inject(testMysql);

    /**
     * test pass, we get the faultInstanceId
     */
    assertNotNull(faultInstanceId);
    assertEquals(18, faultInstanceId.length());
    assertEquals(faultId, faultInstanceId.substring(13));

    /**
     * test the fault injection resutls, to see whether there exists end
     */
    boolean isExist = false;
    Thread.sleep(15000);
    File file = new File("src/main/resources/log_test");

    try {
      //firstly, we make sure we have the log file
      Assert.assertTrue(file.exists());

      if (!file.exists()) {
        System.out.println(" file not existed yet");
      } else {
        String thisLine = null;
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);

        while ((thisLine = br.readLine()) != null) {
          if (thisLine.indexOf("fault injection finish!") != -1 || thisLine
              .indexOf("ERROR") != -1) {
            if (thisLine.indexOf(faultInstanceId) != -1) {
              isExist = true;
              break;
            }
          }
        }
        br.close();
      }

      Assert.assertTrue(isExist);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  @Test
  public void testTerminateFault() throws Exception {
    /**
     * we assume token validation is passed.
     * This validation will not handle it at all.
     *
     * We also assume the validation of the arguments are passed
     */

    /**
     * We use faultId = 10001 to inject.
     * 10001 contains arguments such as asgName;faultyAmiId.
     */
    String faultId = "10001";

    /**
     * missing arguments (why cannot pass the validation)
     */
    StringBuilder reason = new StringBuilder();

    /**
     * vertex MultiMap.
     */
    MultiMap map = new CaseInsensitiveHeaders();

    /**
     * make a fault and pass the arguments validation
     */
    map.add("nothing", "hehe");
    //map.add("faultyAmiId", "ami-fce3c696");
    FaultInjector object1 = new FaultInjector(faultId, map);

    assertTrue(object1.validate(reason, testMysql));

    String faultInstanceId = object1.inject(testMysql);

    FaultInjector.terminateFault(faultInstanceId);

    /**
     * test pass, we get the faultInstanceId
     */
    assertNotNull(faultInstanceId);
    assertEquals(18, faultInstanceId.length());
    assertEquals(faultId, faultInstanceId.substring(13));

    /**
     * test the fault injection resutls, to see whether there exists end
     */
    boolean isExist = false;
    Thread.sleep(10000);
    File file = new File("src/main/resources/log_test");

    try {
      //firstly, we make sure we have the log file
      Assert.assertTrue(file.exists());

      if (!file.exists()) {
        System.out.println(" file not existed yet");
      } else {
        String thisLine = null;
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);

        while ((thisLine = br.readLine()) != null) {
          if (thisLine.indexOf("terminate") != -1) {
            if (thisLine.indexOf(faultInstanceId) != -1) {
              isExist = true;
              break;
            }
          }
        }
        br.close();
      }

      Assert.assertTrue(isExist);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
