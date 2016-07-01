package fault;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by joe on 16/6/22.
 */
public class TestFault extends AbstractFault {

    /**
     * private hashmap.
     */
    private final HashMap<String, String> paramsInstance;

    /**
     * constructor for testFault.
     * @param params = "params get from the http request."
     */
    public TestFault(final HashMap<String, String> params) {
        super(params);
        this.paramsInstance = params;
    }

    @Override
    public final void start() throws
            AmazonServiceException,
            AmazonClientException,
            HoneyCombException,
            IOException, InterruptedException {
        String className = this.getClass().getSimpleName();
        /**
         * construct the loggi
         */
        Loggi loggi =
                new Loggi(paramsInstance.get("faultInstanceId"), className);

        /**
         * remember to start the loggi.
         */
        loggi.start();

        Thread.sleep(10000);
        /**
         * terminate here.
         */
        if (this.isTerminated())
            return;

        /**
         * log the "hello world".
         */
        loggi.log("Hello world");

        /**
         * log the nothing arguments.
         */
        loggi.log(paramsInstance.get("nothing"));


        /**
         * log finish.
         */
        loggi.finish();
    }
}
