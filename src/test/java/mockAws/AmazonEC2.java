package mockAws;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.model.Instance;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AmazonEC2 {
  public static com.amazonaws.services.ec2.AmazonEC2 amazonEC2;
  public static com.amazonaws.services.ec2.AmazonEC2 getAmazonEC2(){
    amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    com.amazonaws.services.ec2.model.DescribeImagesResult describeImagesRes = mockAws
        .DescribeImagesResult
        .getDescribeImagesResult();
    when(amazonEC2.describeImages()).thenReturn(describeImagesRes);

    com.amazonaws.services.ec2.model.DescribeInstancesResult describeInstancesResult = mockAws
        .DescribeInstancesResult
        .getDescribeInstancesResult();
    when(amazonEC2.describeInstances()).thenReturn(describeInstancesResult);

    doNothing().when(amazonEC2).createTags(any());

    com.amazonaws.services.ec2.model.DescribeKeyPairsResult describeKeyPairsResult = mockAws
        .DescribeKeyPairsResult
        .getDescribeKeyPairsResult();
    when(amazonEC2.describeKeyPairs()).thenReturn(describeKeyPairsResult);

    com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult describeSecurityGroupsResult = mockAws
        .DescribeSecurityGroupsResult.getDescribeSecurityGroupsResult();
    when(amazonEC2.describeSecurityGroups()).thenReturn(describeSecurityGroupsResult);

    doNothing().when(amazonEC2).revokeSecurityGroupIngress(any());

    doNothing().when(amazonEC2).authorizeSecurityGroupIngress(any());

    RunInstancesResult res = mock(RunInstancesResult.class);
    Reservation t = mock(Reservation.class);
    Instance instance = mockAws.Instance.getInstance();
    when(t.getInstances().get(0)).thenReturn(instance);
    when(res.getReservation()).thenReturn(t);
    when(amazonEC2.runInstances(any())).thenReturn(res);

    doNothing().when(amazonEC2).terminateInstances(any());

    return amazonEC2;
  }
}
