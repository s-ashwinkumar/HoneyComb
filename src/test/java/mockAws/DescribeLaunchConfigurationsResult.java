package mockAws;

import com.amazonaws.services.autoscaling.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeLaunchConfigurationsResult {
  public static com.amazonaws.services.autoscaling.model
      .DescribeLaunchConfigurationsResult
      describeLaunchConfigurationsRes;

  public static com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult
  getDescribeLaunchConfigurationsRes(){
    describeLaunchConfigurationsRes =
        mock(com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult.class);
    List<com.amazonaws.services.autoscaling.model.LaunchConfiguration> lcs = new ArrayList<>();
    com.amazonaws.services.autoscaling.model.LaunchConfiguration lc = mock(com.amazonaws.services.autoscaling.model.LaunchConfiguration.class);
    lcs.add(lc);
    when(describeLaunchConfigurationsRes.getLaunchConfigurations()).thenReturn(lcs);
    return describeLaunchConfigurationsRes;
  }

  public static com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult
  getDescribeLaunchConfigurationsResWithEmptyLCS(){
    describeLaunchConfigurationsRes =
        mock(com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult.class);
    List<com.amazonaws.services.autoscaling.model.LaunchConfiguration> lcs = new ArrayList<>();
    when(describeLaunchConfigurationsRes.getLaunchConfigurations()).thenReturn(lcs);
    return describeLaunchConfigurationsRes;
  }
}
