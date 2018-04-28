package model;

public class User {

	private String email;
	private String password;
	private Host host;
	
	public User() {}
	
	public User(String username, String password, Host host) {
		super();
		this.email = username;
		this.password = password;
		this.host = host;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	
	
}
