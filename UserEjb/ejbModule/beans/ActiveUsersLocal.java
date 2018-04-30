package beans;


import javax.ejb.Local;

import model.User;

@Local
public interface ActiveUsersLocal {
	
	public void initialize();
	public Boolean addUser(User user);
	public Boolean removeUser(User user);
	public Boolean removeUserByUsername(String username);
	
}
