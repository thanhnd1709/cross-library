/**
 * 
 */
package com.crossover.techtrial.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import lombok.extern.slf4j.Slf4j;

/**
 * BookController for Book related APIs.
 * 
 * @author crossover
 *
 */
@RestController
@Slf4j
public class BookController {

	@Autowired
	private BookService bookService;

	/*
	 * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@GetMapping(path = "/api/book")
	public ResponseEntity<List<Book>> getBooks() {
		List<Book> books = bookService.getAll();
		if (CollectionUtils.isEmpty(books)) {
			return ResponseEntity.notFound().build();
		} else {
	    	log.info("Getting all book successfully");
	    	return ResponseEntity.ok(books);
	    }
	}

	/*
	 * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@PostMapping(path = "/api/book")
	public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
		Book savedBook = bookService.save(book);
		log.info("Book {} was registered successfully", savedBook);
		return ResponseEntity.ok(savedBook);
	}

	/*
	 * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@GetMapping(path = "/api/book/{book-id}")
	public ResponseEntity<Book> getRideById(@PathVariable(name = "book-id", required = true) Long bookId) {
		Book book = bookService.findById(bookId);
		if (book != null) {
			log.info("Getting book successfully");
			return ResponseEntity.ok(book);
		}
		return ResponseEntity.notFound().build();
	}

}
