package mockAws;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class Instance {
  public static com.amazonaws.services.ec2.model.Instance instance;

  public static com.amazonaws.services.ec2.model.Instance getInstance() {
    instance = mock(com.amazonaws.services.ec2.model.Instance.class);
    com.amazonaws.services.ec2.model.InstanceState state = mockAws
        .InstanceState.getInstanceState();
    when(instance.getState()).thenReturn(state);
    when(instance.getInstanceId()).thenReturn("1234");
    return instance;
  }

  public static com.amazonaws.services.ec2.model.Instance
  getInstanceWithAlternatingState() {
    instance = mock(com.amazonaws.services.ec2.model.Instance.class);
    com.amazonaws.services.ec2.model.InstanceState state = mockAws
        .InstanceState.getInstanceStateWithAlternatingState();
    when(instance.getState()).thenReturn(state);
    when(instance.getInstanceId()).thenReturn("1234");
    return instance;
  }

  public static com.amazonaws.services.ec2.model.Instance
  getInstanceWithGivenLaunchTime(Date time) {
    instance = mock(com.amazonaws.services.ec2.model.Instance.class);
    when(instance.getLaunchTime()).thenReturn(time);
    return instance;
  }


}
