/**
 * 
 */
package com.crossover.techtrial.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Transaction;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	@Query("SELECT t FROM transaction t WHERE t.book_id = :bookId AND t.date_of_return IS NULL")
	Transaction findCurrentTransactionByBookId(Long bookId);
}
