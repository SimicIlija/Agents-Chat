package beans;

import javax.ejb.Local;

import exceptions.UserAuthException;
import model.Host;
import model.User;

@Local
public interface UserAuthMgmtLocal {
	
	public User register(User user) throws UserAuthException;
	public User logIn(User user, Host host) throws UserAuthException;
	public boolean logOut(User user);

}
