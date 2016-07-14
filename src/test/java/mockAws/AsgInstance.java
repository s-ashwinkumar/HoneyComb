package mockAws;

/**
 * Created by wilsoncao on 7/13/16.
 */

import static org.mockito.Mockito.*;

public class AsgInstance {
  private static com.amazonaws.services.autoscaling.model.Instance instance;

  public static com.amazonaws.services.autoscaling.model.Instance getInstance(){
    instance = mock(com.amazonaws.services.autoscaling.model.Instance.class);
    return instance;
  }
}
