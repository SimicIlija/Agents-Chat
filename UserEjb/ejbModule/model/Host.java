package model;

public class Host {

	public String address;
	public String name;
	public int port;
	
	public Host() {}
	public Host(String address, String name, int port) {
		super();
		this.address = address;
		this.name = name;
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
