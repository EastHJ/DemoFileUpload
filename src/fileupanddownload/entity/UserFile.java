package fileupanddownload.entity;

//import java.util.Date;

/**
 * 文件类，用于存放上传问价的信息
 * @author h
 *
 */
public class UserFile {
	//文件名
	private String file_name;
	//文件绝对路径
	private String file_src;
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		//Long time = new Date().getTime();
		this.file_name = file_name;//+time.toString();
	}
	public String getFile_src() {
		return file_src;
	}
	public void setFile_src(String file_src) {
		this.file_src = file_src;
	}
	@Override
	public String toString() {
		return "文件名：" + file_name + "\t文件绝对路径：" + file_src ;
	}
	

}
