package dataAccess.User;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.User;

@Local
public interface UserServiceLocal {
	
	public User findOne(ObjectId id);
	public User findOne(String username);
	public User add(User input);
	public void edit(User input);
	public List<User> search(String search);
}
