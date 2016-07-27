package logmodifier;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ashwin on 7/25/16.
 */
public class LogChanger {

  public void setupLogForTest() throws IOException {
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

  }

  public void resetLogAfterTest() throws Exception {
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
  }
}
