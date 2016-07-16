package lib;

import com.amazonaws.services.ec2.model.Image;
import mockAws.AmazonEC2;
import mockAws.DescribeImagesResult;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class Ec2ServiceImplTest {
  public Ec2ServiceImpl obj;

  @Before
  public void setUp() throws Exception {
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring");

  }

  @Test
  public void testConstructors() throws Exception {
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring");
    assertEquals(obj.getFaultInstanceId(), "testinstanceidwithrandomstring");
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstring", null);
    assertNull(obj.getClient());
  }

  @Test
  public void describeAmi() throws Exception {
    Image result;
    result = obj.describeAmi(null);
    assertNull(result);

    AmazonEC2 ec2Obj = new AmazonEC2();
    ec2Obj.amazonEC2 = mock(com.amazonaws.services.ec2.AmazonEC2.class);
    DescribeImagesResult res = new DescribeImagesResult();

    // Valid parameter case
    when(ec2Obj.amazonEC2.describeImages()).thenReturn(null);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    try {
      obj.describeAmi("testingthisrandom");
    } catch (Exception ex) {
      assertNull(ex.getMessage());
    }

    // A full happy path
    com.amazonaws.services.ec2.model.DescribeImagesResult describeImagesRes =
        mockAws.DescribeImagesResult.getDescribeImagesResult();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertEquals(result, describeImagesRes.getImages().get(0));

    // Path when AMI is not available in the region
    describeImagesRes = mockAws.DescribeImagesResult.
        getDescribeImagesResultWithEmptyList();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertNull(null);

    // Part when there is AmazonServiceException
    describeImagesRes = mockAws.DescribeImagesResult.
        getDescribeImagesResultWithException();
    when(ec2Obj.amazonEC2.describeImages(any())).thenReturn(describeImagesRes);
    obj = new Ec2ServiceImpl("testinstanceidwithrandomstr", ec2Obj.amazonEC2);
    result = obj.describeAmi("testingthisrandom");
    assertNull(null);

  }
}
