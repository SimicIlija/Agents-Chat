package model;

import java.util.List;

public class Hosts {

	private List<Host> listOfHosts;

	public Hosts() {}
	public Hosts(List<Host> listOfHosts) {
		super();
		this.listOfHosts = listOfHosts;
	}

	public List<Host> getListOfHosts() {
		return listOfHosts;
	}

	public void setListOfHosts(List<Host> listOfHosts) {
		this.listOfHosts = listOfHosts;
	}
	
	
}
