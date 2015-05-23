package tk.michael.project.db;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Risher
 * Date: 5/22/15
 * Time: 5:16 PM
 */
public class ConnectionStatus {
	private final boolean isConnected;
	private String reason = null;

	public ConnectionStatus( String reason, boolean isConnected ) {
		this.reason = reason;
		this.isConnected = isConnected;
	}

	public ConnectionStatus( boolean connected ) {
		isConnected = connected;
	}

	public ConnectionStatus( SQLException error ) {
		this.isConnected = false;
		this.reason = error.getMessage();
	}

	public boolean isConnected() {
		return isConnected;
	}

	public String getReason() {
		return reason;
	}

	public void setReason( String reason ) {
		this.reason = reason;
	}
}
