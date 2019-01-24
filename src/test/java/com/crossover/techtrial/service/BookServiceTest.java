package com.crossover.techtrial.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.utils.BookBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookServiceTest {
	
	@InjectMocks
	private BookServiceImpl bookService;
	
	@Mock
	BookRepository bookRepository;
	
	@Test
	public void testGetAllShouldReturnAllBook() {
		Book first = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Book second = new BookBuilder()
				.withId(2L)
				.withTitle("The Second Book")
				.build();
		when(bookRepository.findAll()).thenReturn(Arrays.asList(first, second));
		assertThat(bookService.getAll(), CoreMatchers.hasItems(first, second));
		verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
	}
	
	@Test
	public void testSaveBookShouldReturnThatBook() {
		
		Book savedEntry = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		
		when(bookRepository.save(any(Book.class))).thenReturn(savedEntry);
		assertTrue(bookService.save(savedEntry).equals(savedEntry));
		ArgumentCaptor<Book> entryCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(entryCaptor.capture());
        verifyNoMoreInteractions(bookRepository);
        
        Book entryArgument = entryCaptor.getValue();
        assertTrue(entryArgument.equals(savedEntry));
	}
	
	@Test
	public void testFindByIdShouldReturnThatBook() {
		Book entry = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Optional<Book> optional = Optional.of(entry);
		when(bookRepository.findById(1L)).thenReturn(optional);
		assertTrue(bookService.findById(1L).equals(optional.get()));
		verify(bookRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(bookRepository);
	}
	
}
