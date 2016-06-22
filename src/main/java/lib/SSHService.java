package lib;

import java.io.IOException;
import java.util.List;

/**
 * SSHService interface.
 * Provided wrapper services for various operations which involve SSH'ing into EC2 instances.
 *
 * @author anbinhtran
 */
public interface SshService {

  /**
   * Execute a list of Shell commands on an instance, via SSH.
   *
   * @param hostname       the public hostname of the Instance
   * @param sshUser        the Linux user for SSH login (e.g. "ubuntu")
   * @param sshKeyFilePath the path to a Key pair (.PEM) file for SSH authorization
   * @param commands       a list of Shell commands to be executed.
   *                       The commands will be run one after another
   * @throws IOException if an exception occur
   *     when trying to read the Key file or creating the SSH connection
   */
  public void executeSshCommands(
      String hostname, String sshUser, String sshKeyFilePath, List<String> commands)
      throws IOException;

  /**
   * Execute one Shell command on an instance via SSH. Return the command output.
   *
   * @param hostname       the public hostname of the Instance
   * @param sshUser        the Linux user for SSH login (e.g. "ubuntu")
   * @param sshKeyFilePath the path to a Key pair (.PEM) file for SSH authorization
   * @param command        the Shell command to be executed
   * @return the output of the Shell command executed on the instance
   * @throws IOException if an exception occur when trying to read
   *     the Key file or creating the SSH connection
   */
  public String executeSshCommandReturnOutput(
      String hostname, String sshUser, String sshKeyFilePath, String command)
      throws IOException;

}
