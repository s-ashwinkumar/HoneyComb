package fault;

/**
 * Created by wilsoncao on 6/9/16.
 */
public interface FaultInterface {
  void start() throws Exception;

  void terminate();
}
