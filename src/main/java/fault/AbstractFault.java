package fault;

import java.util.HashMap;

/**
 * Created by wilsoncao on 6/10/16.
 */

public abstract class AbstractFault implements FaultInterface {
  private HashMap<String, String> params;

  public AbstractFault(HashMap<String, String> params) {
    this.params = params;
  }


}
