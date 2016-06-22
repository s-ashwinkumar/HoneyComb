package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import lib.ElbService;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wilsoncao on 6/16/16.
 */
public class ElbUnavailableFault extends AbstractFault {

  //  static final Logger logger = LogManager.getLogger(ElbUnavailableFault.class.getName());

  private String elbName;
  private String asgName;
  private String faultInstanceId;
  private ElbService elbService;
  private static Loggi logger;

  /**
   * Constructor for the class.
   * @param params A HashMap as a parameter.
   */
  public ElbUnavailableFault(HashMap<String, String> params) throws IOException {
    super(params);
    this.asgName = params.get("asgName");
    this.elbName = params.get("elbName");
    this.faultInstanceId = params.get("faultInstanceId");
    logger = new Loggi(faultInstanceId,ElbUnavailableFault.class.getName());
  }

  /**
   * A method that implements the interface
   * @throws AmazonServiceException thrown by AWS API.
   * @throws AmazonClientException thrown by AWS API.
   * @throws HoneyCombException thrown by AWS API.
   */
  @Override
  public void start() throws AmazonServiceException, AmazonClientException,
          HoneyCombException, IOException {

    // Get the Service
    if (elbService == null) {
      elbService = ServiceFactory.getElbService(faultInstanceId);
    }

    // Check for ELB existence
    if (elbService.describeLoadBalancer(elbName) == null) {
      throw new HoneyCombException("No Load Balancer with name = "
              + elbName + " can be found");
    }

    // Log the fault injected
    logger.log("Fault injected: tag as unavailable Load Balancer with name = " + elbName);

    // Tag the Load Balancer with: <"elb_experiment_status", "unavailable">
    // to mark it as Unavailable
    elbService.tagElb(elbName, "elb_experiment_status", "unavailable");

  }

  /**
   * ELB Service setter for testing purpose.
   *
   * @param elbService An ELBService Object
   */
  public void elbServiceSetter(ElbService elbService) {
    this.elbService = elbService;
  }


}
