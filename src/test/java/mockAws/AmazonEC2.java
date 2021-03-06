package mockAws;

import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AmazonEC2 {
  public static com.amazonaws.services.ec2.AmazonEC2 amazonEC2;

  public static com.amazonaws.services.ec2.AmazonEC2 getAmazonEC2() {
    amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    com.amazonaws.services.ec2.model.DescribeImagesResult describeImagesRes =
        mockAws
            .DescribeImagesResult
            .getDescribeImagesResult();
    when(amazonEC2.describeImages()).thenReturn(describeImagesRes);

    com.amazonaws.services.ec2.model.DescribeInstancesResult
        describeInstancesResult = mockAws
        .DescribeInstancesResult
        .getDescribeInstancesResult();
    when(amazonEC2.describeInstances()).thenReturn(describeInstancesResult);

    doNothing().when(amazonEC2).createTags(any());

    com.amazonaws.services.ec2.model.DescribeKeyPairsResult
        describeKeyPairsResult = mockAws
        .DescribeKeyPairsResult
        .getDescribeKeyPairsResult();
    when(amazonEC2.describeKeyPairs()).thenReturn(describeKeyPairsResult);

    com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
        describeSecurityGroupsResult = mockAws
        .DescribeSecurityGroupsResult.getDescribeSecurityGroupsResult();
    when(amazonEC2.describeSecurityGroups()).thenReturn
        (describeSecurityGroupsResult);

    doNothing().when(amazonEC2).revokeSecurityGroupIngress(any());

    doNothing().when(amazonEC2).authorizeSecurityGroupIngress(any());

    com.amazonaws.services.ec2.model.RunInstancesResult res = mock(com
        .amazonaws.services.ec2.model.RunInstancesResult.class);
    RunInstancesResult.runInstancesResult = RunInstancesResult
        .getrunInstancesResultWithAlternatingInstanceState();
    when(amazonEC2.runInstances(any())).thenReturn(res);

    doNothing().when(amazonEC2).terminateInstances(any());

    return amazonEC2;
  }
}
