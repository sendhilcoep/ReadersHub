package com.ideas.readers;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ideas.readers.exceptions.EmptyLibraryException;
import com.ideas.readers.exceptions.MembershipException;

public class MembershipTest {

	private Book LOTR = new Book("LORD_OF_THE_RINGS", Language.ENGLISH, Category.HISTORY);
	private Book MEWT = new Book("My Experiments With Truth", Language.HINDI, Category.COOKING);
	private Book ABC = new Book("ABC", Language.HINDI, Category.COOKING);
	private Book XYZ = new Book("XYZ", Language.FRENCH, Category.COOKING);
	private Book QWERTY = new Book("QWERTY", Language.FRENCH, Category.SPORTS);
	private Book ASDF = new Book("ASDF", Language.FRENCH, Category.COOKING);
	private Book ZXCVBN = new Book("ZXCVBN", Language.FRENCH, Category.COOKING);
	private Book MKLP = new Book("MKLP", Language.FRENCH, Category.COOKING);

	@Test(expected = EmptyLibraryException.class)
	public void emptyLibraryCannotIssueAnyBooks() throws EmptyLibraryException {
		LibraryRepo repo = new LibraryRepo(new HashMap<Book, Integer>());
		LibraryService service = new LibraryService(repo);
		User user = new User("SomeUser", Membership.PLATINUM);
		service.issueBook(user, LOTR);
		fail("Library is empty");
	}

	@Test
	public void userCanRequestABook() throws Exception {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService libraryService = new LibraryService(repo);
		User user = new User("SomeUser", Membership.PLATINUM);
		assertTrue(libraryService.issueBook(user, LOTR));
	}

	@Test
	public void userRequestsABookThatHasAlreadyBeenIssued() throws EmptyLibraryException {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService libraryService = new LibraryService(repo);
		User user = new User("SomeUser", Membership.PLATINUM);
		User anotherUser = new User("SomeUser", Membership.PLATINUM);
		assertTrue(libraryService.issueBook(user, LOTR));
		assertFalse(libraryService.issueBook(anotherUser, LOTR));
	}

	@Test(expected = MembershipException.class)
	public void userCannotRequestMoreBooksThanMembershipLimit() throws MembershipException, EmptyLibraryException {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
				put(MEWT, 2);
				put(ABC, 3);
				put(XYZ, 5);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		User user = new User("SomeUser", Membership.IVORY);
		service.issueBooks(user, new ArrayList<Book>(Arrays.asList(LOTR, MEWT, ABC, XYZ, ABC)));
		fail("More books than allowed requested");
	}

	@Test(expected = MembershipException.class)
	public void userCannotRequestMoreBooksThanMembershipLimitAcrossMultipleTransactions()
			throws MembershipException, EmptyLibraryException {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
				put(MEWT, 2);
				put(ABC, 3);
				put(XYZ, 5);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		User user = new User("SomeUser", Membership.IVORY);
		service.issueBooks(user, new ArrayList<Book>(Arrays.asList(LOTR, MEWT, ABC, ABC)));
		service.issueBooks(user, new ArrayList<Book>(Arrays.asList(XYZ)));
		fail("More books than allowed requested");
	}

	@Test
	public void shouldReturnBooksIssuedByAUser() throws Exception {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
				put(MEWT, 2);
				put(ABC, 3);
				put(XYZ, 5);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		final User user = new User("SomeUser", Membership.PLATINUM);
		service.issueBook(user, LOTR);
		assertEquals(1, service.getBooksIssuedBy(user).size());
		service.issueBook(user, MEWT);
		assertEquals(2, service.getBooksIssuedBy(user).size());
		service.returnBook(user, LOTR, LocalDateTime.now().plusDays(1));
		assertEquals(1, service.getBooksIssuedBy(user).size());
	}

	@Test
	public void shouldIssueExtraBook() throws Exception {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
				put(MEWT, 2);
				put(ABC, 3);
				put(XYZ, 5);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
		final User user = new User("SomeUser", Membership.IVORY);
		assertTrue(service.issueBooks(user, Arrays.asList(LOTR, MEWT, ABC, XYZ)));
	}
	
	@Test
	public void userCanCheckIfBookAvailable() throws Exception {
		Map<Book, Integer> books = new HashMap<Book, Integer>() {
			{
				put(LOTR, 1);
				put(MEWT, 2);
				put(ABC, 3);
				put(XYZ, 5);
			}
		};
		LibraryRepo repo = new LibraryRepo(books);
		LibraryService service = new LibraryService(repo);
assertTrue(service.isBookAvailable(LOTR));		
	}

}
