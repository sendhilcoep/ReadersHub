package com.ideas.readers;

import java.util.Objects;

public class Book {

	private String name;
	private Language language;
	private Category category;

	public Book(String name, Language language, Category category) {
		this.name = name;
		this.language = language;
		this.category = category;
	}

	public Language getLanguage() {
		return language;
	}

	public Category getCategory() {
		return category;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Book otherbook = (Book) object;
		return (this.name.equals(otherbook.name) && this.language.equals(otherbook.language));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + Objects.hashCode(this.name);
		hash = 83 * hash + Objects.hashCode(this.language);
		hash = 83 * hash + Objects.hashCode(this.category);
		return hash;
	}

}
