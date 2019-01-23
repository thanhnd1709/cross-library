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
import java.time.LocalDateTime;
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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.service.TransactionService;
import com.crossover.techtrial.utils.BookBuilder;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TestUtil;
import com.crossover.techtrial.utils.TransactionBuilder;

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

	@Autowired
	TransactionRepository transactionRepository;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(transactionController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	/**
	 * This method test the 'api/transaction' to get list of people available
	 * @throws Exception
	 */
	@Test
	public void getAllTransactions_TransactionFound_ShouldReturnFoundTransactionEntries() throws Exception {
		/*Book firstBook = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Book secondBook = new BookBuilder()
				.withId(2L)
				.withTitle("The Second Book")
				.build();
		Member firstMember = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Member secondMember = new MemberBuilder()
				.withId(2L)
				.withName("Cruise")
				.withEmail("cruise@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Transaction firstTransaction = new TransactionBuilder()
				.withId(1L)
				.withBook(firstBook)
				.withMember(firstMember)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(null)
				.build();
		Transaction secondTransaction = new TransactionBuilder()
				.withId(1L)
				.withBook(secondBook)
				.withMember(secondMember)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(null)
				.build();*/
		/*when(transactionService.getAll()).thenReturn(Arrays.asList(first, second));
		mockMvc.perform(get("/api/transaction"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("David")))
		        .andExpect(jsonPath("$[0].email", is("david@crossover.com")))
				.andExpect(jsonPath("$[0].transactionshipStatus", is(TransactionshipStatus.ACTIVE)))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].name", is("Cruise")))
		        .andExpect(jsonPath("$[0].email", is("cruise@crossover.com")))
				.andExpect(jsonPath("$[0].transactionshipStatus", is(TransactionshipStatus.ACTIVE)));
		verify(transactionService, times(1)).getAll();*/
        verifyNoMoreInteractions(transactionService);
	}
	/**
	 * This method test '/api/transaction/{transaction-id}' api to get the transaction with
	 * given transaction id
	 * @throws Exception
	 */
	@Test
	public void findById_TransactionEntryFound_ShouldReturnFoundTransactionEntry() throws Exception {
		Book firstBook = new BookBuilder()
				.withId(1L)
				.withTitle("The First Book")
				.build();
		Member firstMember = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Transaction firstTransaction = new TransactionBuilder()
				.withId(1L)
				.withBook(firstBook)
				.withMember(firstMember)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(null)
				.build();
		when(transactionService.findById(1L)).thenReturn(firstTransaction);
		mockMvc.perform(get("/api/transaction/{transaction-id}", 1L))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.book.id", is(1)))
				.andExpect(jsonPath("$.book.title", is("The First Book")))
				.andExpect(jsonPath("$.member.id", is(1)))
				.andExpect(jsonPath("$.member.name", is("David")))
		        .andExpect(jsonPath("$.member.email", is("david@crossover.com")))
				.andExpect(jsonPath("$.member.membershipStatus", is(MembershipStatus.ACTIVE)));
		// TODO
		verify(transactionService, times(1)).findById(1L);
		verifyNoMoreInteractions(transactionService);
	}
}
