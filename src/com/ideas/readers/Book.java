package com.ideas.readers;

public class Book {

	private String name;
	private Language language;
	private Category category;

	public Book(String name, Language language, Category category) {
		this.name = name;
		this.language = language;
		this.category = category;
	}
	
	public Language getLanguage(){
		return language;
	}
	public Category getCategory(){
		return category;
	}
	@Override
	public boolean equals(Object object){
		Book otherbook = (Book) object;
		return (this.name.equals(otherbook.name) && this.language.equals(otherbook.language));
	}
	
}
