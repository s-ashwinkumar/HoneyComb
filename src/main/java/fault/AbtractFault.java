package fault;

import java.util.HashMap;

/**
 * Created by wilsoncao on 6/10/16.
 */
public abstract class AbtractFault implements Fault {
    private String faultID;
    private String faultName;
    private HashMap<String,String> params;

    public AbtractFault(HashMap<String,String> params){
        this.params = params;
    }

    /**
     * @return the faultID
     */

    public String getFaultID() {
        return faultID;
    }

    /**
     * @return the faultName
     */
    public String getFaultName() {
        return faultName;
    }

    /**
     * @param faultName the faultName to set
     */
    public void setfaultName(String faultName) {
        this.faultName = faultName;
    }

    /**
     * @param faultID the faultID to set
     */
    public void setFaultID(String faultID){
        this.faultID = faultID;
    }
}