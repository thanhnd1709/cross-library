package com.crossover.techtrial.utils;

import com.crossover.techtrial.model.Book;

public class BookBuilder {
	private Long id;
	private String title;
	public BookBuilder withId(Long id) {
		this.id = id;
		return this;
	}
	public BookBuilder withTitle(String title) {
		this.title = title;
		return this;
	}
	public Book build() {
		return new Book(id, title);
	}
}
