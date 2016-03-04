package com.ideas.readers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IssueRepo {
	private static Map<User, List<IssueRequest>> requests = null;

	public IssueRepo() {
		requests = new HashMap<>();
	}

	public void populate(final User user, final IssueRequest request) {
		if (!requests.containsKey(user)) {
			List newList = new ArrayList();
			newList.add(request);
			requests.put(user, newList);
			return;
		}
		List<IssueRequest> list = requests.get(user);
		if (list == null){
			requests.put(user,  Arrays.asList(request));
		}
		else{
			list.add(request);
		}
	}

	public LocalDateTime returnBook(final Book book, final User user) {
		List<IssueRequest> issues = requests.get(user);
		IssueRequest issue = issues.stream().filter(b->b.getBook().equals(book)).findAny().orElse(null);
		if(issue!=null){
			issues.remove(issue);
			return issue.getIssueDate();
		}
		return null;
	}
	public LocalDateTime returnBookIssueDate(final Book book, final User user) {
		List<IssueRequest> issues = requests.get(user);
		return issues.stream().filter(b->b.getBook().equals(book)).map(b->b.getIssueDate()).findAny().orElse(null);
	}
	public List<Book> getBooksIssuedBy(final User user) {
		List<IssueRequest> issueRequests = requests.get(user);
		List<Book> books = issueRequests.stream().map((issueRequest) -> ((Book) issueRequest.getBook()))
				.collect(Collectors.toList());
		return books;
	}

	public int getNoOfBooksIssuedBy(User user) {
		List<IssueRequest> issueRequests = requests.get(user);
		if(null != issueRequests)return issueRequests.size();
		return 0;
	}

}
