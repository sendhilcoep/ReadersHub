package com.ideas.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IssueRepo {
	private static final Map<User, List<IssueRequest>> requests = new HashMap<>();

	public void populate(final User user,final IssueRequest request) {
		List<IssueRequest> list = requests.get(user);
		if(list == null)
			list = new ArrayList<IssueRequest>();
		list.add(request);
	}

	public Date returnBook(final Book book, final User user) {
		List<IssueRequest> issues = requests.get(user);
		for(IssueRequest issue : issues){
			if(issue.getBook().equals(book))
				return issue.getIssueDate();
		}
		return null;
	}

	public List<Book> getBooksIssuedBy(final User user) {
		List<IssueRequest> issueRequests = requests.get(user);
		List<Book> books = issueRequests.stream()
								.map((issueRequest)->((Book)issueRequest.getBook()))
								.collect(Collectors.toList());
		return books;
	}

	
}
