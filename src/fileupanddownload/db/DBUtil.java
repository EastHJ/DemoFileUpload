package fileupanddownload.db;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

/**
 * 数据库连接类，用来连接数据库
 * 
 * @author h
 *
 */
public class DBUtil {
	private static String url;
	private static String user;
	private static String password;
	private static String driver;
	// 使用静态代码块加载数据库信息/SocketDemoFileUpload/src/fileupanddownload/db/DBFileDAO.java
	/// SocketDemoFileUpload/source/db.properties
	/// SocketDemoFileUpload/src/db.properties

	static {
		try {
			// 1.通过外置文件获取数据库配置/SocketDemoFileUpdate/src/fileupdate/db/DBUtil.java
			Properties ps = new Properties();
			InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
			System.out.println(in);
			ps.load(in);
			url = ps.getProperty("url");
			user = ps.getProperty("user");
			password = ps.getProperty("password");
			driver = ps.getProperty("driver");
			// 2.通过反射加在数据库驱动
			Class.forName(driver);
		} catch (IOException e) {
			System.out.println("数据库配置文件加载失败！");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("数据库驱动加载失败！");
			e.printStackTrace();
		}
	}

	// 获取数据库连接
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	// 关闭数据库连接
	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("数据库连接关闭失败！");
			e.printStackTrace();

		}
	}

	// /**
	// * 测试DBUtil配置是否正确
	// * @throws SQLException
	// */
//	public static void main(String[] args) throws SQLException {
//		// 获取连接
//		URL path = Thread.currentThread().getContextClassLoader().getResource("");
//		System.out.println(path);
//		Connection con = DBUtil.getConnection();
//		String sql = "select * from emp";
//		// 获取statement发送sql
//		Statement stm = con.createStatement();
//		// 查寻结果返回结果集
//		ResultSet rs = stm.executeQuery(sql);
//		// 遍历结果集查看信息
//		while (rs.next()) {
//			System.out.println(rs.getInt("empno") + rs.getString("ename"));
//		}
//		// 关闭连接
//		DBUtil.close(con);
//	}

}
