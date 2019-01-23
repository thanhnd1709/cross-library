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
import com.crossover.techtrial.model.Transaction;
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
		transactionService.validate(transaction, bookId, memberId);
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
