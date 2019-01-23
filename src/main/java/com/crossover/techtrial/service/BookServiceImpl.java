/**
 * 
 */
package com.crossover.techtrial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.TransactionRepository;

/**
 * @author crossover
 *
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BookServiceImpl implements BookService{

  @Autowired
  BookRepository bookRepository;
  
  @Autowired
  TransactionRepository transactionRepository;
  
  @Override
  public List<Book> getAll() {
    List<Book> bookList = new ArrayList<>();
    bookRepository.findAll().forEach(bookList::add);
    return bookList;
    
  }
  
  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  @CachePut (value = "book", key = "#result.id")
  public Book save(Book p) {
    return bookRepository.save(p);
  }

  @Override
  @Cacheable (value = "book", key = "#bookId")
  public Book findById(Long bookId) {
    Optional<Book> dbPerson = bookRepository.findById(bookId);
    return dbPerson.orElse(null);
  }

}
