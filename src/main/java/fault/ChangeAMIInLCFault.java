package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
import lib.ASGService;
import lib.EC2Service;
import lib.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by wilsoncao on 6/16/16.
 */
public class ChangeAMIInLCFault extends AbstractFault {

    static final Logger logger = LogManager.getLogger(ChangeAMIInLCFault.class.getName());

    private String faultyAmiId;
    private String asgName;
    private EC2Service ec2Service;
    private ASGService asgService;
    private AutoScalingGroup asg;

    public ChangeAMIInLCFault(String asgName, String faultyAmiId, HashMap<String,String> params) {
        super(params);
        this.asgName = asgName;
        this.faultyAmiId = faultyAmiId;
    }

    @Override
    public void start() throws AmazonServiceException, AmazonClientException,
            HoneyCombException{

        // Get the services
        if (ec2Service == null)
            ec2Service = ServiceFactory.getEC2Service();

        if (asgService == null)
            asgService = ServiceFactory.getASGService();

        // Log the fault injection
        logger.error("Injecting fault: Attach new LaunchConfiguration with different AMI to the ASG");

        // Grab the current Launch Configuration
        LaunchConfiguration lc = asgService.getLaunchConfigurationForAutoScalingGroup(asgName);
        if (lc == null) {
            throw new HoneyCombException("LC or ASG do not exist");
        }

        // Create a new LaunchConfiguration based on the current LC with the faulty AMI ID
        // and with name "faulty-lc"
        CreateLaunchConfigurationRequest req = new CreateLaunchConfigurationRequest();
        req.withImageId(faultyAmiId).
                withInstanceType(lc.getInstanceType()).
                withKeyName(lc.getKeyName()).
                withLaunchConfigurationName("faulty-lc").
                withSecurityGroups(lc.getSecurityGroups());
        asgService.createLaunchConfiguration(req);

        // Update the ASG to use the new faulty LC
        asgService.updateLaunchConfigurationInAutoScalingGroup(asgName, "faulty-lc");


		/* Terminate 1 random instance in the ASG to trigger the launch of a faulty LC instance */

        // Get the AutoScalingGroup with given Name
        if(asg == null)
            asg = asgService.getAutoScalingGroup(asgName);
        if (asg == null) {
            throw new HoneyCombException("Invalid ASG name provided");
        }

        // Get the list of "online" EC2 Instances in the ASG
        // (i.e. the EC2 instances which has state "pending" or "running")
        List<Instance> ec2RunningInstances = new ArrayList<Instance>();
        List<com.amazonaws.services.autoscaling.model.Instance> asgInstances = asg.getInstances();
        for (com.amazonaws.services.autoscaling.model.Instance asgInstance : asgInstances) {
            Instance ec2Instance = ec2Service.describeEC2Instance(asgInstance.getInstanceId());
            if (ec2Instance.getState().getName().equals("pending") ||
                    ec2Instance.getState().getName().equals("running")) {
                ec2RunningInstances.add(ec2Instance);
            }
        }

        // If the ASG has any "online" instances
        if (!ec2RunningInstances.isEmpty()) {

            // Randomize an online Instance to be killed
            Collections.shuffle(ec2RunningInstances);
            Instance instanceToInject = ec2RunningInstances.get(0);

            // Log the action
            logger.error("Faulty LaunchConfiguration with wrong AMI updated. Terminating instance with id = "
                    + instanceToInject.getInstanceId() + " for spawning new instance with faulty LC...");

            // Terminate the instance
            ec2Service.terminateInstance(instanceToInject.getInstanceId());

            // Delay for 5 minutes (ASG EC2 Health Check time) for ASG to spawn new faulty instance
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

    }

    /**
     * ec2Service setter for testing purpose
     * @param ec2Service
     */
    public void ec2ServiceSetter(EC2Service ec2Service){
        this.ec2Service = ec2Service;
    }


    /**
     * asgService setter for testing purpose
     * @param asgService
     */
    public void asgServiceSetter(ASGService asgService){
        this.asgService = asgService;
    }

    /**
     * asg Setter for testing purpose
     * @param asg
     */
    public void asgSetter (AutoScalingGroup asg){
        this.asg = asg;
    }
}
