package fileupanddownload.entity;

/**
 * 用户，用于创建用户实例，进行注册，校验登录信息
 * 
 * @author h
 *
 */
public class User {
	// 用户
	private Integer id;
	// 用户名
	private String user_name;
	// 用户密码
	private String user_password;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name =user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user_name=" + user_name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user_name == null) ? 0 : user_name.hashCode());
		result = prime * result + ((user_password == null) ? 0 : user_password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (user_name == null) {
			if (other.user_name != null)
				return false;
		} else if (!user_name.equals(other.user_name))
			return false;
		if (user_password == null) {
			if (other.user_password != null)
				return false;
		} else if (!user_password.trim().equals(other.user_password.trim()))
			return false;
		return true;
	}

	
 
}
