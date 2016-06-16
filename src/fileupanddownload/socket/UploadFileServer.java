package fileupanddownload.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import fileupanddownload.db.DBFileDAO;
import fileupanddownload.entity.User;
import fileupanddownload.entity.UserFile;

/**
 * 此类是文件上传服务端，用来接受客户端的信息，并连数据库操作
 * 
 * @author h
 *
 */

public class UploadFileServer {
	private ServerSocket server;

	// 构造器初始化server
	public UploadFileServer() throws IOException {
		this.server = new ServerSocket(19892);
	}

	/*
	 * 启动服务端
	 */

	public static void main(String[] args) {

		try {
			UploadFileServer ufs = new UploadFileServer();
			ufs.star();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 该方法监听客户端连接，有客户端连接创建新线程，处理任务后继续监听
	 * 
	 * @throws IOException
	 */
	public void star() throws IOException {
		System.out.println("服务器已启动，等待客户连接·······");
		while (true) {
			// 调用accept（）方法监听客户端连接
			Socket socket = server.accept();
			System.out.println("客户端已连接，等待用户输入·····");
			ClientHander hander = new ClientHander(socket);
			Thread t = new Thread(hander);
			t.start();
		}
	}

	/**
	 * 此方法用于用户注册
	 * 
	 * @param pw
	 * @param br
	 * @return Integer
	 * @throws IOException
	 */
	private synchronized boolean createUser(PrintWriter pw, BufferedReader br) throws IOException {
		User user = new User();
		// 创建数据库工具类实例，准备对数据库操作
		DBFileDAO dao = new DBFileDAO();
		pw.println("注册\n@请输入用户名:");
		user.setUser_name(br.readLine().trim());
		pw.println("@请输入密码：");
		String pwd1 = br.readLine().trim();
		pw.println("@请再次输入密码：");
		String pwd2 = br.readLine().trim();
		if (pwd1.equals(pwd2)) {
			user.setUser_password(pwd1);
		} else {
			pw.println("两次输入的密码不一致，请重新输入！");
			return false;
		}
		User us = dao.queryUser(user);
		// System.out.println(us);
		if (user.getUser_name().equals(us.getUser_name())) {
			pw.println("用户名：" + user.getUser_name() + "已被占用,请重新输入！");
			return false;
		} else {
			// 调用dao.createUser,将数据存入数据库
			dao.createUser(user);
			pw.println("注册成功，请登录！\n--------------------------");
			return true;
		}

	}

	/**
	 * 此方方用来验证用户登录
	 * 
	 * @param pw
	 * @param br
	 * @return User
	 * @throws IOException
	 */
	private synchronized User login(PrintWriter pw, BufferedReader br) throws IOException {
		User user = new User();
		// 创建数据库工具类实例，准备对数据库操作
		DBFileDAO dao = new DBFileDAO();
		pw.println("登录\n@请输入用户名：");
		String str = br.readLine().trim();
		user.setUser_name(str);
		pw.println("@请输入密码：");
		str = br.readLine().trim();
		user.setUser_password(str);
		// 调用user的equals方法判断当前用户是否正确登录
		User us = dao.queryUser(user);
//		System.out.println(user);
//		System.out.println(us);
		if (!user.equals(us)) {
			pw.println("登录失败，用户名或密码不正确!");
			return null;
		} else {
			pw.println("登录成功!");
			return us;
		}
	}

	/**
	 * 此方法用于获取文件路径，上传文件
	 * 
	 * @param pw
	 * @param br
	 * @param user
	 * @return
	 * @throws IOException
	 */
	private synchronized boolean upload(PrintWriter pw, BufferedReader br, User user) throws IOException {
		// 创建数据库工具类实例，准备对数据库操作
		DBFileDAO dao = new DBFileDAO();
		UserFile uf = new UserFile();
		pw.println("上传文件\n@请输入要上传文件的绝对路径：(例如：e:/image/1.jpg)");
		String str = br.readLine().trim();
		// System.out.println("输入的文件路径：" + str);
		uf.setFile_src(str);
		// 获取文件名
		String[] ts = str.split("/");
		uf.setFile_name(ts[ts.length - 1]);
		// 输出测试
		// System.out.println("获取 的文件名" + uf.getFile_name());
		// 调用dao中方法，上传文件
		if (dao.addFileRecoad(user, uf)) {
			pw.println("#文件上传成功！再见！！");
			return true;
		} else {
			pw.println("文件上传失败,若要继续上传请重新登录！");
			return false;
		}
	}

	/**
	 * 此类为私有线程类处理客户端的业务逻辑
	 * 
	 * @author h
	 *
	 */
	private class ClientHander implements Runnable {
		private Socket socket = null;

		public ClientHander(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				// 获得输入流，接受客户端信息,字符集使用utf-8
				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				// 获得输出流，提示用户输入，字符集使用utf-8
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out, "utf-8");
				PrintWriter pw = new PrintWriter(osw, true);

				// 以下定义交互逻辑
				pw.println("------欢迎使用文件上传器！------\n1.注册新用户\n2.登录 \n3.退出\n---------------------\n@请选择：");
				Integer flag = null;
				User user = null;
				while (true) {
					if (flag == null) {
						try {
							flag = Integer.parseInt(br.readLine().trim());
						} catch (Exception e) {
							pw.println("输入错误！请输入1-3之间的数字！\n@请重新输入");
							flag = null;
							continue;
						}
					}
					if (1 == flag && createUser(pw, br)) {
						flag = 2;
					} else if (2 == flag && null != (user=login(pw, br))&&upload(pw, br, user)) {
						break;
					} else if (3 == flag) {
						pw.println("#您已成功退出文件上传器，再见！");
						break;
					} else {
						pw.println("--------------------------\n1.重新注册\n2.重新登录\n3.退出\n@请选择：");
						flag = null;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
