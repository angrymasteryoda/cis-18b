package tk.michael.project.util;

import java.io.Serializable;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 5:06 PM
 */
public class Database implements Serializable {
	private String name;
	private String host;
	private String port;
	private String username;
	private String password; //todo encrypt this

	public Database( String name, String host, String port, String username, String password ) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost( String host ) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort( String port ) {
		this.port = port;
	}
}
