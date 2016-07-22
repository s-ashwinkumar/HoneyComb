package mockLib;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by wilsoncao on 7/16/16.
 */
public class SshService {
  public static lib.SshService sshService;
  public static lib.SshService getSshService(){
    sshService = mock(lib.SshService.class);
    try {
      doNothing().when(sshService).executeSshCommands(any(), any(), any(), any());
      when(sshService.executeSshCommandReturnOutput(any(),any(),any(),any())).thenReturn("ok");
    }catch(IOException e){
      e.printStackTrace();
    }
    return sshService;

  }
}
