package fi.core;

import fault.Fault;
import io.vertx.core.MultiMap;
import loggi.faultinjection.Loggi;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/6/13.
 */
public class FaultInjector {

  /**
   * The faultId indicate a fault in the fault list.
   */
  private final String faultId;

  /**
   * The multimap contains all the arguments is transfer.
   */
  private final MultiMap map;

  /**
   * constructor for the Faultinjector.
   *
   * @param id      fault id
   * @param hashMap hash map for arguments
   */
  public FaultInjector(final String id, final MultiMap hashMap) {

    this.faultId = id;
    this.map = hashMap;
  }

  /**
   * This method is used to check the arguments of the fault beforehand.
   * @param reason string builder to add reason
   * @param fileName database configuration
   * @return true false
   * @throws Exception potential exception
     */
  public final boolean validate(final StringBuilder reason,
                                final String fileName) throws Exception {

    DbConnection dbCon = Utils.returnDbconnection(fileName);
    FaultModel fault = FaultModel.getFault(dbCon, faultId);
    String arguments = fault.getArguments();
    /**
     * the default value will be null in db.
     */
    if (arguments != null) {

      for (String args : arguments.split(";")) {
        if (map.get(args) == null) {
          reason.append(args);
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This is the inject function.
   * @param fileName the database configuration
   * @return faultInstanceId
   * @throws Exception potential exception
     */
  public final String inject(final String fileName) throws Exception {
    DbConnection dbCon = Utils.returnDbconnection(fileName);
    FaultModel fault = FaultModel.getFault(dbCon, faultId);
    final String location = fault.getLocation();
    final String name = fault.getName();


    /**
     * generate the faultInstanceId 13 + faultId.
     */
    long unixTime = System.currentTimeMillis();
    final String faultInstanceId = String.valueOf(unixTime) + faultId;

    /**
     * load fault script and inject in a thread.
     */
    Loggi loggi = new Loggi(faultInstanceId, name);

    Thread faultThread = new Thread(new Runnable() {
      public void run() {
        try {

          /**
           * put the arguments into hashMap.
           */
          HashMap<String, String> params = new HashMap<>();
          params.put("faultInstanceId", faultInstanceId);
          for (Map.Entry<String, String> item : map.entries()) {
            params.put(item.getKey(), item.getValue());
          }

          File authorizedJarFile = new File(location);
          ClassLoader authorizedLoader = URLClassLoader
              .newInstance(new URL[]{authorizedJarFile.toURI().toURL()});
          Fault authorizedPlugin = (Fault) authorizedLoader.loadClass(name)
              .getDeclaredConstructor(HashMap.class)
              .newInstance(params);
          authorizedPlugin.start();
        } catch (Exception ex) {
          //e.printStackTrace();
          loggi.error(ex);
        }
      }
    });

    faultThread.setName(faultInstanceId);
    faultThread.start();
    return faultInstanceId;
  }
}
