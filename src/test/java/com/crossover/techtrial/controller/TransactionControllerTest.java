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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import com.crossover.techtrial.utils.BookBuilder;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TestUtil;

/**
 * @author David Cruise Thanh Nguyen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	private TransactionController transactionController;

	@Mock
	private TransactionService transactionService;
	
	@Mock
	private BookService bookService;
	
	@Mock
	private MemberService memberService;

	@Autowired
	TransactionRepository transactionRepository;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(transactionController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	
	/**
	 * This method test the 'api/transaction' when the bookId is not valid long value
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_WithInvalidBookId_ShouldReturnBadRequest() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "bookId");
		params.put("memberId", "1");
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isBadRequest())
                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
		verifyNoMoreInteractions(transactionService);
	}
	
	/**
	 * This method test the 'api/transaction' when the bookId is not valid long value
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_WithInvalidMember_ShouldReturnBadRequest() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "1");
		params.put("memberId", "memberId");
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isBadRequest())
                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
		verifyNoMoreInteractions(transactionService);
	}
	
	
	/**
	 * This method test the 'api/transaction' when the bookId is not exist in database
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_WithNotExistBookId_ShouldReturnNotFound() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "3");
		params.put("memberId", "1");
		Member member = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(bookService.findById(3L)).thenReturn(null);
		when(memberService.findById(1L)).thenReturn(member);
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isNotFound())
                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
		verifyNoMoreInteractions(transactionService);
	}
	
	/**
	 * This method test the 'api/transaction' when the member is not exist in database
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_WithNotExistMemberId_ShouldReturnNotFound() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "3");
		params.put("memberId", "1");
		Book book = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		when(bookService.findById(3L)).thenReturn(book);
		when(memberService.findById(1L)).thenReturn(null);
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isNotFound())
                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
		verifyNoMoreInteractions(transactionService);
	}
	
	/**
	 * This method test the 'api/transaction' when the member is not exist in database
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_WithAllreadyBorrowedBook_ShouldReturnForbidden() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "3");
		params.put("memberId", "1");
		Book book = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Member member = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(bookService.findById(3L)).thenReturn(book);
		when(memberService.findById(1L)).thenReturn(member);
		when(transactionService.findCurrentTransactionByBookId(any(Long.class))).thenReturn(new Transaction());
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isForbidden())
                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
		verify(transactionService, times(1)).findCurrentTransactionByBookId(any(Long.class));
		verifyNoMoreInteractions(transactionService);
	}
	
	/**
	 * This method test the 'api/transaction' failure case when the book has already been returned
	 * @throws Exception
	 */
	@Test
	public void test_issueBookToMember_Successfull_ShouldReturnOK() throws IOException, Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("bookId", "3");
		params.put("memberId", "1");
		Book book = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Member member = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(bookService.findById(3L)).thenReturn(book);
		when(memberService.findById(1L)).thenReturn(member);
		when(transactionService.findCurrentTransactionByBookId(any(Long.class))).thenReturn(null);
		MvcResult result = mockMvc.perform(post("/api/transaction")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(params)))
                .andExpect(status().isOk())
                .andReturn();
		// TODO Argument captor
		verify(transactionService, times(1)).findCurrentTransactionByBookId(any(Long.class));
		verify(transactionService, times(1)).save(any(Transaction.class));
		verifyNoMoreInteractions(transactionService);
	}
	
	/**
	 * This method test the 'api/transaction' return successfully
	 * @throws Exception
	 */
	@Test
	public void test_returnBookTransaction_Successfull_ShouldReturnOk() throws IOException, Exception {
		Transaction currentTran = new Transaction();
		currentTran.setDateOfReturn(null);
		when(transactionService.findById(1L)).thenReturn(currentTran);
		MvcResult result = mockMvc.perform(patch("/api/transaction/{transactionId}/return", 1L))
                .andExpect(status().isOk())
                .andReturn();
		verify(transactionService, times(1)).findById(1L);
		verify(transactionService, times(1)).update(currentTran);
		verifyNoMoreInteractions(transactionService);
		// TODO ArgumentCaptor
	}
	
}
