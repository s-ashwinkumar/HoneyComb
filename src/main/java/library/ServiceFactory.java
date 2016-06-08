package library;

/**
 * Static factory class for the various AWS API wrapper services.
 * 
 * @author anbinhtran
 *
 */
public class ServiceFactory {

	private static ASGService asgService = new ASGServiceImpl();
	
	/**
	 * Static method for obtaining wrapper service for ASG
	 * @return an ASGService object
	 */
	public static ASGService getASGService() {
		return asgService;
	}
	
	private static EC2Service ec2Service = new EC2ServiceImpl();
	
	/**
	 * Static method for obtaining wrapper service for EC2
	 * @return an EC2Service object
	 */
	public static EC2Service getEC2Service() {
		return ec2Service;
	}
	
	private static ELBService elbService = new ELBServiceImpl();
	
	/**
	 * Static method for obtaining wrapper service for ELB
	 * @return an ELBService object
	 */
	public static ELBService getELBService() {
		return elbService;
	}
	
	private static SSHService sshService = new SSHServiceImpl();
	
	/**
	 * Static method for obtaining SSH service
	 * @return an SSHService object
	 */
	public static SSHService getSSHService() {
		return sshService;
	}
	
}
