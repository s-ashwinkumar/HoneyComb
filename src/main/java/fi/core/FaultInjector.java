package fi.core;

import io.vertx.core.MultiMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    public static String url="jdbc:mysql://ec2-184-72-206-196.compute-1.amazonaws.com:3306/honeycomb?useUnicode=true&characterEncoding=utf-8";

    /**
     * The faultId indicate a fault in the fault list
     */
    final private String faultId;

    /**
     * The multimap contains all the arguments is transfer
     */
    final private MultiMap map;

    public FaultInjector(String faultId1, MultiMap map1) {

        this.faultId = faultId1;
        this.map = map1;
    }

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
                    System.out.println(reason);
                    return false;
                }
            }
        }
        return true;
    }
}
