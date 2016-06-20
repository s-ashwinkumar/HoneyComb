package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by joe on 16/6/20.
 */
public class TestFault extends AbtractFault {

    public TestFault(HashMap<String, String> params) {
        super(params);
    }

    @Override
    public void start() throws AmazonServiceException, AmazonClientException, IOException{
        String className = this.getClass().getSimpleName();
        //                       faultInstanceId + className;
        Loggi loggi = new Loggi("123456789112300001", className);
        loggi.start();
        loggi.finish();
    }
}
