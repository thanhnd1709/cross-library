/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.exceptions.TransactionException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kshah
 *
 */
@RestController
@Slf4j
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	MemberService memberService;
	/*
	 * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS Example Post
	 * Request : { "bookId":1,"memberId":33 }
	 */
	@PostMapping(path = "/api/transaction")
	public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {
		// check bookId and memberId parameters:
		Long bookId;
		Long memberId;
		try {
			bookId = new Long(params.get("bookId"));
		} catch (NumberFormatException e) {
			throw new TransactionException(String.format("bookId %s is not a valid number", params.get("bookId")), HttpStatus.BAD_REQUEST);
		}
		try {
			memberId = new Long(params.get("memberId"));
		} catch (NumberFormatException e) {
			throw new TransactionException(String.format("memberId %s is not a valid number", params.get("memberId")), HttpStatus.BAD_REQUEST);
		}
		Transaction transaction = new Transaction();
		// validate book and member exist
		Book book = bookService.findById(bookId);
		if (book == null) {
			throw new TransactionException(String.format("bookId %s does not exist", bookId), HttpStatus.NOT_FOUND);
		}
		transaction.setBook(book);
		Member member = memberService.findById(memberId);
		if (member == null) {
			throw new TransactionException(String.format("memberId %s does not exist", memberId), HttpStatus.NOT_FOUND);
		}
		transaction.setMember(member);
		// validate the current book has not been borrowed by another member
		Transaction currentTran = transactionService.findCurrentTransactionByBookId(bookId);
		if (currentTran != null) {
			throw new TransactionException(String.format("bookId %s has been borrowed by another member", bookId),
					HttpStatus.FORBIDDEN);
		}
		Transaction tran = transactionService.save(transaction);
		log.info("Transaction {} was registered successfully", tran);
		return ResponseEntity.ok(tran);
	}

	/*
	 * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@PatchMapping(path = "/api/transaction/{transaction-id}/return")
	public ResponseEntity<Transaction> returnBookTransaction(@PathVariable(name = "transaction-id") Long transactionId) {
		Transaction transaction = transactionService.findById(transactionId);
		// if transaction has been returned before
		if (transaction.getDateOfReturn() != null) {
			throw new TransactionException(String.format("The book has been returned"), HttpStatus.FORBIDDEN);
		}
		transaction.setDateOfReturn(LocalDateTime.now());
		transactionService.update(transaction);
		log.info("Book was returned successfully");
		return ResponseEntity.ok().body(transaction);
	}

}
