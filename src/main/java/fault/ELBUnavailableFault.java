package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import lib.ELBService;
import lib.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by wilsoncao on 6/16/16.
 */
public class ELBUnavailableFault extends AbstractFault {

    static final Logger logger = LogManager.getLogger(ELBUnavailableFault.class.getName());

    private String elbName;
    private String asgName;
    private ELBService elbService;

    public ELBUnavailableFault(String asgName, String elbName, HashMap<String,String> params) {
        super(params);
        this.asgName = asgName;
        this.elbName = elbName;
    }

    @Override
    public void start() throws AmazonServiceException, AmazonClientException,
            HoneyCombException {

        // Get the Service
        if(elbService == null)
            elbService = ServiceFactory.getELBService();

        // Check for ELB existence
        if (elbService.describeLoadBalancer(elbName) == null) {
            throw new HoneyCombException("No Load Balancer with name = "
                    + elbName + " can be found");
        }

        // Log the fault injected
        logger.info("Fault injected: tag as unavailable Load Balancer with name = " + elbName);

        // Tag the Load Balancer with: <"elb_experiment_status", "unavailable">
        // to mark it as Unavailable
        elbService.tagELB(elbName, "elb_experiment_status", "unavailable");

    }

    /**
     * ELB Service setter for testing purpose
     * @param elbService
     */
    public void elbServiceSetter(ELBService elbService){
        this.elbService = elbService;
    }


}
