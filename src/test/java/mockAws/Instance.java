package mockAws;

import com.amazonaws.services.ec2.model.InstanceState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class Instance {
  private static com.amazonaws.services.ec2.model.Instance instance;

  public static com.amazonaws.services.ec2.model.Instance getInstance(){
    instance = mock(com.amazonaws.services.ec2.model.Instance.class);
    com.amazonaws.services.ec2.model.InstanceState state = mockAws.InstanceState.getInstanceState();
    when(instance.getState()).thenReturn(state);
    return instance;
  }
}
