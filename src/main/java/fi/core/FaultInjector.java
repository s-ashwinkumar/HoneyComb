package fi.core;

import fault.AbstractFault;
import fault.Fault;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import loggi.faultinjection.Loggi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/6/13.
 */
public class FaultInjector {
    /**
     * This class is to handle fault injection and termination
     */

    /**
     * Below is for the database connection
     */
    public static String userr="root";
    public static String pass="123";
    public static String url= "jdbc:mysql://ec2-184-72-206-196.compute-1.amazonaws.com:3306/honeycomb?useUnicode=true&characterEncoding=utf-8";

    /**
     * The faultId indicate a fault in the fault list
     */
    final private String faultId;

    /**
     * The multimap contains all the arguments is transfer
     */
    final private MultiMap map;

    /**
     * constructor for the Faultinjector
     * @param faultId1
     * @param map1
     */
    public FaultInjector(String faultId1, MultiMap map1) {

        this.faultId = faultId1;
        this.map = map1;
    }

    /**
     * This method is used to check the arguments of the fault beforehand
     * @param reason
     * @return true false
     */
    public boolean validate(StringBuilder reason) {
        String arguments = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, userr, pass);
            Statement stmt = con.createStatement();


            String sql="select arguments from fault where faultID = '" + faultId +"' limit 0,1";

            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()) {
                arguments = rs.getString("arguments");
                break;
            }
            rs.close();
            stmt.close();
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        /**
         * the default value will be null in db.
         */
        if (arguments != null) {

            for (String retval : arguments.split(";")) {
                if (map.get(retval) == null) {
                    reason.append(retval);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This is the inject function
     * @return faultInstanceId
     */
    public String inject() throws IOException{
        String faultInsanceId = null;

        String location = null;
        String faultName = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(url, userr, pass);
            Statement stmt = con.createStatement();


            String sql="select location, name from fault where faultID = '" + faultId +"' limit 0,1";

            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next()) {
                location = rs.getString("location");
                faultName = rs.getString("name");
                break;
            }
            rs.close();
            stmt.close();
            con.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        /**
         * We should make sure these values are existed in the upload function.
         */
        final String loc = location;
        final String nam = faultName;

        /**
         * generate the faultInstanceId 13 + faultId
         */
        long unixTime = System.currentTimeMillis();
        faultInsanceId = String.valueOf(unixTime) + faultId;

        /**
         * load fault script and inject in a thread.
         */
        Loggi loggi = new Loggi(faultInsanceId, faultName);

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {

                    /**
                     * put the arguments into hashMap.
                     */
                    HashMap<String, String> params = new HashMap<>();
                    for(Map.Entry<String,String> item : map.entries()) {
                        params.put(item.getKey(), item.getValue());
                    }

                    File authorizedJarFile = new File(loc);
                    ClassLoader authorizedLoader = URLClassLoader.
                            newInstance(new URL[]{authorizedJarFile.toURI().toURL()});
                    Fault authorizedPlugin = (Fault) authorizedLoader.loadClass(nam).
                            getDeclaredConstructor(HashMap.class)
                            .newInstance(params);
                    authorizedPlugin.start();
                } catch (Exception e) {
                    //e.printStackTrace();
                    loggi.error(e);
                }
            }
        });

        t.setName(faultInsanceId);
        t.start();


        return faultInsanceId;
    }

}
