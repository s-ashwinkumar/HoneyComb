package mockAws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.KeyPairInfo;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeKeyPairsResult {
  private static com.amazonaws.services.ec2.model.DescribeKeyPairsResult
      describeKeyPairsResult;

  public static com.amazonaws.services.ec2.model.DescribeKeyPairsResult
  getDescribeKeyPairsResult() {
    describeKeyPairsResult = mock(com.amazonaws.services.ec2.model
        .DescribeKeyPairsResult.class);
    KeyPairInfo info = mock(KeyPairInfo.class);
    List<KeyPairInfo> keyPairs = new ArrayList<>();
    keyPairs.add(info);
    when(describeKeyPairsResult.getKeyPairs()).thenReturn(keyPairs);
    return describeKeyPairsResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeKeyPairsResult
  getDescribeKeyPairsResultWithEmptyKeyPairs() {
    describeKeyPairsResult = mock(com.amazonaws.services.ec2.model
        .DescribeKeyPairsResult.class);
    List<KeyPairInfo> keyPairs = new ArrayList<>();
    when(describeKeyPairsResult.getKeyPairs()).thenReturn(keyPairs);
    return describeKeyPairsResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeKeyPairsResult
  getDescribeKeyPairsResultWithException() {
    describeKeyPairsResult = mock(com.amazonaws.services.ec2.model
        .DescribeKeyPairsResult.class);
    when(describeKeyPairsResult.getKeyPairs()).thenThrow
        (AmazonServiceException.class);
    return describeKeyPairsResult;
  }
}
