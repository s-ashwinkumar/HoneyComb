package fi.core;


import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by ashwin on 6/3/16.
 */
public class User {

  /**
   * constant path for credential file.
   */
  private static final String PASSWORD_FILE = "flatfiles/users";

  /**
   * string field for username.
   */
  private String username;

  /**
   * string field for password.
   */
  private String password;

  /**
   * String field for api token.
   */
  private String apiToken;

  /**
   * constructor.
   *
   * @param user  username
   * @param passw password
   */
  public User(final String user, final String passw) {
    this.username = user;
    this.password = passw;
  }

  /**
   * getter for api token.
   *
   * @return string api token
   */
  public String getApiToken() {
    return apiToken;
  }

  /**
   * getter for api token.
   *
   * @return string api token
   */
  public String getFileName() {
    return PASSWORD_FILE;
  }

  /**
   * getter for username.
   *
   * @return string password
   */
  public String getUsername() {
    return username;
  }

  /**
   * getter for password.
   *
   * @return string password
   */
  public String getPassword() {
    return password;
  }

  /**
   * This method checks if the user is valid and sets the api token and
   * returns true if the user is valid. Else it just returns false
   *
   * @return boolean true if valid username password, false if invalid
   */
  public boolean isValidUser() {
    BufferedReader br = null;
    String line;
    try {
      br = new BufferedReader(
          new FileReader(getFileName()));
      while ((line = br.readLine()) != null) {
        String[] fields = line.split("\t");
        if (getUsername().equals(fields[0])
            && getPassword().equals(fields[1])) {
          this.apiToken = fields[2];
          return true;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }

  /**
   * This method checks if the token given is the existing token.
   *
   * @return boolean true if valid token, false if invalid
   */
  public static boolean isValidUser(String token) {
    BufferedReader br = null;
    String line;
    try {
      br = new BufferedReader(
          new FileReader(PASSWORD_FILE));
      while ((line = br.readLine()) != null) {
        String[] fields = line.split("\t");
        if (token.equals(fields[2])) {
          return true;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }
}
