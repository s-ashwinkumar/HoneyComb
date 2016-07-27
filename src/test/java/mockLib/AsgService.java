package mockLib;

import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.*;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class AsgService {
  public static lib.AsgService asgService;

  public static lib.AsgService getAsgService() {
    asgService = mock(lib.AsgService.class);
    LaunchConfiguration lc = mockAws.LaunchConfiguration.getLc();
    when(asgService.getLaunchConfigurationForAutoScalingGroup(anyString()))
        .thenReturn(lc);
    when(asgService.createLaunchConfiguration(any())).thenReturn("ok");
    AutoScalingGroup asg = mockAws.AutoScalingGroup.getAsg();
    when(asgService.getAutoScalingGroup(any())).thenReturn(asg);
    doNothing().when(asgService).updateLaunchConfigurationInAutoScalingGroup
        (anyString(), anyString());
    when(asgService.getAutoScalingGroup(matches("null"))).thenReturn(null);
    return asgService;
  }

  public static lib.AsgService getAsgServiceWithEmptyLB() {
    asgService = mock(lib.AsgService.class);
    LaunchConfiguration lc = mockAws.LaunchConfiguration.getLc();
    when(asgService.getLaunchConfigurationForAutoScalingGroup(anyString()))
        .thenReturn(lc);
    when(asgService.createLaunchConfiguration(any())).thenReturn("ok");
    AutoScalingGroup asg = mockAws.AutoScalingGroup.getAsgWithEmptyList();
    when(asgService.getAutoScalingGroup(any())).thenReturn(asg);
    doNothing().when(asgService).updateLaunchConfigurationInAutoScalingGroup
        (anyString(), anyString());
    when(asgService.getAutoScalingGroup(matches("null"))).thenReturn(null);
    return asgService;
  }
}
