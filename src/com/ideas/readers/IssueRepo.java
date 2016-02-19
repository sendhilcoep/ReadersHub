package com.ideas.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class IssueRepo {
	private static Map<User, List<IssueRequest>> requests = new HashMap<>();

	public void populate(User user, IssueRequest request) {
		List<IssueRequest> list = requests.get(user);
		if(list == null)
			list = new ArrayList<IssueRequest>();
		list.add(request);
	}

	public Date returnBook(Book book, User user) {
		List<IssueRequest> issues = requests.get(user);
		for(IssueRequest issue : issues){
			if(issue.getBook().equals(book))
				return issue.getIssueDate();
		}
		return null;
	}

	
}
