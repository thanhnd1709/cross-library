package com.crossover.techtrial.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import com.crossover.techtrial.utils.BookBuilder;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TransactionBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionServiceTest {
	
	@InjectMocks
	private TransactionServiceImpl transactionService;
	
	@Mock
	TransactionRepository transactionRepository;
	
	/*@Test
	public void testGetAllShouldReturnAllTransaction() {
		Transaction first = new TransactionBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@crossover.com")
				.withRegistrationNumber("P001")
				.build();
		Transaction second = new TransactionBuilder()
				.withId(2L)
				.withName("Tom")
				.withEmail("tom@crossover.com")
				.withRegistrationNumber("P002")
				.build();
		when(transactionRepository.findAll()).thenReturn(Arrays.asList(first, second));
		assertThat(transactionService.getAll(), CoreMatchers.hasItems(first, second));
		verify(transactionRepository, times(1)).findAll();
        verifyNoMoreInteractions(transactionRepository);
	}*/
	
	@Test
	public void testSaveTransactionShouldReturnThatTransaction() {
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
		Transaction savedEntry = new TransactionBuilder()
				.withId(1L)
				.withBook(book)
				.withMember(member)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(LocalDateTime.now())
				.build();
		
		when(transactionRepository.save(any(Transaction.class))).thenReturn(savedEntry);
		assertTrue(transactionService.save(savedEntry).equals(savedEntry));
		ArgumentCaptor<Transaction> entryCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(entryCaptor.capture());
        verifyNoMoreInteractions(transactionRepository);
        
        Transaction entryArgument = entryCaptor.getValue();
        assertTrue(entryArgument.equals(savedEntry));
	}
	
	@Test
	public void testFindByIdShouldReturnThatTransaction() {
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
		Transaction entry = new TransactionBuilder()
				.withId(1L)
				.withBook(book)
				.withMember(member)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(LocalDateTime.now())
				.build();
		Optional<Transaction> optional = Optional.of(entry);
		when(transactionRepository.findById(1L)).thenReturn(optional);
		assertTrue(transactionService.findById(1L).equals(optional.get()));
		verify(transactionRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(transactionRepository);
	}
	
	@Test
	public void testFindCurrentTransactionByBookId() {
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
		Transaction entry = new TransactionBuilder()
				.withId(1L)
				.withBook(book)
				.withMember(member)
				.withDateOfIssue(LocalDateTime.now())
				.withDateOfReturn(LocalDateTime.now())
				.build();
		when(transactionRepository.findCurrentTransactionByBookId(1L)).thenReturn(entry);
		assertTrue(transactionService.findCurrentTransactionByBookId(1L).equals(entry));
		verify(transactionRepository, times(1)).findCurrentTransactionByBookId(1L);
        verifyNoMoreInteractions(transactionRepository);
	}
}
