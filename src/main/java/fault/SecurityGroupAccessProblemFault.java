package fault;

import lib.Ec2Service;
import lib.ServiceFactory;
import loggi.faultinjection.Loggi;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;

/**
 * Created by wilsoncao on 7/7/16.
 */
public class SecurityGroupAccessProblemFault extends AbstractFault{
  private String faultInstanceId;
  private String asgName;
  private static Loggi logger;
  private String failedSecurityGroupName;

  public SecurityGroupAccessProblemFault(HashMap<String,String> params) throws IOException {

    super(params);
    this.asgName = params.get("asgName");
    this.faultInstanceId = params.get("faultInstanceId");
    this.failedSecurityGroupName = params.get("failedSecurityGroupName");
    logger = new Loggi(faultInstanceId,SecurityGroupAccessProblemFault.class.getName());
  }

  public void start() throws Exception{
    Ec2Service ec2Service = ServiceFactory.getEc2Service(this.faultInstanceId);

    // Log the fault injection
    logger.log("Injecting fault: Security Group access problem fault to all instances");

    // Inject fault: Security Group access problem (change SG Inbound rules)
    ec2Service.revokeSecurityGroupInboundRule(failedSecurityGroupName, "tcp", 80, "0.0.0.0/0");

    // Delay for 30s for ELB to detect the failure
    Thread.sleep(30000);
  }
}
