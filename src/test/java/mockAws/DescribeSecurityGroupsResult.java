package mockAws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.SecurityGroup;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeSecurityGroupsResult {
  private static com.amazonaws.services.ec2.model
      .DescribeSecurityGroupsResult describeSecurityGroupsResult;

  public static com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
  getDescribeSecurityGroupsResult() {
    describeSecurityGroupsResult = mock(com.amazonaws.services.ec2.model
        .DescribeSecurityGroupsResult.class);
    SecurityGroup sg = mock(SecurityGroup.class);
    List<SecurityGroup> sgs = new ArrayList<>();
    sgs.add(sg);
    when(describeSecurityGroupsResult.getSecurityGroups()).thenReturn(sgs);
    return describeSecurityGroupsResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
  getDescribeSecurityGroupsResultWithEmptySecurityGroups() {
    describeSecurityGroupsResult = mock(com.amazonaws.services.ec2.model
        .DescribeSecurityGroupsResult.class);
    List<SecurityGroup> sgs = new ArrayList<>();
    when(describeSecurityGroupsResult.getSecurityGroups()).thenReturn(sgs);
    return describeSecurityGroupsResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult
  getDescribeSecurityGroupsResultWithException() {
    describeSecurityGroupsResult = mock(com.amazonaws.services.ec2.model
        .DescribeSecurityGroupsResult.class);
    when(describeSecurityGroupsResult.getSecurityGroups()).thenThrow
        (AmazonServiceException.class);
    return describeSecurityGroupsResult;
  }
}
