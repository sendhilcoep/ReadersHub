package com.ideas.readers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.junit.Test;

public class ChargesTest {
	private Book LOTR = new Book("LORD_OF_THE_RINGS",Language.ENGLISH,Category.HISTORY);
	private Book MEWT = new Book("My Experiments With Truth", Language.HINDI, Category.COOKING);
	private Book ABC = new Book("ABC", Language.HINDI, Category.COOKING);
	private Book XYZ = new Book("XYZ", Language.FRENCH, Category.COOKING);
	private Book QWERTY = new Book("QWERTY", Language.FRENCH, Category.SPORTS);
	private Book ASDF = new Book("ASDF", Language.FRENCH, Category.COOKING);
	private Book ZXCVBN = new Book("ZXCVBN", Language.FRENCH, Category.COOKING);
	private Book MKLP = new Book("MKLP", Language.FRENCH, Category.COOKING);
	
	@Test
	public void booksReturnedWithinTimeLimitIncurNoExtraCharges() throws EmptyLibraryException {
		Map<Book, Integer> books = new HashMap<Book, Integer>(){{
			put(LOTR, 1);
			put(MEWT, 2);
			put(ABC, 3);
			put(XYZ, 5);
		}};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		final User user = new User("SomeUser", Membership.PLATINUM);
		service.issueBook(user, LOTR);
		double charges = service.returnBook(user,LOTR,LocalDateTime.now());
		assertEquals(0,charges,0.0);
	}
	
	@Test
	public void booksReturnedLaterIncurExtraCharges() throws EmptyLibraryException {
		Map<Book, Integer> books = new HashMap<Book, Integer>(){{
			put(LOTR, 1);
			put(MEWT, 2);
			put(ABC, 3);
			put(XYZ, 5);
		}};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		final User user = new User("SomeUser", Membership.PLATINUM);
		service.issueBook(user, LOTR);
		double charges = service.returnBook(user,LOTR,LocalDateTime.now().plusDays(30));
		assertEquals(500.0,charges,0.0);
	}
	
	@Test
	public void ExtrabookReturnedIncurExtraCharges() throws EmptyLibraryException, MembershipException {
		Map<Book, Integer> books = new HashMap<Book, Integer>(){{
			put(LOTR, 1);
			put(MEWT, 2);
			put(ABC, 3);
			put(XYZ, 5);
		}};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		final User user = new User("SomeUser", Membership.IVORY);
		service.issueBooks(user, Arrays.asList(LOTR,ABC,MEWT,XYZ));
		double charges = service.returnBook(user,LOTR,LocalDateTime.now().plusDays(2));
		assertEquals(50.0,charges,0.0);
	}
}
