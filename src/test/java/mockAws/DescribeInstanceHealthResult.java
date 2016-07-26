package mockAws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.elasticloadbalancing.model.InstanceState;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeInstanceHealthResult {
  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeInstanceHealthResult
      describeInstanceHealthResult;

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeInstanceHealthResult
  getDescribeInstanceHealthResult() {
    describeInstanceHealthResult =
        mock(com.amazonaws.services.elasticloadbalancing.model
            .DescribeInstanceHealthResult.class);
    InstanceState state = mock(InstanceState.class);
    List<InstanceState> instances = new ArrayList<>();
    instances.add(state);
    when(describeInstanceHealthResult.getInstanceStates()).thenReturn
        (instances);
    return describeInstanceHealthResult;
  }

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeInstanceHealthResult
  getDescribeInstanceHealthResultWithException() {
    describeInstanceHealthResult =
        mock(com.amazonaws.services.elasticloadbalancing.model
            .DescribeInstanceHealthResult.class);
    when(describeInstanceHealthResult.getInstanceStates()).thenThrow
        (AmazonServiceException.class);
    return describeInstanceHealthResult;
  }

  public static com.amazonaws.services.elasticloadbalancing.model
      .DescribeInstanceHealthResult
  getDescribeInstanceHealthResultWithEmptyList() {
    describeInstanceHealthResult =
        mock(com.amazonaws.services.elasticloadbalancing.model
            .DescribeInstanceHealthResult.class);
    List<InstanceState> instances = new ArrayList<>();
    when(describeInstanceHealthResult.getInstanceStates()).thenReturn
        (instances);
    return describeInstanceHealthResult;
  }
}
