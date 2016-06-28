package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import java.io.IOException;

/**
 * Created by wilsoncao on 6/9/16.
 */
public interface FaultInterface {
  public void start() throws Exception;
}
