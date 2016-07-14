package mockAws;

import com.amazonaws.services.ec2.model.Image;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/13/16.
 */
public class DescribeImagesResult {
  private static com.amazonaws.services.ec2.model.DescribeImagesResult describeImagesResult;

  public static com.amazonaws.services.ec2.model.DescribeImagesResult getDescribeImagesResult(){
    describeImagesResult = mock(com.amazonaws.services.ec2.model.DescribeImagesResult.class);
    Image image = mock(Image.class);
    List<Image> images = new ArrayList<>();
    images.add(image);
    when(describeImagesResult.getImages()).thenReturn(images);
    return describeImagesResult;
  }
}
