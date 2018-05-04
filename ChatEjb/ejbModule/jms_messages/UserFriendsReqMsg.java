package jms_messages;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.Host;
import model.ObjectIdMapping;

public class UserFriendsReqMsg implements Serializable {
	
	private String search;
	
	@JsonSerialize(using = ObjectIdMapping.ObjectIdSerializer.class)
	@JsonDeserialize(using = ObjectIdMapping.ObjectIdDeserializer.class)
	private ObjectId user;
	
	private String addRemove;

	private Host host;
	
	private String sessionId;
	
	private UserFriendsReqMsgType type;

	public UserFriendsReqMsg() {}

	public UserFriendsReqMsg(Host host, String sessionId, UserFriendsReqMsgType type) {
		this.host = host;
		this.sessionId = sessionId;
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

	public String getAddRemove() {
		return addRemove;
	}

	public void setAddRemove(String addRemove) {
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
