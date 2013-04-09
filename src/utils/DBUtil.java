package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Properties;

public class DBUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DBUtil db1 = new DBUtil();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = "";
		try {
			while ((s = br.readLine()) != null) {
				if (s.equals("#"))
					break;
				System.out.println(s);
				if (s.toLowerCase().startsWith("select")) {
					ResultSet rs = db1.executeQuerySQL(s);
					while (rs.next()) {
						System.out.println(rs.getString(1));
					}
				} else {
					int r = db1.executeUpdateSQL(s);
					System.out.println("rows affected:" + r);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		db1.rundown();
	}

	void createUserTable() {

	}

	void test() {

	}

	static Connection _conn = null;



	String _url = "";
	String _user = "";
	String _pwd = "";

	public void createConn() {
		try {
			if (_conn != null) {
				try {
					_conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			System.err.println("% creating connection...");
			Properties props = new Properties();
			props.setProperty("user", _user);
			props.setProperty("password", _pwd);
			// props.setProperty("ssl","true");
			_conn = DriverManager.getConnection(_url+_database, props);
			_conn.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private static String _database = "emu";

	public static void setDatabase(String s) {
		_database = s;
	}

	public DBUtil() {		
		String postgresUrl = "jdbc:postgresql://localhost:5432/";		
		String postgresClassName = "org.postgresql.Driver";
		String database = "revtxt";
		String user = "postgres";
		String pwd = "multimodal";
		init(postgresUrl, database, postgresClassName, user, pwd);
	}
	
	public DBUtil(String url, String database, String driverClassName, String user, String pwd) {
		init(url, database, driverClassName, user, pwd);
	}
	
	public void init(String url, String database, String driverClassName, String user, String pwd) {
		_database = database;
		_url = url;
		String fullUrl = _url + _database;
		_user = user;
		_pwd = pwd;
		
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		// TODO: create a regular user!

		try {
			if (_conn == null || _conn.isClosed()) {
				System.err.println("%connecting to " + fullUrl + "...");
				createConn();
				// conn.setAutoCommit(false);
			}
			
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void commit() {
		try {
			_conn.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void rundown() {
		System.out.println("% database rundown.");
		try {
			
			_conn.commit();
			_conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public boolean execute(String sql) {
		validateConn();
		try {
			Statement stmt = _conn.createStatement();
			return stmt.execute(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public ResultSet executeQuerySQL(String sql) {
		validateConn();
		try {
			Statement stmt = _conn.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public int executeUpdateSQL(String sql) {
		validateConn();
		try {
			Statement stmt = _conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	void validateConn() {
		try {
			if (_conn == null) {
				System.err.println("creating connection...");
				Properties props = new Properties();
				props.setProperty("user", _user);
				props.setProperty("password", _pwd);
				// props.setProperty("ssl","true");
				_conn = DriverManager.getConnection(_url, props);
				// conn.setAutoCommit(false);
			}

			if (_conn.isClosed()) {
				System.err.println("creating connection...");
				Properties props = new Properties();
				props.setProperty("user", _user);
				props.setProperty("password", _pwd);
				// props.setProperty("ssl","true");
				_conn = DriverManager.getConnection(_url, props);
				// conn.setAutoCommit(false);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	boolean checkIfPageExists(String page_sha1) {
		validateConn();
		ResultSet rs = executeQuerySQL("select * from emu_pages where page_sha1='"
				+ page_sha1 + "'");
		// System.out.println("select * from libx_pages where id='" + id + "'");
		// ResultSet rs = executeQuerySQL("select * from libx_pages");
		try {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	String _sql_insert_page = 
		"insert into emu_pages (" 
		+ "page_sha1, " 
		+ "time, "
		+ "wid, "
		+ "tab, " 
		+ "type, " 
		+ "url, " 
		+ "ref, "		
		+ "ip, "
		+ "user_id, "
		+ "is_search, "
		+ "engine, "
		+ "vertical "
		+ ") values (?,?,?,?,?,?,?,?,?,?,?,?)";



	String _sql_insert_event = 
		"insert into emu_events (" 
		+ "event_sha1, "
		+ "time, "
		+ "ev, "
		+ "url, "
		+ "ref, "
		+ "ip, "
		+ "user_id, " 
		+ "content_sha1, "
		+ "cx, "
		+ "cy, " 
		+ "x, " 
		+ "y, " 
		+ "scrlX, " 
		+ "scrlY, "
		+ "iw, " 
		+ "ih, " 
		+ "ow, " 
		+ "oh, " 
		+ "scrlW, "
		+ "scrlH, " 
		+ "dom_path, " 
		+ "targ_id, "
		+ "is_doc_area, " 
		+ "duration, " 
		+ "select_text, " 
		+ "button "
		+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	boolean checkIfEventExists(String event_sha1) {
		validateConn();
		ResultSet rs = executeQuerySQL("select * from emu_events where event_sha1='"
				+ event_sha1 + "'");
		// System.out.println("select * from libx_pages where id='" + id + "'");
		// ResultSet rs = executeQuerySQL("select * from libx_pages");
		try {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	

	
	String _sql_insert_mouse = 
		"insert into emu_mouses (" 
		+ "event_id, "
		+ "time, "
		+ "ev, "
		+ "page_id, "
		+ "ip, "
		+ "user_id, " 
		+ "x, " 
		+ "y, " 
		+ "xdist, " 
		+ "ydist, "
		+ "is_serp, "
		+ "res_rand, "
		+ "task, "
		+ "founded, "
		+ "has_image, "
		+ "has_fixation "
		+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	boolean checkIfMouseExists(int event_id) {
		validateConn();
		ResultSet rs = executeQuerySQL("select * from emu_mouses where event_id='"
				+ event_id + "'");
		// System.out.println("select * from libx_pages where id='" + id + "'");
		// ResultSet rs = executeQuerySQL("select * from libx_pages");
		try {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	

	public static void stmtSetInt(PreparedStatement stmt, int index, Integer val) throws SQLException {
		if (val != null) {
			stmt.setInt(index, val);
		} else {
			stmt.setNull(index, Types.INTEGER);
		}
	}
	
	public static void stmtSetBoolean(PreparedStatement stmt, int index, Boolean val) throws SQLException {
		if (val != null) {
			stmt.setBoolean(index, val);
		} else {
			stmt.setNull(index, Types.BOOLEAN);
		}
	}
	
	public static void stmtSetString(PreparedStatement stmt, int index, String val) throws SQLException {
		stmt.setString(index, val);
	}
	
	String _sql_insert_content = 
		"insert into emu_contents (" 
		+ "content_sha1, "
//		+ "time, "
//		+ "url, "		
		+ "data, "
//		+ "type, "
		+ "length "
		+ ") values (?,?,?)";

	boolean checkIfContentExists(String content_sha1) {
		validateConn();
		ResultSet rs = executeQuerySQL("select * from emu_contents where content_sha1='"
				+ content_sha1 + "'");
		// System.out.println("select * from libx_pages where id='" + id + "'");
		// ResultSet rs = executeQuerySQL("select * from libx_pages");
		try {
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}


}