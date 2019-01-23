/**
 * 
 */
package com.crossover.techtrial.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.exceptions.GlobalExceptionHandler;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.utils.BookBuilder;
import com.crossover.techtrial.utils.TestUtil;

/**
 * @author David Cruise Thanh Nguyen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	@Autowired
	BookRepository bookRepository;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	/**
	 * This method test the 'api/book' to get list of people available
	 * @throws Exception
	 */
	@Test
	public void getAllBooks_BookFound_ShouldReturnFoundBookEntries() throws Exception {
		Book first = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Book second = new BookBuilder()
				.withId(2L)
				.withTitle("The Second Book")
				.build();
		when(bookService.getAll()).thenReturn(Arrays.asList(first, second));
		mockMvc.perform(get("/api/book"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is("The First Book")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].title", is("The Second Book")));
		verify(bookService, times(1)).getAll();
        verifyNoMoreInteractions(bookService);
	}
	/**
	 * This method test '/api/book/{book-id}' api to get the book with
	 * given book id
	 * @throws Exception
	 */
	@Test
	public void findById_BookEntryFound_ShouldReturnFoundBookEntry() throws Exception {
		Book found = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		when(bookService.findById(1L)).thenReturn(found);
		mockMvc.perform(get("/api/book/{book-id}", 1L))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		        .andExpect(jsonPath("$.id", is(1)))
		        .andExpect(jsonPath("$.title", is("The First Book")));

		verify(bookService, times(1)).findById(1L);
		verifyNoMoreInteractions(bookService);
	}
	
	/**
	 * This method test api '/api/book/{book-id}'
	 * @throws Exception
	 */
	@Test public void findById_BookNotFound_ShouldReturnNotFoundEntity() throws Exception {
		when(bookService.findById(2L)).thenReturn(null);
		mockMvc.perform(get("/api/book/{book-id}", 2L))
			.andDo(print())
			.andExpect(status().isNotFound());
		verify(bookService, times(1)).findById(2L);
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void saveBook_WithNullTitle_ShouldReturnBadRequest() throws IOException, Exception {
		Book entry = new BookBuilder()
				.withId(1L)
				.withTitle(null)
				.build();
		MvcResult result = mockMvc.perform(post("/api/book")
	                .contentType(TestUtil.APPLICATION_JSON_UTF8)
	                .content(TestUtil.convertObjectToJsonBytes(entry)))
	                .andExpect(status().isBadRequest())
	                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
        verifyZeroInteractions(bookService);
	}
	
	/**
	 * This method test the successfully case when saving book
	 * @throws Exception
	 */
	@Test
    public void saveBook_ShouldSaveBookEntryAndReturnSavedEntry() throws Exception {
		Book savedEntry = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		when(bookService.save(any(Book.class))).thenReturn(savedEntry);
		mockMvc.perform(post("/api/book")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savedEntry)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("The First Book")));
		
		ArgumentCaptor<Book> entryCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookService, times(1)).save(entryCaptor.capture());
        verifyNoMoreInteractions(bookService);
        
        Book entryArgument = entryCaptor.getValue();
        assertThat(entryArgument.getId(), is(1L));
        assertThat(entryArgument.getTitle(), is("The First Book"));
	}
}
