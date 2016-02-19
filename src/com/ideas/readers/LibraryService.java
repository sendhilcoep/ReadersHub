package com.ideas.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryService {

	LibraryRepo libRepo;
	IssueRepo issueRepo = new IssueRepo();
	
	public LibraryService(LibraryRepo libRepo){
		this.libRepo = libRepo;
	}
	
	public boolean issueBook(User user, Book book) throws EmptyLibraryException {
		boolean bookIssued = libRepo.issueBook(book);
		if(bookIssued) {
			IssueRequest request = new IssueRequest(book, new Date());
			issueRepo.populate(user,request);
		}
		return bookIssued;
		
	}

	public boolean issueBooks(User user, ArrayList<Book> requestList) throws MembershipException, EmptyLibraryException {
		if(!(requestedBooksWithinMembershipLimit(user, requestList.size())
				&&requestedBooksWithinLanguageLimit(requestList)
				&&requestedBooksWithinCategoryNLanguageLimit(requestList)))
			throw new MembershipException();
		
		for(Book book : requestList) {
			if(!libRepo.canIssue(book))
				return false;
			libRepo.issueBook(book);
		}
		return true;
			
	}

	private boolean requestedBooksWithinCategoryNLanguageLimit(ArrayList<Book> requestList) {
		Map<Language,List<Book>> newMap = requestList.stream().collect(Collectors.groupingBy(Book::getLanguage));
		for (Map.Entry<Language, List<Book>> entry : newMap.entrySet()) {
			if(entry.getValue().size()>3)
				return false;
		}
		for (Map.Entry<Language, List<Book>> entry : newMap.entrySet()) {
			Map<Category,List<Book>> categoryMap = entry.getValue().stream().collect(Collectors.groupingBy(Book::getCategory));	
			for (Map.Entry<Category, List<Book>> category : categoryMap.entrySet()) {
				if(category.getValue().size()>3)
					return false;
			}
		}
		return true;
	}

	private boolean requestedBooksWithinLanguageLimit(ArrayList<Book> requestList) {
		Map<Language,List<Book>> newMap = requestList.stream().collect(Collectors.groupingBy(Book::getLanguage));
		for (Map.Entry<Language, List<Book>> entry : newMap.entrySet()) {
			if(entry.getValue().size()>4)
				return false;
		}
		return true;
	}

	private boolean requestedBooksWithinMembershipLimit(User user, int requestSize) {
		return user.getMembership().getMaxBooksHeld() > requestSize;
	}

	public double returnBook(User user, Book book) {
		libRepo.returnBook(book);
		issueRepo.returnBook(book,user);
		return 0;
	}

}
