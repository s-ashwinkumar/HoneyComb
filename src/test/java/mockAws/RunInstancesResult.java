package mockAws;

import com.amazonaws.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ashwin on 7/16/16.
 */
public class RunInstancesResult {
  public static com.amazonaws.services.ec2.model.RunInstancesResult
      runInstancesResult;

  public static com.amazonaws.services.ec2.model.RunInstancesResult
  getrunInstancesResult() {
    runInstancesResult = mock(com.amazonaws.services.ec2.model
        .RunInstancesResult.class);
    Reservation t = mock(Reservation.class);
    com.amazonaws.services.ec2.model.Instance instance = mockAws.Instance
        .getInstance();
    when(t.getInstances().get(0)).thenReturn(instance);
    when(runInstancesResult.getReservation()).thenReturn(t);

    return runInstancesResult;

  }

  public static com.amazonaws.services.ec2.model.RunInstancesResult
  getrunInstancesResultWithAlternatingInstanceState() {
    runInstancesResult = mock(com.amazonaws.services.ec2.model
        .RunInstancesResult.class);
    Reservation reservation = mock(Reservation.class);
    com.amazonaws.services.ec2.model.Instance instance = mockAws.Instance
        .getInstanceWithAlternatingState();
    List<com.amazonaws.services.ec2.model.Instance> instances = new ArrayList();
    instances.add(instance);
    when(reservation.getInstances()).thenReturn(instances);

    when(runInstancesResult.getReservation()).thenReturn(reservation);

    return runInstancesResult;

  }
}
