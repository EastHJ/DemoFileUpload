package fileupanddownload.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import org.junit.Test;

import fileupanddownload.entity.UserFile;
import fileupanddownload.entity.User;

/**
 * 此类为工具类，定义一些数据库增.删.改.查的操作
 * 
 * @author h
 *
 */
public class DBFileDAO {
	/**
	 * 此方法用于用户注册
	 * @param user
	 */
	public void createUser(User user) {
		Connection con = null;
		try {
			// 创建连接
			con = DBUtil.getConnection();
			// 创建sql语句
			String sql = "insert into tb_user values(tb_user_sq.nextval,?,?)";
			// 创建preparedstatement语句发送sql语句
			PreparedStatement ps = con.prepareStatement(sql);
			// 设置用户名和密码
			ps.setString(1, user.getUser_name());
			ps.setString(2, user.getUser_password());
//			System.out.println(user.getUser_name());
//			System.out.println("create" + sql);
			// 发送sql
			int i = ps.executeUpdate();
			if (i > 0) {
				System.out.println("用户注册成功！");
			}
		} catch (SQLException e) {
			System.out.println("用户注册失败");
			e.printStackTrace();
		} finally {
			DBUtil.close(con);
		}

	}

	/*
	 * 数据库中，用户名具有唯一性，通过传参查询的用户可返一个已存在的用户，或空值
	 * 此方法用于，获取数据库中的用户
	 */
	public User queryUser(User user) {
		Connection con = null;
		User qeUser = new User();
		try {

			con = DBUtil.getConnection();
			String sql = "select * from tb_user where user_name = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUser_name());
		//	System.out.println("query" + sql+user.getUser_name());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				qeUser.setId(rs.getInt("id"));
				//System.out.println(rs.getInt("id"));
				qeUser.setUser_name(rs.getString("user_name"));
			//	System.out.println(rs.getString("user_name"));
				qeUser.setUser_password(rs.getString("user_password"));
			//	System.out.println(rs.getString("user_password"));
			}
			
			return qeUser;
		} catch (SQLException e) {
			System.out.println("查询用户失败！");
			e.printStackTrace();
			return null;
		}finally{
			DBUtil.close(con);
		}
		//return null;
	}
/*
 * 此方法用于将文件存入到数据库中
 */
	public boolean addFileRecoad(User user, UserFile file) {
		Connection con = null;
		boolean autoCommit = false;
		FileInputStream fis = null;
		boolean flag = false;
		try {
			con = DBUtil.getConnection();
			// 将事务改为手动提交
			autoCommit = con.getAutoCommit();
			con.setAutoCommit(false);
			// 先在数据库里创建一条记录并初始化用empty_blob（）；
			String sql = "insert into tb_file values(tb_file_sq.nextval,?,?,empty_blob())";
			// 插入文件记录并获得主键
			PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
			ps.setInt(1, user.getId());
			ps.setString(2, file.getFile_name());
			// i!=0说明插入成功
			int i = ps.executeUpdate();
			if (i > 0) {
				// System.out.println("插入成功");
				// 获取刚刚插入文件行的ID
				ResultSet rs = ps.getGeneratedKeys();
				Integer fileId = 0;
				if (rs.next()) {
					fileId = rs.getInt(1);
				}
				// 清除ps参数重新编译sql定位到刚插入的文件
				ps.clearParameters();
				// 使用for update 锁定到数据行
				sql = "select user_file from tb_file where id =? for update";
				ps = con.prepareStatement(sql);
				ps.setInt(1, fileId);
				rs = ps.executeQuery();
				// 根据刚刚获得的结果集更新数据，将文件写入
				if (rs.next()) {
					// 得到java.sql.Blob对象，然后Cast为oracle.sql.BLOB
					oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob("user_file");
					// 获数据库的输出流
					OutputStream out = blob.setBinaryStream(1);
					// 获得文件输入流，通过File实例传入文件路径
					fis = new FileInputStream(new File(file.getFile_src()));
					// 将输入流写入到输出流
					byte[] b = new byte[blob.getBufferSize()];
					int len = 0;
					while ((len = fis.read(b)) != -1) {
						out.write(b, 0, len);
					}
					// 将最后一次的数据强制写出
					out.flush();
				}
				// 结束后提交事务
				con.commit();
				flag= true;
			} else {
				// 插入新文件记录不成功则回滚
				con.rollback();
			}

		} catch (Exception e) {
			try {
				// 出现异常回滚
				con.rollback();
				flag = false;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		//	System.out.println("上传文件失败！");
			e.printStackTrace();
			//throw new RuntimeException("上传文件失败！",e);
			
		} finally {
			// 最会关闭流，将事务提交方式还原
			try {
				fis.close();
				con.setAutoCommit(autoCommit);
				DBUtil.close(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 测试此类方法
//	 */
//	@Test
//	public void test() {
//		User user = new User();
//		user.setId(1);
//		user.setUser_name("小夏");
//		user.setUser_password("12345");
//		DBFileDAO dao = new DBFileDAO();
//		// dao.createUser(user);
//		// User u = dao.queryUser(user);
//		// System.out.println(u.getUser_password());
//		UserFile file = new UserFile();
//		file.setFile_name("1.jpg");
//		file.setFile_src("F:/UserData/My Documents/My Pictures/1.jpg");
//		dao.addFileRecoad(user, file);
//	}

}
