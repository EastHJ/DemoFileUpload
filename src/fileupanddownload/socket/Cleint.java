package fileupanddownload.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 此类是客户端，用于提示用户注册，登录，上传文件等操作
 * 
 * @author h
 *
 */

public class Cleint {
	/**
	 * 客户端启动逻辑
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// 创建socket，连接服务器
			Socket socket = new Socket("localhost", 19892);
			// 通过socket获得输出流
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out, "utf-8");
			PrintWriter pw = new PrintWriter(osw, true);
			// 通过socket获得输入流
			InputStream in = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			// 创建控制台输入流从控制台接受信息输入
			Scanner scan = new Scanner(System.in);
			// 定义字符串变量存储服务器信息
			String smsg = null;
			// 定一个客户端变量存储客户端输入的字符
			String cmsg = null;
			// 通过循环与服务器进行交互
			while ((smsg = br.readLine()) != null) {

				if ("@".equals(smsg.substring(0, 1))) {
					System.out.println(smsg.substring(1));
					cmsg = scan.nextLine();
					pw.println(cmsg);
				} else if ("#".equals(smsg.substring(0, 1))) {
					System.out.println(smsg.substring(1));
					break;
				} else {
					System.out.println(smsg);
				}
			}
			scan.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
