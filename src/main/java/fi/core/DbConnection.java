package fi.core;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by ashwin on 6/15/16.
 */
public class DbConnection implements MyDbWrapper {
  /**
   * connection object.
   */
  private Connection conn;
  /**
   * statement object.
   */
  private Statement stmt;
  /**
   * input stream to get properties.
   */
  private InputStream inputStream;
  /**
   * constant path for credential file.
   */
  private static final String PROPERTY_FILE = "MySQL.properties";

  /**
   * constructor.
   *
   * @throws Exception IOexception
   */
  public DbConnection() throws Exception {
    setConn();
    setStmt(getConn().createStatement());
  }

  /**
   * getter for statement object.
   *
   * @return statement objct
   */
  public Statement getStmt() {
    return stmt;
  }

  /**
   * setter for statement object.
   *
   * @param stmt Statement object
   */
  public void setStmt(Statement stmt) {
    this.stmt = stmt;
  }

  /**
   * getter for connection object.
   *
   * @return connection object
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * setter for connection object.
   *
   * @throws Exception Db connection exceptions
   */
  public void setConn() throws Exception {
    conn = null;

    Properties prop = new Properties();
    inputStream = getClass().getClassLoader()
        .getResourceAsStream(PROPERTY_FILE);

    if (inputStream != null) {
      prop.load(inputStream);
    } else {
      throw new FileNotFoundException("property file '" + PROPERTY_FILE + "' "
          + "not found in the classpath");
    }
    Class.forName(prop.getProperty("driver"));
    conn = DriverManager.getConnection(prop.getProperty("url"), prop
        .getProperty("username"), prop.getProperty("password"));


  }

}
