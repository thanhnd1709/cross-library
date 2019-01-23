package com.crossover.techtrial.utils;

import java.time.LocalDateTime;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;

public class TransactionBuilder {

	private Long id;
	private Book book;
	private Member member;
	private LocalDateTime dateOfIssue;
	private LocalDateTime dateOfReturn;

	public TransactionBuilder withId(Long id) {
		this.id = id;
		return this;
	}

	public TransactionBuilder withBook(Book book) {
		this.book = book;
		return this;
	}

	public TransactionBuilder withMember(Member member) {
		this.member = member;
		return this;
	}

	public TransactionBuilder withDateOfIssue(LocalDateTime dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
		return this;
	}

	public TransactionBuilder withDateOfReturn(LocalDateTime dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
		return this;
	}

	public Transaction build() {
		return new Transaction(id, book, member, dateOfIssue, dateOfReturn);
	}
}
