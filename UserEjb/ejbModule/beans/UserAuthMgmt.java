package beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import dataAccess.User.UserServiceLocal;
import exceptions.UserAuthException;
import jms_messages.UserAuth.UserAuthResMsgType;
import model.Host;
import model.User;

@Stateless
public class UserAuthMgmt implements UserAuthMgmtLocal {
	
	@EJB
	ActiveUsersLocal activeUSers;
	
	@EJB
	UserServiceLocal userService;
	

	@Override
	public User register(User user) throws UserAuthException {
		if(user.getUsername() == null || user.getUsername().trim().isEmpty())
			throw new UserAuthException(UserAuthResMsgType.REQUIRED_FIELD_EMPTY);
		if(user.getPassword() == null || user.getPassword().trim().isEmpty() || user.getPassword().length() < 6)
			throw new UserAuthException(UserAuthResMsgType.REQUIRED_FIELD_EMPTY);
		//TODO proveriti ostala polja
		user = userService.add(user);
		if(user == null)
			throw new UserAuthException(UserAuthResMsgType.USERNAME_EXISTS);
		return user;
	}

	@Override
	public User logIn(User user, Host host) throws UserAuthException {
		User userDb = userService.findOne(user.getUsername());
		if(userDb == null || !userDb.getPassword().equals(user.getPassword()))
			throw new UserAuthException(UserAuthResMsgType.INVALID_CREDENTIALS);
		
		// Host je vec podesen
		userDb.setHost(user.getHost());
		if(activeUSers.addUser(userDb))
			return userDb;
		return null;
	}

	@Override
	public boolean logOut(User user) {
		return activeUSers.removeUser(user);
	}

}
