package mockAws;

import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class LaunchConfiguration {
  private static com.amazonaws.services.autoscaling.model.LaunchConfiguration lc;

  public static com.amazonaws.services.autoscaling.model.LaunchConfiguration getLc(){
    lc = mock(com.amazonaws.services.autoscaling.model.LaunchConfiguration.class);
    when(lc.getInstanceType()).thenReturn("ok");
    when(lc.getKeyName()).thenReturn("ok");
    when(lc.getSecurityGroups()).thenReturn(null);
    return lc;
  }

}
