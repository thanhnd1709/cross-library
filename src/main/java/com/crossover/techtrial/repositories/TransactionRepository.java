/**
 * 
 */
package com.crossover.techtrial.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Transaction;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	@Query(value = "SELECT * FROM transaction WHERE book_id = :bookId AND date_of_return IS NULL",
		nativeQuery = true)
	Transaction findCurrentTransactionByBookId(@Param("bookId") Long bookId);
}
