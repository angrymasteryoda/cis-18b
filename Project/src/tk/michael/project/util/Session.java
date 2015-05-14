package tk.michael.project.util;

import com.michael.api.IO.IO;
import tk.michael.project.Main;
import tk.michael.project.db.DbUtils;
import tk.michael.project.db.MysqlDatabase;

import java.io.*;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created By: Michael Risher
 * Date: 5/13/15
 * Time: 5:31 PM
 */
public class Session implements Serializable {
	private UUID userId;
	private long created;
	private long expires;
	private String username;

	private static String outFileDir = "data";
	private static String outFileName = outFileDir + "/session.dat";

	public Session( UUID userId, String username ){
		this.userId = userId;
		this.created = Util.getUnix();
		this.username = username;
		this.expires = created + ( 7 * 24 * 60 * 60 );
		// 7 days * 24 hours * 60 minutes * 60 seconds
	}

	public Session( UUID userId, long created, long expires, String username ) {
		this.userId = userId;
		this.created = created;
		this.expires = expires;
		this.username = username;
	}

	public boolean createSession(){
		boolean result = false;
		MysqlDatabase db = new MysqlDatabase();
		if ( db.open() ) {
			int find = db.numRows( DbUtils.prependTable( "sessions" ), "user='" + userId.toString() + "' and end!=-1" );
			if ( find == 0 ) {
				PreparedStatement state = db.query( "INSERT INTO " + DbUtils.prependTable( "sessions" ) + " ( user,start, expires ) VALUES (?,?,?)" );
				try {
					state.setString( 1, userId.toString() );
					state.setLong( 2, created );
					state.setLong( 3, expires );
					find = state.executeUpdate();
					if ( find == 0 ) {
						//it failed
						IO.println( "Failed to create session" );
					} else {
						Thread thread = new Thread(  ){
							public void run(){
								serialize();
								DatabaseHandler.saveDatabasesToDb();
							}
						};
						thread.start();
						result = true;
					}
				} catch ( SQLException ex ) {
					ex.printStackTrace();
				}
			} else {
				IO.println( "session already exists" );
			}
		}
		return result;
	}

	public void expire(){
		MysqlDatabase db = new MysqlDatabase(  );
		if ( db.open() ) {
			int find = db.numRows( DbUtils.prependTable( "sessions" ), "user='" + userId.toString() + "'" );
			if ( find != 0 ) {
				try {
					PreparedStatement preparedStatement = db.query( "DELETE FROM " + DbUtils.prependTable( "sessions" ) + " WHERE user='" + userId.toString() + "'" );
					find = preparedStatement.executeUpdate();

					File save = new File( outFileName );
					save.delete();
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
		db.close();

	}

	public void serialize(){
		try {
			File testFile = new File( outFileName );
			if ( !testFile.exists() ) {
				testFile.getParentFile().mkdir();
				testFile.createNewFile();
			}
			FileOutputStream file = new FileOutputStream( outFileName );
			ObjectOutputStream out = new ObjectOutputStream( file );
			out.writeObject( this );
			out.close();
			file.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public static void load(){
		final Session session = read();
		if ( session != null ) {
			if ( session.getExpires() < Util.getUnix() ) {
				//session expired don't log them in and expire them
				Thread thread = new Thread(){
					public void run(){
						session.expire();
					}
				};
			} else {
				Main.login( session );
			}
		}
	}

	private static Session read(){
		try {
			FileInputStream file = new FileInputStream( outFileName );
			ObjectInputStream in = new ObjectInputStream( file );
			Session session = (Session) in.readObject();
			in.close();
			file.close();
			return session;
		} catch ( IOException e ){
		} catch ( ClassNotFoundException e ){
			e.printStackTrace();
		}

		return null;
	}

	public UUID getUserId() {
		return userId;
	}

	public long getCreated() {
		return created;
	}

	public String getUsername() {
		return username;
	}

	public long getExpires() {
		return expires;
	}
}