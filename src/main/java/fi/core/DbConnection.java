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
public class DbConnection {
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
   * method to return filename.
   *
   * @return String filename
   */
  public static String getFileName() {
    return "MySQL.properties";
  }

  /**
   * getter for statement object.
   *
   * @return statement object
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
  public void setConn(String file) throws Exception {
    conn = null;

    Properties prop = new Properties();
    inputStream = getClass().getClassLoader()
        .getResourceAsStream(file);

    if (inputStream != null) {
      prop.load(inputStream);
    } else {
      throw new FileNotFoundException("property file '" + file + "' "
          + "not found in the classpath");
    }
    Class.forName(prop.getProperty("driver"));
    conn = DriverManager.getConnection(prop.getProperty("url"), prop
        .getProperty("username"), prop.getProperty("password"));


  }

}
