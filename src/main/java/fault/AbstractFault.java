package fault;

import fault.FaultInterface;

import java.util.HashMap;

/**
 * Created by wilsoncao on 6/10/16.
 */

public abstract class AbstractFault implements FaultInterface {
  private HashMap<String, String> params;
  private boolean isTerminated = false;
  protected String faultInstanceId;

  public AbstractFault(HashMap<String, String> params) {
    this.faultInstanceId = params.get("faultInstanceId");
    this.params = params;
  }

  public void terminate() {
    isTerminated = true;
  }

  public boolean isTerminated() {
    return isTerminated;
  }

}
