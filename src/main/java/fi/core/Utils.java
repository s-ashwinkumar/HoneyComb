package fi.core;

/**
 * Created by ashwin on 6/17/16.
 */
public class Utils {

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
}
