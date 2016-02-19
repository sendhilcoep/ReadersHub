package com.ideas.readers;

public class User {

	private String name;
	private Membership membership;

	public User(String name, Membership membership) {
		this.name = name;
		this.membership = membership;
	}

	public Membership getMembership() {
		return membership;
	}

}
