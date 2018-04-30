package cluster;

import java.util.List;

import javax.ejb.Local;

import model.User;

@Local
public interface UserClusterManagerLocal {

	public void addUserToActiveList(User user);
	
	public List<User> getAllActiveUsers();

}
