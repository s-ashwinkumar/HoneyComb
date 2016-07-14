package mockAws;


import com.amazonaws.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeInstancesResult {
  private static com.amazonaws.services.ec2.model.DescribeInstancesResult describeInstancesResult;
  public static com.amazonaws.services.ec2.model.DescribeInstancesResult getDescribeInstancesResult(){
    describeInstancesResult = mock(com.amazonaws.services.ec2.model.DescribeInstancesResult.class);
    Reservation reservation = mock(Reservation.class);
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(reservation);
    when(describeInstancesResult.getReservations()).thenReturn(reservations);
    return describeInstancesResult;
  }
}
