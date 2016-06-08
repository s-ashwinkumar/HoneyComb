package library;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.model.InstanceState;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.amazonaws.services.elasticloadbalancing.model.Tag;

/**
 * ELBService interface. 
 * Provide wrapper services for various operations on AWS ELB API.
 * 
 * @author anbinhtran
 *
 */
public interface ELBService {

	/**
	 * Describe an Elastic Load Balancer (ELB) from the provided Name.
	 * 
	 * @param elbName the provided ELB Name
	 * @return a LoadBalancerDescription object for the ELB, or NULL if the ELB cannot be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public LoadBalancerDescription describeLoadBalancer(String elbName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Describe the ELB Health status of all Instances in the Load Balancer.
	 * 
	 * @param elbName the provided ELB Name
	 * @return a list of InstanceState describing the ELB Health status of all Instances in the ELB, or NULL if the ELB cannot be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public List<InstanceState> describeInstanceHealthInLoadBalancer(String elbName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Update the ELB Health Check target.
	 * 
	 * @param elbName the ELB we want to update
	 * @param target the new Health Check target
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if invalid parameters provided
	 */
	public void updateELBHealthCheckTarget(String elbName, String target) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Add Tag to a Load Balancer.
	 * 
	 * @param elbName the Load Balancer name
	 * @param tagKey the Tag key
	 * @param tagValue the Tag value
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if either elbName or tagKey are invalid
	 */
	public void tagELB(String elbName, String tagKey, String tagValue) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Get the Tags associated with a Load Balancer.
	 * 
	 * @param elbName the Load Balancer name
	 * @return the list of Tags
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public List<Tag> describeELBTags(String elbName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Remove a particular Tag from a Load Balancer.
	 * 
	 * @param elbName the Load Balancer name
	 * @param tagKey the Tag key we want to remove
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if either elbName or tagKey are invalid
	 */
	public void removeTagFromElb(String elbName, String tagKey)
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
}
