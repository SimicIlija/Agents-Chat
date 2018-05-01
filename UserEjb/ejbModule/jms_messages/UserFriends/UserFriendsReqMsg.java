package jms_messages.UserFriends;

import org.bson.types.ObjectId;

import model.Host;

public class UserFriendsReqMsg {
	
	private String search;
	
	private ObjectId addRemove;

	private Host host;
	
	private UserFriendsReqMsgType type;

	public UserFriendsReqMsg() {}

	public UserFriendsReqMsg(String search, Host host, UserFriendsReqMsgType type) {
		this.search = search;
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

}
