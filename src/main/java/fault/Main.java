package fault;


import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import lib.EC2Service;
import lib.ServiceFactory;


/**
 * Created by wilsoncao on 6/13/16.
 */
public class Main {
    public static void main(String[] args){

        ChangeAMIInLCFault fault = new ChangeAMIInLCFault("asg","ami-2ef2d244",null);
        try {
            fault.start();
        }catch(Exception e){
            e.printStackTrace();
        }

//        EC2Service ec2Service = ServiceFactory.getEC2Service();
//        RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
//                .withInstanceType(InstanceType.T2Micro)
//                .withImageId("ami-fce3c696")
//                .withMinCount(1)
//                .withMaxCount(1);
//        ec2Service.runInstance(runInstancesRequest);
    }
}
