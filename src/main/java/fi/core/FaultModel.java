package fi.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ashwin on 6/15/16.
 */
public class FaultModel {
  /**
   * variable for fault id.
   */
  private Integer faultId;
  /**
   * variable for fault name.
   */
  private String name;
  /**
   * variable for description.
   */
  private String description;
  /**
   * variable for location.
   */
  private String location;
  /**
   * variable for arguments - ( ';' separated).
   */
  private String arguments;
  /**
   * variable for active flag.
   */
  private Boolean active;

  /**
   * constructor for fault class.
   *
   * @param faultId   faultid
   * @param name      name
   * @param arguments semicolon separated arguments
   * @param active    active flag
   */
  public FaultModel(int faultId, String name, String arguments, boolean
      active, String location, String description) {
    this.faultId = faultId;
    this.name = name;
    this.arguments = arguments;
    this.active = active;
    this.location = location;
    this.description = description;
  }

  /**
   * getter for gault id.
   *
   * @return faultid
   */
  public Integer getFaultId() {
    return faultId;
  }

  /**
   * setter for fault id.
   *
   * @param faultId fault id
   */
  public void setFaultId(Integer faultId) {
    this.faultId = faultId;
  }

  /**
   * getter for name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * setter for fault name.
   *
   * @param name name of the fault
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getter for description.
   *
   * @return description string
   */
  public String getDescription() {
    return description;
  }

  /**
   * setter for description.
   *
   * @param description description of the fault
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * getter for location.
   *
   * @return location
   */
  public String getLocation() {
    return location;
  }

  /**
   * setter for location.
   *
   * @param location location of the fault
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * getter for arguments.
   *
   * @return arguments
   */
  public String getArguments() {
    return arguments;
  }

  /**
   * setter for arguments.
   *
   * @param arguments semicolon separated arguments for the fault
   */
  public void setArguments(String arguments) {
    this.arguments = arguments;
  }

  /**
   * getter for active flag.
   *
   * @return active flag
   */
  public Boolean getActive() {
    return active;
  }

  /**
   * setter for active flag.
   *
   * @param active active flag for the fault
   */
  public void setActive(Boolean active) {
    this.active = active;
  }

  /**
   * method that returns a list of all the faults.
   *
   * @param dbCon dbconnection parameter
   * @return list of faults
   * @throws Exception exceptions during db connection
   */
  public static List<FaultModel> getFaults(DbConnection dbCon) throws
      Exception {
    List<FaultModel> faultList = new ArrayList<>();
    String query = "select * from fault";
    ResultSet rs = dbCon.getStmt().executeQuery(query);
    while (rs.next()) {
      FaultModel tempObj = new FaultModel(rs.getInt("faultID"),
          rs.getString("name"), rs.getString("arguments"),
          rs.getBoolean("active"), rs.getString("location"),
          rs.getString("description"));
      faultList.add(tempObj);
    }
    return faultList;
  }

  /**
   * Method to mark a fault unusable.
   *
   * @param id id of the fault to be deactivated
   * @return integer number of records updated
   */
  public static Integer updateFault(String id, DbConnection dbCon, Boolean
      status, String description, String arguments)
      throws
      Exception {
    String query = "update fault set active="+status;
    if(description != null)
      query += ", description='"+description+"'";
    if(arguments != null)
      query += ", arguments='"+arguments+"'";
    query += " where faultID=" + id;
    return dbCon.getStmt().executeUpdate(query);
  }

  /**
   * method for returning a fault.
   *
   * @param dbCon   connection object
   * @param faultId fault id
   * @return fault model object
   * @throws Exception io exceptions
   */
  public static FaultModel getFault(DbConnection dbCon, String faultId) throws
      Exception {
    String sql = "select * from fault where faultID = '" + faultId + "'";
    ResultSet rs = dbCon.getStmt().executeQuery(sql);
    FaultModel tempObj = null;
    while (rs.next()) {
      tempObj = new FaultModel(rs.getInt("faultID"), rs.getString("name"),
          rs.getString("arguments"), rs.getBoolean("active"),
          rs.getString("location"), rs.getString("description"));
    }
    rs.close();
    return tempObj;
  }

  /**
   * method to insert a record in db while uploading fault.
   *
   * @param dbCon Dbconnection object
   * @param name  Name of the fault
   * @param desc  Description of the fault
   * @param args  arguments for the fault
   * @return -1 if failure else should ideally return 1 if inserted.
   */
  public static void insertFault(DbConnection dbCon, String name, String
      desc, String args) throws Exception {
    String query = "insert into fault (active,name,description,location," +
        "arguments) values (1,'" + name + "', '" + desc + "','faults/" +
        name + "', " +
        "'" + args + "')";
    dbCon.getStmt().executeUpdate(query);

  }

  public static FaultModel getFaultByName(DbConnection dbCon, String name) throws
      Exception {
    String sql = "select * from fault where name = '" + name + "'";
    ResultSet rs = dbCon.getStmt().executeQuery(sql);
    FaultModel tempObj = null;
    while (rs.next()) {
      tempObj = new FaultModel(rs.getInt("faultID"), rs.getString("name"),
          rs.getString("arguments"), rs.getBoolean("active"), rs.getString
          ("location"), rs.getString("description"));
    }
    rs.close();
    return tempObj;

  }

}
