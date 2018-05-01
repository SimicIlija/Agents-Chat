package beans;

import java.awt.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import jms_messages.LastChatsResMsg;
import jms_messages.MessageReqMsg;
import model.Chat;
import model.Message;

@Stateless
public class DB_Access implements DB_AccessLocal{
	
	//Data before database
	private Chat chat1;
	private Chat chat2;
	private Chat chat3;
	ArrayList<Chat> chats;
	
	@PostConstruct
	public void init() {
		Message m1 = new Message("ddd@ddd.com", (long)1525024791, "first");
		Message m2 = new Message("aaa@aaa.com", (long)1525024795, "second");
		Message m3 = new Message("ddd@ddd.com", (long)1525025799, "third");
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(m1);
		messages.add(m2);
		messages.add(m3);
		ArrayList<String> users = new ArrayList<>();
		users.add("ddd@ddd.com");
		users.add("aaa@aaa.com");
		chat1 = new Chat(users, null, (long)1525024791);
		//chat1.setId(1);
		
		Message m4 = new Message("ddd@ddd.com", (long)1525024791, "first");
		Message m5 = new Message("ooo@ooo.com", (long)1525024795, "second");
		Message m6 = new Message("ddd@ddd.com", (long)1525025799, "third");
		ArrayList<Message> messages1 = new ArrayList<>();
		messages1.add(m4);
		messages1.add(m5);
		messages1.add(m6);
		ArrayList<String> users1 = new ArrayList<>();
		users1.add("ooo@ooo.com");
		users1.add("ddd@ddd.com");
		chat2 = new Chat(users1, null, (long)1525024791);
		//chat2.setId(2);
		
		Message m7 = new Message("ccc@ccc.com", (long)1525024791, "first");
		Message m8 = new Message("eee@eee.com", (long)1525024795, "second");
		Message m9 = new Message("ccc@ccc.com", (long)1525025000, "third");
		ArrayList<Message> messages2 = new ArrayList<>();
		messages2.add(m7);
		messages2.add(m8);
		messages2.add(m9);
		ArrayList<String> users2 = new ArrayList<>();
		users2.add("ccc@ccc.com");
		users2.add("eee@eee.com");
		chat3 = new Chat(users2, null, (long)1525024791);
		//chat3.setId(3);
		
		chats = new ArrayList<>();
		
		chats.add(chat1);
		chats.add(chat2);
		chats.add(chat3);
	}
	
	@Override
	public LastChatsResMsg getLastChats(String username) {
		ArrayList<Chat> ret = new ArrayList<>();
		
		for(Chat chat:chats) {
			for(String user:chat.getUsernames()) {
				if(user.equals(username)) {
					ret.add(chat);
					break;
				}
			}
		}
		LastChatsResMsg retu = new LastChatsResMsg();
		retu.setChats(ret);
		return retu;
	}

	@Override
	public void saveMessage(MessageReqMsg messageReqMsg) {
		for(Chat chat:chats) {
			//if(chat.getId() == messageReqMsg.getChat() ) {
			//	chat.getMessages().add(new Message(messageReqMsg.getSender(), messageReqMsg.getTimeStamp(), messageReqMsg.getContent()));
			//}
			break;
		}
		
	}

	
}
