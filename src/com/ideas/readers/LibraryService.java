package com.ideas.readers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LibraryService {

	LibraryRepo libRepo;
	IssueRepo issueRepo = new IssueRepo();
	
	public LibraryService(LibraryRepo libRepo){
		this.libRepo = libRepo;
	}
	
	public boolean issueBook(final User user, final Book book) throws EmptyLibraryException {
		boolean bookIssued = libRepo.issueBook(book);
		if(bookIssued) {
			IssueRequest request = new IssueRequest(book, new Date());
			issueRepo.populate(user,request);
		}
		return bookIssued;
		
	}

	public boolean issueBooks(final User user, final ArrayList<Book> requestList) throws MembershipException, EmptyLibraryException {
		if(!(requestedBooksWithinMembershipLimit(user, requestList.size())
				&&requestedBooksWithinLanguageLimit(requestList)
				&&requestedBooksWithinCategoryNLanguageLimit(requestList)))
			throw new MembershipException();
		
		for(Book book : requestList) {
			if(!libRepo.canIssue(book))
				return false;
			issueBook(user,book);
			libRepo.issueBook(book);
		}
		return true;
			
	}

	private boolean requestedBooksWithinCategoryNLanguageLimit(ArrayList<Book> requestList) {
		Map<Language,List<Book>> newMap = requestList.stream().collect(Collectors.groupingBy(Book::getLanguage));
		Predicate<? super Entry<Language, List<Book>>> moreThan3BooksOfSameLanguage = e->e.getValue().size()>3;
		Predicate<? super Entry<Category, List<Book>>> moreThan3BooksOfSameCategory = f->f.getValue().size()>3;
		return newMap.entrySet().stream().filter(moreThan3BooksOfSameLanguage)
				.map(e->e.getValue().stream().collect(Collectors.groupingBy(Book::getCategory)))
				.sequential().noneMatch(e->e.entrySet().stream().anyMatch(moreThan3BooksOfSameCategory));
	}

	private boolean requestedBooksWithinLanguageLimit(ArrayList<Book> requestList) {
		return requestList.stream().collect(Collectors.groupingBy(Book::getLanguage)).values().stream().noneMatch(e->e.size()>4);
	}

	private boolean requestedBooksWithinMembershipLimit(User user, int requestSize) {
		return user.getMembership().getMaxBooksHeld() > requestSize;
	}

	public double returnBook(User user, Book book) {
		libRepo.returnBook(book);
		issueRepo.returnBook(book,user);
		return 0;
	}

	public List<Book> getBooksIssuedBy(final User user) {
		return issueRepo.getBooksIssuedBy(user);
	}

}
