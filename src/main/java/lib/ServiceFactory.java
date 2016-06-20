package lib;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.powermock.core.classloader.annotations.PrepareForTest;

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

	/**
	 * Non-Static method of obtaining ec2 service for testing purpose
	 * @return an EC2Service object
     */
	public EC2Service getEC2ServiceTest() {
		return ec2Service;
	}

	/**
	 * Non-Static method of obtaining ELB service for testing purpose
	 * @return an ELBService object
     */
	public ELBService getElbServiceTest() {
		return elbService;
	}

	/**
	 * Non-Static method of obtaining ASG service for testing purpose
	 * @return an ASGService object
     */

	public ASGService getAsgServiceTest(){
		return asgService;
	}


	/**
	 * Non-Static method of obtaining SSH service for testing purpose
	 * @return
     */
	public SSHService getSSHServiceTest(){
		return sshService;
	}

}
