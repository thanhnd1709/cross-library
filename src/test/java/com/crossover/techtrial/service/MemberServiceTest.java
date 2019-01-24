package com.crossover.techtrial.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TestUtil;
import com.crossover.techtrial.utils.TopMemberDTOBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
public class MemberServiceTest {
	
	@InjectMocks
	private MemberServiceImpl memberService;
	
	@Mock
	MemberRepository memberRepository;
	
	@Test
	public void testGetAllShouldReturnAllMember() {
		Member first = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@crossover.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Member second = new MemberBuilder()
				.withId(2L)
				.withName("Tom")
				.withEmail("tom@crossover.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(memberRepository.findAll()).thenReturn(Arrays.asList(first, second));
		assertThat(memberService.getAll(), CoreMatchers.hasItems(first, second));
		verify(memberRepository, times(1)).findAll();
        verifyNoMoreInteractions(memberRepository);
	}
	
	@Test
	public void testSaveMemberShouldReturnThatMember() {
		
		Member savedEntry = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@crossover.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		
		when(memberRepository.save(any(Member.class))).thenReturn(savedEntry);
		assertTrue(memberService.save(savedEntry).equals(savedEntry));
		ArgumentCaptor<Member> entryCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(entryCaptor.capture());
        verifyNoMoreInteractions(memberRepository);
        Member entryArgument = entryCaptor.getValue();
        assertTrue(entryArgument.equals(savedEntry));
	}
	
	@Test
	public void testFindByIdShouldReturnThatMember() {
		Member entry = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@crossover.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Optional<Member> optional = Optional.of(entry);
		when(memberRepository.findById(1L)).thenReturn(optional);
		assertTrue(memberService.findById(1L).equals(optional.get()));
		verify(memberRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(memberRepository);
	}
	
	@Test
	public void get_TopMemberShouldReturnFoundMember()  throws Exception{
		TopMemberDTO firstMember = new TopMemberDTOBuilder()
				.withName("first")
				.withEmail("first@crossover.com")
				.withMemberId(1L)
				.withBookCount(10)
				.build();
		TopMemberDTO secondMember = new TopMemberDTOBuilder()
				.withName("second")
				.withEmail("second@crossover.com")
				.withMemberId(2L)
				.withBookCount(20)
				.build();
		TopMemberDTO thirdMember = new TopMemberDTOBuilder()
				.withName("third")
				.withEmail("third@crossover.com")
				.withMemberId(3L)
				.withBookCount(30)
				.build();
		TopMemberDTO fouthMember = new TopMemberDTOBuilder()
				.withName("fouth")
				.withEmail("fouth@crossover.com")
				.withMemberId(4L)
				.withBookCount(40)
				.build();
		TopMemberDTO fifthMember = new TopMemberDTOBuilder()
				.withName("fifth")
				.withEmail("fifth@crossover.com")
				.withMemberId(5L)
				.withBookCount(50)
				.build();
		List<TopMemberDTO> topMemberList = new ArrayList<TopMemberDTO>();
		topMemberList.add(firstMember);
		topMemberList.add(secondMember);
		topMemberList.add(thirdMember);
		topMemberList.add(fouthMember);
		topMemberList.add(fifthMember);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTime = LocalDateTime.parse("2018-09-18 14:00:00", formatter);
		LocalDateTime endTime = LocalDateTime.parse("2018-09-18 14:03:00", formatter);
		Object[] first = new Object[4], second = new Object[4], third = new Object[4], fouth = new Object[4], fifth = new Object[4];
		first[0] = new BigInteger("1"); first[1] = "first"; first[2] = "first@crossover.com"; first[3] = new BigInteger("10");
		second[0] = new BigInteger("2"); second[1] = "second"; second[2] = "second@crossover.com"; second[3] = new BigInteger("20");
		third[0] = new BigInteger("3"); third[1] = "third"; third[2] = "third@crossover.com"; third[3] = new BigInteger("30");
		fouth[0] = new BigInteger("4"); fouth[1] = "fouth"; fouth[2] = "fouth@crossover.com"; fouth[3] = new BigInteger("40");
		fifth[0] = new BigInteger("5"); fifth[1] = "fifth"; fifth[2] = "fifth@crossover.com"; fifth[3] = new BigInteger("50");
		List<Object[]> topMembers = Arrays.asList (first, second, third, fouth, fifth);
		when(memberRepository.getTopMembers(5L, startTime, endTime)).thenReturn(topMembers);
		List<TopMemberDTO> returnMemberList = memberService.getTopMembers(5L, startTime, endTime);
		assertTrue(TestUtil.isEqualsList(topMemberList, returnMemberList)) ;  
		verify(memberRepository, times(1)).getTopMembers(5L, startTime, endTime);
	}
}
