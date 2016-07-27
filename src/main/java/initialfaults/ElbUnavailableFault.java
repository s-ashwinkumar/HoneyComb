package initialfaults;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model
    .DeleteLoadBalancerRequest;
import fault.HoneyCombException;
import lib.AmazonClientFactory;
import lib.ElbService;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wilsoncao on 6/16/16.
 */
public class ElbUnavailableFault extends AbstractFault {

  //  static final Logger logger = LogManager.getLogger(ElbUnavailableFault
  // .class.getName());

  private String elbName;
  private ElbService elbService;
  private AmazonElasticLoadBalancing elb;
  private static Loggi logger;

  /**
   * Constructor for the class.
   *
   * @param params A HashMap as a parameter.
   */
  public ElbUnavailableFault(HashMap<String, String> params) throws
      IOException {
    super(params);
    this.elbName = params.get("elbName");
    logger = new Loggi(faultInstanceId, ElbUnavailableFault.class.getName());

  }

  /**
   * A method that implements the interface
   *
   * @throws AmazonServiceException thrown by AWS API.
   * @throws AmazonClientException  thrown by AWS API.
   * @throws HoneyCombException     thrown by AWS API.
   */
  @Override
  public void start() throws Exception {

    logger.start();
    // Get the Service
    if (elbService == null) {
      elbService = ServiceFactory.getElbService(faultInstanceId);
    }

    if (elb == null) {
      elb = AmazonClientFactory.getAmazonElasticLoadBalancingClient();
    }

    if (this.isTerminated())
      return;
    // Check for ELB existence
    if (elbService.describeLoadBalancer(elbName) == null) {
      throw new HoneyCombException("No Load Balancer with name = "
          + elbName + " can be found");
    }

    if (this.isTerminated())
      return;
    DeleteLoadBalancerRequest req = new DeleteLoadBalancerRequest()
        .withLoadBalancerName(elbName);

    if (this.isTerminated())
      return;
    elb.deleteLoadBalancer(req);
    // Log the fault injected
    logger.log("Fault injected: successfully delete Load Balancer with name =" +
        " " + elbName);
    // Tag the Load Balancer with: <"elb_experiment_status", "unavailable">
    // to mark it as Unavailable
    // elbService.tagElb(elbName, "elb_experiment_status", "unavailable");
    logger.finish();

  }

  /**
   * ELB Service setter for testing purpose.
   *
   * @param elbService An ELBService Object
   */
  public void elbServiceSetter(ElbService elbService) {
    this.elbService = elbService;
  }

  public void elbSetter(AmazonElasticLoadBalancing elb) {
    this.elb = elb;
  }


}
