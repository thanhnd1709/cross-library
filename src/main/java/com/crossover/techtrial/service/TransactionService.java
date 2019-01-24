package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;

public interface TransactionService {
	
	public Transaction findById(Long transactionId);

	public Transaction save(Transaction tran);

	//public void validate(Transaction transaction, Long bookId, Long memberId);

	public void update(Transaction transaction);

	public Transaction findCurrentTransactionByBookId(Long bookId);
}
