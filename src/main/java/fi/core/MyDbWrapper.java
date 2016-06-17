package fi.core;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by ashwin on 6/15/16.
 */
public interface MyDbWrapper {

  /**
   * get con method.
   * @return connection object
   */
  public Connection getConn();

  /**
   * get statement method.
   * @return statement object
   */
  public Statement getStmt();
}
