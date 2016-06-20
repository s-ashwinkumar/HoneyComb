package fi.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ashwin on 6/17/16.
 */
public class UtilsTest {
  @Test
  public void isNumeric() {
    assertEquals(Utils.isNumeric("haha"),false);
    assertEquals(Utils.isNumeric("123"),true);
  }

  @Test
  public void testForbiddenConstructor(){
    try{
      Utils obj = new Utils();
    }catch(Exception ex){
      assertEquals("Instances of this type are forbidden.",ex.getMessage());
    }
  }
}