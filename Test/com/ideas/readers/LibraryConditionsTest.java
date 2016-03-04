package com.ideas.readers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LibraryConditionsTest {
	private Book LOTR = new Book("LORD_OF_THE_RINGS",Language.ENGLISH,Category.HISTORY);
	private Book MEWT = new Book("My Experiments With Truth", Language.HINDI, Category.COOKING);
	private Book ABC = new Book("ABC", Language.HINDI, Category.COOKING);
	private Book XYZ = new Book("XYZ", Language.FRENCH, Category.COOKING);
	private Book QWERTY = new Book("QWERTY", Language.FRENCH, Category.SPORTS);
	private Book ASDF = new Book("ASDF", Language.FRENCH, Category.COOKING);
	private Book ZXCVBN = new Book("ZXCVBN", Language.FRENCH, Category.COOKING);
	private Book MKLP = new Book("MKLP", Language.FRENCH, Category.COOKING);
	private Map<Book, Integer> books;
	private LibraryRepo repo;
	private LibraryService service;
	private User user;

	@Before
	public void init(){
		books=initBooksList();
		repo = new LibraryRepo(books);
		service = new LibraryService(repo);
		user = new User("SomeUser", Membership.PLATINUM);
	}
	@After
	public void destroy(){
		books=null;
		repo=null;
		service=null;
		user=null;
	}
	private Map<Book, Integer> initBooksList() {
		Map<Book, Integer> books = new HashMap<Book, Integer>(){{
			put(LOTR, 1);
			put(MEWT, 2);
			put(ABC, 3);			
			put(XYZ, 5);
			put(ASDF,6);
			put(QWERTY, 3);
			put(ASDF,2);
			put(ZXCVBN,2);
			put(MKLP, 2);
		}};
		return books;
	}
	
	@Test(expected=MembershipException.class)
	public void userCannotRequest5BooksOfSameLanguage() throws MembershipException, EmptyLibraryException {
		service.issueBooks(user, new ArrayList<Book>(Arrays.asList(XYZ, QWERTY, ASDF, ZXCVBN, MKLP)));
		fail("Not expected to reach here");
	}
	
	@Test(expected=MembershipException.class)
	public void userCannotRequest4BooksOfSameCatrgoryAndLanguage() throws MembershipException, EmptyLibraryException {
		service.issueBooks(user, new ArrayList<Book>(Arrays.asList(XYZ, QWERTY, ASDF, ZXCVBN, MKLP)));
		fail("Not expected to reach here");
	}
//	@Ignore
	@Test(expected=MembershipException.class)
	public void userCannotRequest4BooksOfSameCategoryAndLanguageAcrossMultipleTransactions() throws MembershipException, EmptyLibraryException {
		User testUser = new User("SomeUser1", Membership.IVORY);
		service.issueBooks(testUser, new ArrayList<Book>(Arrays.asList(XYZ, ABC)));
		service.issueBooks(testUser, new ArrayList<Book>(Arrays.asList(QWERTY, LOTR, MEWT)));
		fail("Not expected to reach here");
	}
	@Test
	public void userCanRequest3BooksOfSameCategoryDifferentLanguages() throws Exception {
		assertTrue(service.issueBooks(user, new ArrayList<Book>(Arrays.asList(LOTR,ABC,ASDF,XYZ))));
	}
	@Test
	public void userCanRequest3BooksOfSameLanguageDifferentCategories() throws Exception {
		assertTrue(service.issueBooks(user, new ArrayList<Book>(Arrays.asList(LOTR,QWERTY,ASDF,XYZ))));
	}
}
