
import fi.core.Helloworld;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

/**
 * Created by wilsoncao on 5/24/16.
 */
public class MainTest {

    private static Helloworld obj;

    @BeforeClass
    public static void initObj(){
        obj = new Helloworld();
    }
    @org.junit.Test
    public void TestAddition() throws Exception {
        int result = obj.addition(3,4);
        assertEquals(7,result);
    }

    @org.junit.Test
    public void TestAddtion2() throws Exception{
        int result = obj.addition(4,4);
        assertEquals(8,result);
    }
}