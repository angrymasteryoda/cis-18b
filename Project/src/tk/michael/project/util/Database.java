package tk.michael.project.util;

import com.michael.api.security.AES;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 4/22/15
 * Time: 5:06 PM
 */
public class Database implements Serializable {
	private final UUID id;
	private String name;
	private String host;
	private String port;
	private String username;
	private String password; //todo encrypt this
	private String databaseName;

	public Database( String name, String host, String port, String username, String password, String databaseName ) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.host = host;
		this.port = port;
		this.username = username;
		try {
			this.password = AES.encrypt( password );
		} catch ( Exception ex ){
			ex.printStackTrace();
		}
		this.databaseName = databaseName;
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
		String password = "";
		try {
			password = AES.decrypt( this.password );
		} catch ( Exception ex ){
			ex.printStackTrace();
		}
		return password;
	}

	public void setPassword( String password ) {
		try {
			this.password = AES.encrypt( password );
		} catch ( Exception ex ){
			ex.printStackTrace();
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort( String port ) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName( String databaseName ) {
		this.databaseName = databaseName;
	}

	public UUID getId() {
		return id;
	}

	public String getUrl(){
		return "jdbc:mysql://" + host + ":" + port + "/" + getDatabaseName();
	}

	@Override
	public String toString(){
		return ( host + ":" + port + " -db" + databaseName + " -u " + username + " -p " + password );
	}
}
