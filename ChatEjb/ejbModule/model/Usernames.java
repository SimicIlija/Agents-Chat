package model;

import java.util.List;

public class Usernames {
	List<String> usernames;

	public Usernames() {}
	
	public Usernames(List<String> usernames) {
		super();
		this.usernames = usernames;
	}

	public List<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
	

}
