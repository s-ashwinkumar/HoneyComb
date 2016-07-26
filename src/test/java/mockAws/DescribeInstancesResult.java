package mockAws;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeInstancesResult {
  private static com.amazonaws.services.ec2.model.DescribeInstancesResult
      describeInstancesResult;

  public static com.amazonaws.services.ec2.model.DescribeInstancesResult
  getDescribeInstancesResultWithInstances() {
    describeInstancesResult = mock(com.amazonaws.services.ec2.model
        .DescribeInstancesResult.class);
    Reservation reservation = mock(Reservation.class);
    List<Reservation> reservations = new ArrayList<>();
    List<Instance> instances = new ArrayList<>();
    Instance instance = mock(Instance.class);
    instances.add(instance);
    when(reservation.getInstances()).thenReturn(instances);
    reservations.add(reservation);
    when(describeInstancesResult.getReservations()).thenReturn(reservations);
    return describeInstancesResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeInstancesResult
  getDescribeInstancesResultWithoutReservation() {
    describeInstancesResult = mock(com.amazonaws.services.ec2.model
        .DescribeInstancesResult.class);
    List<Reservation> reservations = new ArrayList<>();
    when(describeInstancesResult.getReservations()).thenReturn(reservations);
    return describeInstancesResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeInstancesResult
  getDescribeInstancesResultWithoutException() {
    describeInstancesResult = mock(com.amazonaws.services.ec2.model
        .DescribeInstancesResult.class);
    List<Reservation> reservations = new ArrayList<>();
    when(describeInstancesResult.getReservations()).thenThrow
        (AmazonServiceException.class);
    return describeInstancesResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeInstancesResult
  getDescribeInstancesResult() {
    describeInstancesResult = mock(com.amazonaws.services.ec2.model
        .DescribeInstancesResult.class);
    Reservation reservation = mock(Reservation.class);
    List<Reservation> reservations = new ArrayList<>();
    List<Instance> instances = new ArrayList<>();
    Instance instance = mock(Instance.class);
    reservations.add(reservation);
    when(describeInstancesResult.getReservations()).thenReturn(reservations);
    return describeInstancesResult;
  }

  public static com.amazonaws.services.ec2.model.DescribeInstancesResult
  getDescribeInstancesResultWithGivenInstance(Instance instance) {
    describeInstancesResult = mock(com.amazonaws.services.ec2.model
        .DescribeInstancesResult.class);
    Reservation reservation = mock(Reservation.class);
    List<Reservation> reservations = new ArrayList<>();
    List<Instance> instances = new ArrayList<>();
    instances.add(instance);
    when(reservation.getInstances()).thenReturn(instances);
    reservations.add(reservation);
    when(describeInstancesResult.getReservations()).thenReturn(reservations);
    return describeInstancesResult;
  }
}
