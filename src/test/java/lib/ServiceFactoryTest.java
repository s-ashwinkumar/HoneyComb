package lib;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by ashwin on 7/22/16.
 */
public class ServiceFactoryTest {
  public ServiceFactory obj;

  @Test
  public void testGetters() throws Exception {
    obj = new ServiceFactory();
    AsgService AsgService = ServiceFactory.getAsgService("testingwiththisid");;
    Ec2Service Ec2Service = ServiceFactory.getEc2Service("testingwiththisid");;
    ElbService ElbService = ServiceFactory.getElbService("testingwiththisid");;
    SshService SshService = ServiceFactory.getSshService("testingwiththisid");;
    assertEquals(AsgService, obj.getAsgServiceTest());
    assertEquals(Ec2Service, obj.getEc2ServiceTest());
    assertEquals(ElbService, obj.getElbServiceTest());
    assertEquals(SshService, obj.getSshServiceTest());
  }
}
