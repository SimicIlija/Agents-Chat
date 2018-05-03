package cluster;

import java.util.List;

import javax.ejb.Local;

import model.User;

@Local
public interface UserClusterManagerLocal {

	public boolean addUserToActiveList(User user);
	
	public boolean removeUserFroActiveList(User user);

	public List<User> getAllActiveUsers();

}
