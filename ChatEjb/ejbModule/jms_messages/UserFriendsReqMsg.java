package jms_messages;

import org.bson.types.ObjectId;

import model.Host;

public class UserFriendsReqMsg {
	
	private String search;
	
	private ObjectId user;
	
	private ObjectId addRemove;

	private Host host;
	
	private UserFriendsReqMsgType type;

	public UserFriendsReqMsg() {}

	public UserFriendsReqMsg(Host host, UserFriendsReqMsgType type) {
		this.host = host;
		this.type = type;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public ObjectId getAddRemove() {
		return addRemove;
	}

	public void setAddRemove(ObjectId addRemove) {
		this.addRemove = addRemove;
	}

	public UserFriendsReqMsgType getType() {
		return type;
	}

	public void setType(UserFriendsReqMsgType type) {
		this.type = type;
	}

	public ObjectId getUser() {
		return user;
	}

	public void setUser(ObjectId user) {
		this.user = user;
	}
	
}
