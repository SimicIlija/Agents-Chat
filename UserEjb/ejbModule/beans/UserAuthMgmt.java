package beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import exceptions.UserAuthException;
import model.Host;
import model.User;

@Stateless
public class UserAuthMgmt implements UserAuthMgmtLocal {
	
	@EJB
	ActiveUsers activeUSers;

	@Override
	public User register(User user) throws UserAuthException {
		// TODO logika registracije
		return user;
	}

	@Override
	public User logIn(User user, Host host) throws UserAuthException {
		// TODO login logika
		user.setHost(host);
		
		if(activeUSers.addUser(user))
			return user;
		return null;
	}

	@Override
	public boolean logOut(String username) {
		return activeUSers.removeUserByUsername(username);

	}

}
