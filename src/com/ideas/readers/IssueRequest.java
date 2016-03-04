package com.ideas.readers;

import java.time.LocalDateTime;
import java.util.Date;

public class IssueRequest {

	private LocalDateTime issueDate;
	private Book book;

	public IssueRequest(Book book, LocalDateTime issueDate) {
		this.book = book;
		this.issueDate = issueDate;
	}

	public IssueRequest(IssueRequest request) {
		this.book = request.book;
		this.issueDate = request.issueDate;
	}

	public Object getBook() {
		return book;
	}

	public LocalDateTime getIssueDate() {
		return issueDate;
	}

}
