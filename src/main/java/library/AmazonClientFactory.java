package library;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;

/**
 * Static factory class for various Amazon AWS clients (e.g. AutoScaling, EC2, ELB) for calling AWS API.
 * 
 * The AWS credentials will be automatically obtained using DefaultAWSCredentialsProviderChain class:
 * http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
 * 
 * @author anbinhtran
 *
 */
public class AmazonClientFactory {

	private static AmazonAutoScaling autoScalingClient = Region.getRegion(Regions.AP_SOUTHEAST_2).
			createClient(AmazonAutoScalingClient.class, null, null);
	
	/**
	 * Static method to obtain an AutoScaling client for calling AWS AutoScaling API
	 * @return an AmazonAutoScaling client object
	 */
	public static AmazonAutoScaling getAmazonAutoScalingClient() {
		return autoScalingClient;
	}
	
	private static AmazonEC2 ec2Client = Region.getRegion(Regions.AP_SOUTHEAST_2).
			createClient(AmazonEC2Client.class, null, null);
	
	/**
	 * Static method to obtain an EC2 client for calling AWS EC2 API
	 * @return an AmazonEC2 client object
	 */
	public static AmazonEC2 getAmazonEC2Client() {
		return ec2Client;
	}
	
	private static AmazonElasticLoadBalancing elasticLoadBalancingClient = Region.
			getRegion(Regions.AP_SOUTHEAST_2).
			createClient(AmazonElasticLoadBalancingClient.class, null, null);
	
	/**
	 * Static method to obtain an Elastic Load Balancing client for calling AWS ELB API
	 * @return an AmazonElasticLoadBalancing client object
	 */
	public static AmazonElasticLoadBalancing getAmazonElasticLoadBalancingClient() {
		return elasticLoadBalancingClient;
	}
	
}
