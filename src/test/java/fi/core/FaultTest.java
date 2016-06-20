package fi.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ashwin on 6/19/16.
 */
public class FaultTest {
  DbConnection obj;
  @Before
  public void setUp() throws Exception {
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
  }

  @After
  public void tearDown ()throws Exception {
    obj.getStmt().execute("drop table fault;");
    obj.getConn().close();
  }

  @Test
  public void getterSetterConstructorTest(){
    Fault testObj = new Fault(123,"name","arguments",true);
    assertSame(123,testObj.getFaultId());
    assertEquals("name",testObj.getName());
    assertEquals("arguments",testObj.getArguments());
    assertEquals(true,testObj.getActive());

    testObj.setActive(false);
    testObj.setFaultId(1234);
    testObj.setArguments("testargs");
    testObj.setDescription("description");
    testObj.setLocation("location");
    testObj.setName("new name");

    assertTrue(testObj.getFaultId() == 1234);
    assertEquals(false,testObj.getActive());
    assertEquals("testargs",testObj.getArguments());
    assertEquals("description",testObj.getDescription());
    assertEquals("location",testObj.getLocation());
    assertEquals("new name",testObj.getName());
  }

  //TDD tests
  @Test
  public void getFaultsTest() throws Exception {
    List<Fault> res = Fault.getFaults(obj);
    assertEquals(1,res.size());
    assertEquals("Test Fault",((Fault)res.toArray()[0]).getName());
    obj.getStmt().execute("Truncate table fault");
    res = Fault.getFaults(obj);
    assertEquals(0,res.size());
    // reset table with a fault
    obj.getStmt().execute("Insert into fault (name,description,location," +
        "arguments,active) values ('Test Fault','Description of test fault'," +
        "'/var/whatever','instancename;secondargument;thirdargument',true);");

  }

  @Test
  public void removeFaultTest() throws Exception {
    List<Fault> res = Fault.getFaults(obj);
    int id = ((Fault)res.toArray()[0]).getFaultId();
    int result = Fault.removeFault("99999",obj);
    assertEquals(0,result);
    result = Fault.removeFault(id+"",obj);
    assertEquals(1,result);
  }

}