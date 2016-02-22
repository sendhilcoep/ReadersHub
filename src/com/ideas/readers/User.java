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
	
@Override
public boolean equals(Object obj) {
	User otherUser = (User)obj;
	return this.name.equals(otherUser.name)&&this.membership.equals(otherUser.membership);
//	return super.equals(obj);
}
@Override
public int hashCode() {
	return super.hashCode();
}

}
