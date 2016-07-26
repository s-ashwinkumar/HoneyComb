package mockAws;

/**
 * Created by wilsoncao on 7/13/16.
 */

import static org.mockito.Mockito.mock;

public class AsgInstance {
  public static com.amazonaws.services.autoscaling.model.Instance instance;

  public static com.amazonaws.services.autoscaling.model.Instance getInstance
      () {
    instance = mock(com.amazonaws.services.autoscaling.model.Instance.class);
    return instance;
  }

}
