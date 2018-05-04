package beans;



import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import dataAccess.MongoConnection;
import dataAccess.ChatMessage.ChatMessageServiceLocal;
import dataAccess.User.UserServiceLocal;
import model.Chat;
import model.Message;
import model.User;

@Singleton
@Startup
public class Test {
	
	@EJB
	MongoConnection conn;
	
	@EJB
	UserServiceLocal userService;
	
	@EJB
	ChatMessageServiceLocal chatMessageService;
	
	@EJB
	GroupMgmtLocal group;

	@PostConstruct
	public void init() {
		/////////////////////////////////////////////////////////
		conn.getDatastore().getCollection(User.class).drop();
		conn.getDatastore().getCollection(Chat.class).drop();
		conn.getDatastore().getCollection(Message.class).drop();
		////////////////////////////////////////////////////////
		
		User u1 = new User("proba", "probaproba", "Proba", "Probic", null);
		User u2 = new User("test", "testtest", "Test", "Testic", null);
		User u3 = new User("tri", "tritri", "Trit", "Tritic", null);
		u1 = userService.add(u1);
		userService.add(u2);
		userService.add(u3);
		
		group.createNew("cet", "proba", "test,tri");
		
		
		
		
//		ArrayList<String> users = new ArrayList<>();
//		users.add(u1.getUsername());
//		users.add(u2.getUsername());
//		
//		Chat chat1 = chatMessageService.creteChat(new Chat(users, null, (long)1525024791));
//		chatMessageService.saveMessage(chat1.getId(), new Message("test", (long)1525024791, "first"));
//		chatMessageService.saveMessage(chat1.getId(), new Message("proba", (long)1525024791, "second"));
//		chatMessageService.saveMessage(chat1.getId(), new Message("test", (long)1525024791, "third"));
	}
}
