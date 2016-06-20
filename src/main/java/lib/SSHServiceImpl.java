package lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * Implementation class for SSH services.
 * 
 * @author anbinhtran
 *
 */
public class SSHServiceImpl implements SSHService {

	static final Logger logger = LogManager.getLogger(SSHService.class.getName());
	
	@Override
	public void executeSSHCommands(String hostname, String sshUser, 
			String sshKeyFilePath, List<String> commands) throws IOException {
		
		// If no command provided, do nothing
		if (commands == null || commands.isEmpty()) {
			logger.debug("No Shell command provided for SSH");
			return;
		}
		
		// Load credentials for SSH'ing to the instance
		File sshKeyFile = new File(sshKeyFilePath);
		String sshKeyFilePass = "a"; // will be ignored if not needed

		// Open SSH connection to EC2 Instance
		Connection conn = new Connection(hostname);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPublicKey(sshUser, sshKeyFile, sshKeyFilePass);

		if (isAuthenticated == false) {
			throw new IOException("Authentication failed.");
		}

		logger.debug("Connection to " + hostname + " opened with user = " + sshUser + ", sshKeyFile = " + sshKeyFilePath);
		
		// Execute specified Shell commands, one after the other
		for (String cmd : commands) {
			
			// Create a SSH session (with -t flag) for each command execution
			Session sess = conn.openSession();
			sess.requestPTY("vt220");
			
			// Execute the command
			sess.execCommand(cmd);

			// Log command exit status, if available (otherwise "null")
			logger.debug("Shell command: " + cmd + ", Exit code: " + sess.getExitStatus() + 
					", Output: " + getSSHOutput(sess.getStdout()));
			
			// Close the session
			sess.close();
		}

		// Close the connection
		conn.close();
		
		logger.debug("Connection to " + hostname + " closed.");
		
	}

	@Override
	public String executeSSHCommandReturnOutput(String hostname,
			String sshUser, String sshKeyFilePath, String command)
			throws IOException {
		
		// If no command provided, do nothing
		if (GenericValidator.isBlankOrNull(command)) {
			logger.debug("No Shell command provided for SSH");
			return null;
		}

		// Load credentials for SSH'ing to the instance
		File sshKeyFile = new File(sshKeyFilePath);
		String sshKeyFilePass = "a"; // will be ignored if not needed

		// Open SSH connection to EC2 Instance
		Connection conn = new Connection(hostname);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPublicKey(sshUser, sshKeyFile, sshKeyFilePass);

		if (isAuthenticated == false) {
			throw new IOException("Authentication failed.");
		}

		logger.debug("Connection to " + hostname + " opened with user = " + sshUser + ", sshKeyFile = " + sshKeyFilePath);

		// Create a SSH session (with -t flag) for each command execution
		Session sess = conn.openSession();
		sess.requestPTY("vt220");

		// Execute the command
		sess.execCommand(command);

		// Grab the command execution output
		String output = getSSHOutput(sess.getStdout());
		
		// Log command exit status, if available (otherwise "null")
		logger.debug("Shell command: " + command + ", Exit code: " + sess.getExitStatus() + 
				", Output: " + output);

		// Close the session
		sess.close();

		// Close the connection
		conn.close();

		logger.debug("Connection to " + hostname + " closed.");
		
		// Return the Shell command output
		return output;
		
	}
	
	/**
	 * Private method.
	 * Grab the output from SSH session stdout. Trim any blank lines from the output.
	 * 
	 * @param sshSessionOutput the InputStream which is the stdout of the SSH session
	 * @return a String containing the appended output of the SSH session
	 * @throws IOException if an error occurred when reading the output
	 */
	private String getSSHOutput(InputStream sshSessionOutput) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		
		InputStream stdout = new StreamGobbler(sshSessionOutput);
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        while (true) {
        	String line = br.readLine();
        	if (line == null) {
        		break;
        	}
        	if (line.matches("^\\s*$")) {
        		continue;
        	}
        	if (sb.toString().isEmpty()) {
        		sb.append(line);
        	} else {
        		sb.append('\n');
        		sb.append(line);
        	}
        }
        
        br.close();
        
        return sb.toString();
	}

}
