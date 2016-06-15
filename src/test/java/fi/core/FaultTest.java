package fi.core;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.CaseInsensitiveMap;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import static org.junit.Assert.*;

/**
 * Created by joe on 16/6/13.
 */
public class FaultTest {

    @Test
    public void testValidateFault() throws Exception {
        /**
         * we assume token validation is passed.
         * This validation will not handle it at all.
         */

        /**
         * We use faultId = 10000 to validate the correctness.
         * 10000 contains arguments such as instancename,
         * secondargument, thirdargument.
         */
        String faultId = "10000";
        /**
         * missing arguments (why cannot pass the validation)
         */
        StringBuilder reason = new StringBuilder();
        /**
         * vertex MultiMap.
         */
        MultiMap map = new CaseInsensitiveHeaders();

        /**
         * the first test, we don't give any arguments
         */
        FaultInjector object1 = new FaultInjector(faultId, map);
        assertFalse(object1.validate(reason));
        assertNotEquals("",reason.toString());

        /**
         * the second test, we miss thirdargument
         */
        reason.delete(0,reason.length());
        map.add("instancename", "test2");
        map.add("secondargument", "test2");
        FaultInjector object2 = new FaultInjector(faultId, map);
        assertFalse(object2.validate(reason));
        assertEquals("thirdargument", reason.toString());

        /**
         * the third test, we miss nothingt
         */
        reason.delete(0,reason.length());
        map.add("thirdargument","test3");
        FaultInjector object3 = new FaultInjector(faultId, map);
        assertTrue(object3.validate(reason));
        assertEquals("",reason.toString());
    }

    @Test
    public void testInjection() throws Exception {


    }
}
