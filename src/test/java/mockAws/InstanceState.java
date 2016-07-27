package mockAws;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class InstanceState {
  private static com.amazonaws.services.ec2.model.InstanceState state;

  public static com.amazonaws.services.ec2.model.InstanceState
  getInstanceState() {
    state = mock(com.amazonaws.services.ec2.model.InstanceState.class);
    when(state.getName()).thenReturn("running");
    return state;
  }

  public static com.amazonaws.services.ec2.model.InstanceState
  getInstanceStateWithAlternatingState() {
    state = mock(com.amazonaws.services.ec2.model.InstanceState.class);
    when(state.getName()).thenReturn("Stopped", "running");
    return state;
  }
}
