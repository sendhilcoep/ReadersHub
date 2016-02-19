package com.ideas.readers;

import java.util.HashMap;
import java.util.Map;

public class LibraryRepo {
	private Map<Book,Integer> books = new HashMap<>();
	public boolean issueBook(Book requestedBook) throws EmptyLibraryException {
		if(books.size() > 0) {
			Integer countOfCopies = books.get(requestedBook);
			if(countOfCopies > 0) {
				books.put(requestedBook, --countOfCopies);
				return true;
			}
			return false;
		}
		throw new EmptyLibraryException("No books in the library");
	}

	public LibraryRepo(Map<Book, Integer> books) {
		this.books.putAll(books);
	}

	public boolean canIssue(Book book) {
		return books.get(book) > 0;
	}

	public void returnBook(Book book) {
		books.put(book, books.get(book)+1);
		
	}
}
