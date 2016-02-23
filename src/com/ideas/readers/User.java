package com.ideas.readers;

import java.util.Objects;

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
	if(this==obj)return true;
	if(obj==null)return false;
	if(getClass() != obj.getClass())return false;
	User otherUser = (User)obj;
	return this.name.equals(otherUser.name)&&this.membership.equals(otherUser.membership);
}
@Override
public int hashCode() {
	 int hash = 7;
	 	        hash = 83 * hash + Objects.hashCode(this.name);
	 	        hash = 83 * hash + Objects.hashCode(this.membership);
	 	        return hash;
}

}
