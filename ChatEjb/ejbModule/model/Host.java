package model;

public class Host {

	public String address;
	public String name;
	
	public Host() {}
	public Host(String address, String name) {
		super();
		this.address = address;
		this.name = name;
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
