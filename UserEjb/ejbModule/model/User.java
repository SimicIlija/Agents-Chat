package model;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User extends BaseDO {
	
	@Indexed(unique=true)
	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	@JsonIgnore
	@Reference
	private List<User> friends;
	
	@Reference
	private List<User> friendReq;
	
	@NotSaved
	@Transient
	private Host host;
	
	public User() {}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.friends = new ArrayList<>();
		this.friendReq = new ArrayList<>();
	}
	
	public User(String username, String password, String firstName, String lastName, Host host) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.host = host;
		this.friends = new ArrayList<>();
		this.friendReq = new ArrayList<>();
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public List<User> getFriendReq() {
		return friendReq;
	}

	public void setFriendReq(List<User> friendReq) {
		this.friendReq = friendReq;
	}
	
}
