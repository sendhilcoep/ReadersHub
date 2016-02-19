package com.ideas.readers;

import java.util.Date;

public class IssueRequest {

	private Date issueDate;
	private Book book;

	public IssueRequest(Book book, Date issueDate) {
		this.book = book;
		this.issueDate = issueDate;
		// TODO Auto-generated constructor stub
	}

	public Object getBook() {
		return book;
	}

	public Date getIssueDate() {
		return issueDate;
	}

}
