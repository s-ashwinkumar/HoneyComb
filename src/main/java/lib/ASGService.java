package lib;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.autoscaling.model.Activity;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;

/**
 * ASGService interface. 
 * Provide wrapper services for various operations on AWS AutoScaling API.
 * 
 * @author anbinhtran
 *
 */
public interface ASGService {

	/**
	 * Get the AutoScalingGroup object representing the ASG with the provided Name.
	 * 
	 * @param asgName the name of the ASG
	 * @return the AutoScalingGroup object with the provided Name, or NULL if no ASG with that name can be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public AutoScalingGroup getAutoScalingGroup(String asgName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Get the LaunchConfiguration object associated with the provided ASG.
	 * 
	 * @param asgName the name of the ASG
	 * @return the LaunchConfiguration object associated with the ASG, or NULL if either the ASG or the LaunchConfiguration cannot be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public LaunchConfiguration getLaunchConfigurationForAutoScalingGroup(String asgName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Get the LaunchConfiguration object from a given LC Name.
	 * 
	 * @param lcName the name of the LaunchConfiguration
	 * @return the LaunchConfiguration object with the provided Name, or NULL if a LC with that name cannot be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public LaunchConfiguration getLaunchConfigurationByName(String lcName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Get the list of Scaling Activities (both in progress and completed) of the provided ASG.
	 * The Scaling Activities that are in progress will appear first on the list.
	 * 
	 * @param asgName the name of the ASG
	 * @return the list of Scaling Activities of the ASG, or NULL if the ASG cannot be found
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public List<Activity> getScalingActivitiesForAutoScalingGroup(String asgName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Get the "newest" EC2 Instance in the provided Auto Scaling Group
	 * @param asgName the ASG name
	 * @return the EC2 Instance object of the "newest" instance launched, or NULL if either ASG cannot be found or ASG contains no instance
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public com.amazonaws.services.ec2.model.Instance getNewestEC2InstanceInASG(String asgName) 
			throws AmazonServiceException, AmazonClientException;
	
	/**
	 * Update the ASG to use the new Launch Configuration.
	 * 
	 * @param asgName the ASG name
	 * @param lcName the new LC name
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 */
	public void updateLaunchConfigurationInAutoScalingGroup(String asgName, String lcName) 
			throws AmazonServiceException, AmazonClientException;
	
	
	/* The following methods will mostly be used by test cases */
	
	/**
	 * Update the DesiredCapacity, MinSize and MaxSize of a ASG.
	 * 
	 * @param asgName the ASG Name
	 * @param capacity the new ASG Capacity
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if invalid parameters provided
	 */
	public void updateCapacityOfASG(String asgName, int capacity) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Create a LaunchConfiguration for ASG, using specified parameters.
	 * 
	 * @param params a set of parameters for creating the LC
	 * @return the newly created Launch Configuration name
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if null parameters provided
	 */
	public String createLaunchConfiguration(CreateLaunchConfigurationRequest params) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Create an Auto Scaling Group, using specified parameters.
	 * 
	 * @param params a set of parameters for creating the ASG
	 * @return the newly created ASG name
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if null parameters provided
	 */
	public String createAutoScalingGroup(CreateAutoScalingGroupRequest params) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Delete a LaunchConfiguration with the specified Name.
	 * 
	 * @param lcName the name of the LC to be deleted
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if invalid LC name provided
	 */
	public void deleteLaunchConfiguration(String lcName)
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
	/**
	 * Delete an Auto Scaling Group with the specified Name.
	 * 
	 * @param asgName the name of the ASG to be deleted
	 * @throws AmazonServiceException if error occurred while calling AWS API
	 * @throws AmazonClientException if error occurred while calling AWS API
	 * @throws IllegalArgumentException if invalid ASG name provided
	 */
	public void deleteAutoScalingGroup(String asgName) 
			throws AmazonServiceException, AmazonClientException, IllegalArgumentException;
	
}
