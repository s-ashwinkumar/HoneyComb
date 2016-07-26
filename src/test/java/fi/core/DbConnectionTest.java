package fi.core;

import logmodifier.LogChanger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Created by ashwin on 6/19/16.
 * <p>
 * Please make sure there is a DB named faultinjectortest in your DB.
 * Also verify the credentials in the test properties file.
 */
public class DbConnectionTest {
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
  public void getFileName() throws Exception {
    assertEquals("MySQL.properties", DbConnection.getFileName());
  }

  @Test
  public void getAndSetStmt() throws Exception {
    DbConnection obj = new DbConnection();
    obj.setConn("MySQLTest.properties");
    Statement testStmt = obj.getConn().createStatement();
    obj.setStmt(testStmt);
    assertEquals(testStmt, obj.getStmt());
  }

  @Test
  public void getAndSetConn() throws Exception {
    DbConnection obj = new DbConnection();
    obj.setConn("MySQLTest.properties");
    assertNotNull(obj.getConn());
    //exception case
    try {
      obj = new DbConnection();
      obj.setConn("dummyfile");
    } catch (Exception ex) {
      assertNull(obj.getConn());
      assertEquals("property file 'dummyfile' not found in the classpath", ex
          .getMessage());
    }

  }


}