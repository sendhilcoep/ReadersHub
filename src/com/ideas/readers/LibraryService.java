package com.ideas.readers;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ideas.readers.exceptions.EmptyLibraryException;
import com.ideas.readers.exceptions.MembershipException;

public class LibraryService {

	LibraryRepo libRepo;
	IssueRepo issueRepo = new IssueRepo();
	
	public LibraryService(LibraryRepo libRepo){
		this.libRepo = libRepo;
	}
	
	public boolean issueBook(final User user, final Book book) throws EmptyLibraryException {
		boolean bookIssued = libRepo.issueBook(book);
		if(bookIssued) {
			IssueRequest request = new IssueRequest(book, LocalDateTime.now());
			issueRepo.populate(user,request);
		}
		return bookIssued;
		
	}

	public boolean issueBooks(final User user, final List<Book> list) throws MembershipException, EmptyLibraryException {
		if(!(requestedBooksWithinMembershipLimit(user, list.size())&&requestedBooksWithinLanguageLimit(list)
				&&requestedBooksWithinCategoryNLanguageLimit(list)))
			throw new MembershipException();
		
		for(Book book : list) {
			if(!libRepo.canIssue(book))
				return false;
			issueBook(user,book);
			libRepo.issueBook(book);
		}
		return true;
			
	}

	private boolean requestedBooksWithinCategoryNLanguageLimit(List<Book> list) {
		Map<Language,List<Book>> newMap = list.stream().collect(Collectors.groupingBy(Book::getLanguage));
		Predicate<? super Entry<Language, List<Book>>> moreThan3BooksOfSameLanguage = e->e.getValue().size()>3;
		Predicate<? super Entry<Category, List<Book>>> moreThan3BooksOfSameCategory = f->f.getValue().size()>3;
		return newMap.entrySet().stream().filter(moreThan3BooksOfSameLanguage)
				.map(e->e.getValue().stream().collect(Collectors.groupingBy(Book::getCategory)))
				.sequential().noneMatch(e->e.entrySet().stream().anyMatch(moreThan3BooksOfSameCategory));
	}

	public boolean requestedBooksWithinLanguageLimit(List<Book> list) {
		return list.stream().collect(Collectors.groupingBy(Book::getLanguage)).values().stream().filter(n->n.size()>0).noneMatch(e->e.size()>4);
	}

	private boolean requestedBooksWithinMembershipLimit(User user, int requestSize) {
		return user.getMembership().getMaxBooksHeld()+1 >= (requestSize+issueRepo.getNoOfBooksIssuedBy(user));
	}

	public Receipt returnBook(User user, Book book, LocalDateTime returnTime) {
		libRepo.returnBook(book);
		Boolean isExtraBook = user.getMembership().hasExtraBook(issueRepo.getBooksIssuedBy(user).size());
		IssueRequest issue = issueRepo.returnBook(book,user);
		double charges = getChargesForReturn(user, returnTime, isExtraBook, issue);
		Receipt receipt = new Receipt(user,issue,charges);
		return receipt;
	}

	private double getChargesForReturn(User user, LocalDateTime returnTime, Boolean isExtraBook, IssueRequest issue) {
		LocalDateTime issueDate = issue.getIssueDate(); 
		Period periodBetweenIssueAndReturn = Period.between(issueDate.toLocalDate(), returnTime.toLocalDate());
		double charges = user.getMembership().getChargesForReturn(periodBetweenIssueAndReturn.getDays(),isExtraBook);
		return charges;
	}

	public List<Book> getBooksIssuedBy(final User user) {
		return issueRepo.getBooksIssuedBy(user);
	}

	public Receipt returnBooks(User user, List<Book> returnedBooks, LocalDateTime returnTime) {
		Receipt receipt = new Receipt(user);
		for(Book book: returnedBooks){
			libRepo.returnBook(book);
			Boolean isExtraBook = user.getMembership().hasExtraBook(issueRepo.getBooksIssuedBy(user).size());
			IssueRequest issue = issueRepo.returnBook(book,user);
			double charges = getChargesForReturn(user, returnTime, isExtraBook, issue);
			receipt.addDetail(issue,charges);
		}
		return receipt;
	}

	public boolean isBookAvailable(Book book) {
		return libRepo.canIssue(book);
	}

}
