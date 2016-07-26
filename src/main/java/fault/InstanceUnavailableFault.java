package fault;

import lib.Ec2Service;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wilsoncao on 6/16/16.
 */
public class InstanceUnavailableFault extends AbstractFault {
  private Ec2Service ec2Service;
  private String instanceId;
  private static Loggi logger;

  /**
   * Constructor of this class.
   *
   * @param params A HashMap object.
   * @throws IOException If we can't get the class name, throw this exception
   */
  public InstanceUnavailableFault(HashMap<String, String> params) throws
      IOException {
    super(params);
    this.instanceId = params.get("instanceId");
    logger = new Loggi(faultInstanceId, InstanceUnavailableFault.class
        .getName());
  }

  @Override
  public void start() throws Exception {

    logger.start();

    logger.log("Terminating instance with id = "
        + instanceId + " ...");
    if (ec2Service == null) {
      ec2Service = ServiceFactory.getEc2Service(faultInstanceId);
    }
    // Terminate the instance
    if (this.isTerminated())
      return;
    ec2Service.terminateInstance(instanceId);

    logger.finish();
  }

  /**
   * EC2 Service Setter for testing purpose.
   *
   * @param ec2Service An Ec2Service object.
   */
  public void ec2ServiceSetter(Ec2Service ec2Service) {
    this.ec2Service = ec2Service;
  }


}
