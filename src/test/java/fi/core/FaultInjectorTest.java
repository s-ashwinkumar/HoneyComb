package fi.core;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.CaseInsensitiveMap;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import static org.junit.Assert.*;

/**
 * Created by joe on 16/6/13.
 */
public class FaultInjectorTest {

//    @Test
//    public void testValidateFault() throws Exception {
//        /**
//         * we assume token validation is passed.
//         * This token validation will not handle it at all.
//         */
//
//        /**
//         * We use faultId = 10000 to validate the correctness.
//         * 10000 contains arguments such as instancename,
//         * secondargument, thirdargument.
//         */
//        String faultId = "10000";
//        /**
//         * missing arguments (why cannot pass the validation)
//         */
//        StringBuilder reason = new StringBuilder();
//        /**
//         * vertex MultiMap.
//         */
//        MultiMap map = new CaseInsensitiveHeaders();
//
//        /**
//         * the first test, we don't give any arguments
//         */
//        FaultInjector object1 = new FaultInjector(faultId, map);
//        assertFalse(object1.validate(reason));
//        assertNotEquals("",reason.toString());
//
//        /**
//         * the second test, we miss thirdargument
//         */
//        reason.delete(0,reason.length());
//        map.add("instancename", "test2");
//        map.add("secondargument", "test2");
//        FaultInjector object2 = new FaultInjector(faultId, map);
//        assertFalse(object2.validate(reason));
//        assertEquals("thirdargument", reason.toString());
//
//        /**
//         * the third test, we miss nothing.
//         */
//        reason.delete(0,reason.length());
//        map.add("thirdargument","test3");
//        FaultInjector object3 = new FaultInjector(faultId, map);
//        assertTrue(object3.validate(reason));
//        assertEquals("",reason.toString());
//    }
//
//    @Test
//    public void testInjection() throws Exception {
//        /**
//         * we assume token validation is passed.
//         * This validation will not handle it at all.
//         *
//         * We also assume the validation of the arguments are passed
//         */
//
//        /**
//         * We use faultId = 10001 to inject.
//         * 10001 contains arguments such as asgName;faultyAmiId.
//         */
//        String faultId = "10001";
//
//        /**
//         * missing arguments (why cannot pass the validation)
//         */
//        StringBuilder reason = new StringBuilder();
//
//        /**
//         * vertex MultiMap.
//         */
//        MultiMap map = new CaseInsensitiveHeaders();
//
//        /**
//         * make a fault and pass the arguments validation
//         */
//        map.add("asgName", "test2");
//        map.add("faultyAmiId", "ami-123");
//        FaultInjector object1 = new FaultInjector(faultId, map);
//
//        assertTrue(object1.validate(reason));
//
//        String faultInstanceId = object1.inject();
//
//        /**
//         * test pass, we get the faultInstanceId
//         */
//        assertNotNull(faultInstanceId);
//        assertEquals(18, faultInstanceId.length());
//        assertEquals(faultId, faultInstanceId.substring(13));
//
//        /**
//         * test the fault injection resutls, to see whether there exists end
//         */
//        boolean isExist = false;
//        Thread.sleep(3000);
//        File file = new File("src/main/resources/log");
//
//        try{
//            //firstly, we make sure we have the log file
//            Assert.assertTrue(file.exists());
//
//            if (!file.exists()) {
//                System.out.println(" file not existed yet");
//            } else {
//                String  thisLine = null;
//                FileReader fileReader = new FileReader(file);
//                BufferedReader br = new BufferedReader(fileReader);
//
//                while ((thisLine = br.readLine()) != null) {
//                    if(thisLine.indexOf("fault injection finish!") != -1 || thisLine.indexOf("ERROR") != -1) {
//                        if (thisLine.indexOf(faultInstanceId) != -1) {
//                            isExist = true;
//                            break;
//                        }
//                    }
//                }
//                br.close();
//            }
//
//            Assert.assertTrue(isExist);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//    }
}
