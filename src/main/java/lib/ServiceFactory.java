package lib;

import java.io.IOException;

/**
 * Static factory class for the various AWS API wrapper services.
 */
public class ServiceFactory {

  private static AsgService AsgService;
  private static Ec2Service Ec2Service;
  private static ElbService ElbService;
  private static SshService SshService;

  /**
   * Static method for obtaining wrapper service for ASG.
   * @param faultInstanceId  a String for fault instance ID.
   * @return an AsgService object.
   * @throws IOException
   */
  public static AsgService getAsgService(String faultInstanceId) throws
      IOException {
    AsgService = new AsgServiceImpl(faultInstanceId);
    return AsgService;
  }

  /**
   * Static method for obtaining wrapper service for EC2.
   * @param faultInstanceId  a String for fault instance ID.
   * @return an Ec2Service object.
   * @throws IOException
   */
  public static Ec2Service getEc2Service(String faultInstanceId) throws
      IOException {
    Ec2Service = new Ec2ServiceImpl(faultInstanceId);
    return Ec2Service;
  }

  /**
   * Static method for obtaining wrapper service for ELB.
   * @param faultInstanceId  a String for fault instance ID.
   * @return an ElbService object.
   * @throws IOException
   */
  public static ElbService getElbService(String faultInstanceId) throws
      IOException {
    ElbService = new ElbServiceImpl(faultInstanceId);
    return ElbService;
  }


  /**
   * Static method for obtaining SSH service.
   * @param faultInstanceId  a String for fault instance ID.
   * @return an SshService object.
   * @throws IOException
   */
  public static SshService getSshService(String faultInstanceId) throws
      IOException {
    SshService = new SshServiceImpl(faultInstanceId);
    return SshService;
  }

  /**
   * Non-Static method of obtaining ec2 service for testing purpose.
   *
   * @return an Ec2Service object.
   */
  public Ec2Service getEc2ServiceTest() {

    return Ec2Service;
  }

  /**
   * Non-Static method of obtaining ELB service for testing purpose.
   *
   * @return an ElbService object.
   */
  public ElbService getElbServiceTest() {

    return ElbService;
  }

  /**
   * Non-Static method of obtaining ASG service for testing purpose.
   *
   * @return an AsgService object.
   */

  public AsgService getAsgServiceTest() {
    return AsgService;
  }


  /**
   * Non-Static method of obtaining SSH service for testing purpose.
   *
   * @return an SshService object
   */
  public SshService getSshServiceTest() {
    return SshService;
  }

}
