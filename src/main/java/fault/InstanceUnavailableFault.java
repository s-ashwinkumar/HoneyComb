package fault;

import lib.ASGService;
import lib.EC2Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by wilsoncao on 6/16/16.
 */
public class InstanceUnavailableFault extends AbstractFault {
    private EC2Service ec2Service;
    private String instanceId;
    static final Logger logger = LogManager.getLogger(InstanceUnavailableFault.class.getName());
    public InstanceUnavailableFault(String instanceId, HashMap<String,String> params){
        super(params);
        this.instanceId = instanceId;
    }
    @Override
    public void start(){
        logger.info("Terminating instance with id = "
                + instanceId + " ...");

        // Terminate the instance
        ec2Service.terminateInstance(instanceId);

        // Delay for 5 minutes (ASG EC2 Health Check time) for ASG to spawn new faulty instance
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * EC2 Service Setter for testing purpose
     * @param ec2Service
     */
    public void ec2ServiceSetter(EC2Service ec2Service) {
        this.ec2Service = ec2Service;
    }


}
