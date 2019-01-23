package com.crossover.techtrial.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.exceptions.TransactionException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.repositories.TransactionRepository;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	MemberRepository memberRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "transaction", key = "#result.id")
	public Transaction save(Transaction tran) {
		tran.setDateOfIssue(LocalDateTime.now());
		return transactionRepository.save(tran);
	}

	@Override
	public void validate(Transaction transaction, Long bookId, Long memberId) {
		// validate book and member exist
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {
			throw new TransactionException(String.format("bookId %s does not exist", bookId), HttpStatus.NOT_FOUND);
		}
		transaction.setBook(book);
		Member member = memberRepository.findById(memberId).get();
		if (member == null) {
			throw new TransactionException(String.format("memberId %s does not exist", memberId), HttpStatus.NOT_FOUND);
		}
		transaction.setMember(member);
		// validate the current book has not been borrowed by another member
		Transaction currentTran = transactionRepository.findCurrentTransactionByBookId(bookId);
		if (currentTran != null) {
			throw new TransactionException(String.format("bookId %s has been borrowed by another member", bookId),
					HttpStatus.FORBIDDEN);
		}
	}

	@Override
	@Cacheable(value = "transaction", key = "#transactionId")
	public Transaction findById(Long transactionId) {
		return transactionRepository.findById(transactionId).orElse(null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "transaction", key = "#transaction.id")
	public void update(Transaction transaction) {
		transactionRepository.save(transaction);
	}

}
