package fi.core;

/**
 * Created by ashwin on 6/17/16.
 */
public class Utils {

  /**
   * Constructs a new MyUtilities.
   * @throws InstantiationException
   */
  public Utils() throws InstantiationException
  {
    throw new InstantiationException("Instances of this type are forbidden.");
  }

  /**
   * A Utility method to check if the given parameters is a number.
   *
   * @param str number as string
   * @return boolean
   */
  public static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * Method that returns a db connection object
   * @returnDbconnection object
   * @throws Exception any kind
   */
  public static DbConnection returnDbconnection(String filename) throws
      Exception {
    DbConnection dbCon = new DbConnection();
    dbCon.setConn(filename);
    dbCon.setStmt(dbCon.getConn().createStatement());
    return dbCon;
  }
}
