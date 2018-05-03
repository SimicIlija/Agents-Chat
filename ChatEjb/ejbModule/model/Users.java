package model;

import java.util.List;

public class Users {
	
	List<User> listOfUsers;
	
	public Users() {}

	public Users(List<User> listOfUsers) {
		super();
		this.listOfUsers = listOfUsers;
	}

	public List<User> getListOfUsers() {
		return listOfUsers;
	}

	public void setListOfUsers(List<User> listOfUsers) {
		this.listOfUsers = listOfUsers;
	}
	
	
}
