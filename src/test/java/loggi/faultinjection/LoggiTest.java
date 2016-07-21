package loggi.faultinjection;

import fi.core.DbConnection;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Created by joe on 16/6/1.
 */
public class LoggiTest {

    @Before
    public void setUp() throws Exception {

        // for the test log purpose
        File oldName = new File("src/main/resources/log_test");

        if (!oldName.exists())
            oldName.createNewFile();

        Properties props = new Properties();
        try {
            InputStream configStream = getClass().getClassLoader().getResourceAsStream("log4j.properties");
            props.load(configStream);
            configStream.close();
        } catch (IOException e) {
            System.out.println("Errornot laod configuration file ");
        }

        props.setProperty("log4j.appender.file.File","src/main/resources/log_test");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);


    }

    @After
    public void tearDown ()throws Exception {
        Properties props = new Properties();
        try {
            InputStream configStream = getClass().getClassLoader().getResourceAsStream("log4j.properties");
            props.load(configStream);
            configStream.close();
        } catch (IOException e) {
            System.out.println("Errornot laod configuration file ");
        }

        props.setProperty("log4j.appender.file.File","src/main/resources/log");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }

    /**
     * tdd start
     * @throws Exception
     */

    @Test
    public void testStart() throws Exception {
        File file = new File("src/main/resources/log_test");

        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();


                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                loggi.start();

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();



                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("INFO    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("fault injection start!");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testLog() throws Exception {
        File file = new File("src/main/resources/log_test");
        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();

                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                loggi.log("Hello World!");

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();


                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("INFO    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("Hello World!");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFinish() throws Exception {
        File file = new File("src/main/resources/log_test");

        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();


                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                loggi.finish();

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();



                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("INFO    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("fault injection finish!");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testError() throws Exception {
        File file = new File("src/main/resources/log_test");
        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();

                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                loggi.error("Bad Aws Request");

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();


                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("ERROR    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("Bad Aws Request");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testErrorException() throws Exception {
        File file = new File("src/main/resources/log_test");
        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();

                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                //make an exception
                File file2 = new File("src/main/resources/notExisted");
                try {
                    String  thisLine = null;
                    StringBuilder sb = new StringBuilder("");
                    FileReader fileReader = new FileReader(file2);
                    BufferedReader br = new BufferedReader(fileReader);
                    while ((thisLine = br.readLine()) != null) {
                        sb.append(thisLine);
                    }
                    br.close();
                } catch (Exception e) {
                    loggi.error(e);
                }

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();



                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("ERROR    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("java.io.FileNotFoundException: src/main/resources/notExisted (No such file or directory)");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * unit test start
     * @throws Exception
     */
    //improve the coverage, test When log file is not existed
    @Test
    public void testFile() throws Exception {
        File file = new File("src/main/resources/log_test");

        if (file.exists()) {
            file.deleteOnExit();
        }

        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);

        try{
            //firstly, we make sure we have the log file
            Assert.assertTrue(file.exists());

            if (!file.exists()) {
                System.out.println(" file not existed yet");
            } else {
                FileWriter fw =  new FileWriter(file);
                fw.write("");
                fw.close();


                //make sure the current time can be very close
                java.util.Date date= new java.util.Date();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

                loggi.start();

                String  thisLine = null;
                StringBuilder sb = new StringBuilder("");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();



                //secondly I will compare the content inside the log with the expected content after loggi.start()
                StringBuilder answer = new StringBuilder("");
                answer.append(sdf.format(date));
                answer.append("    ");
                answer.append("INFO    ");
                answer.append("[FaultInstanceId = 123456789112300001]    ");
                answer.append("[FaultId = 00001]    ");
                answer.append("[FaultName = LoggiTest]    ");
                answer.append("fault injection start!");

                //in order to compare I need to delete million second
                String s1 = answer.toString();
                String s2 = sb.toString();

                String a1 = s1.substring(0,19) + s1.substring(23);
                String a2 = s2.substring(0,19) + s2.substring(23);
                Assert.assertEquals(a1, a2);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
