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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.exceptions.GlobalExceptionHandler;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TestUtil;
import com.crossover.techtrial.utils.TopMemberDTOBuilder;

/**
 * @author David Cruise Thanh Nguyen
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	private MemberController memberController;

	@Mock
	private MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	/**
	 * This method test the 'api/member' to get list of people available
	 * @throws Exception
	 */
	@Test
	public void test_getAll_Members_ShouldReturnFoundMemberEntries() throws Exception {
		Member first = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		Member second = new MemberBuilder()
				.withId(2L)
				.withName("Cruise")
				.withEmail("cruise@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(memberService.getAll()).thenReturn(Arrays.asList(first, second));
		mockMvc.perform(get("/api/member"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("David")))
		        .andExpect(jsonPath("$[0].email", is("david@gmail.com")))
				.andExpect(jsonPath("$[0].membershipStatus", is("ACTIVE")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Cruise")))
		        .andExpect(jsonPath("$[1].email", is("cruise@gmail.com")))
				.andExpect(jsonPath("$[1].membershipStatus", is("ACTIVE")));
		verify(memberService, times(1)).getAll();
        verifyNoMoreInteractions(memberService);
	}
	/**
	 * This method test '/api/member/{member-id}' api to get the member with
	 * given member id
	 * @throws Exception
	 */
	@Test
	public void test_getMemberById_MemberEntryFound_ShouldReturnFoundMemberEntry() throws Exception {
		Member found = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(memberService.findById(1L)).thenReturn(found);
		mockMvc.perform(get("/api/member/{member-id}", 1L))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("David")))
		        .andExpect(jsonPath("$.email", is("david@gmail.com")))
				.andExpect(jsonPath("$.membershipStatus", is("ACTIVE")));
		verify(memberService, times(1)).findById(1L);
		verifyNoMoreInteractions(memberService);
	}
	
	/**
	 * This method test api '/api/member/{member-id}'
	 * @throws Exception
	 */
	@Test public void test_getMemberById_MemberNotFound_ShouldReturnNotFoundEntity() throws Exception {
		when(memberService.findById(2L)).thenReturn(null);
		mockMvc.perform(get("/api/member/{member-id}", 2L))
			.andDo(print())
			.andExpect(status().isNotFound());
		verify(memberService, times(1)).findById(2L);
		verifyNoMoreInteractions(memberService);
	}
	
	@Test
	public void test_register_Member_WithNullEmail_ShouldReturnBadRequest() throws IOException, Exception {
		Member entry = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail(null)
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		MvcResult result = mockMvc.perform(post("/api/member")
	                .contentType(TestUtil.APPLICATION_JSON_UTF8)
	                .content(TestUtil.convertObjectToJsonBytes(entry)))
	                .andExpect(status().isBadRequest())
	                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
        verifyZeroInteractions(memberService);
	}
	
	@Test
	public void test_register_Member_WithNullName_ShouldReturnBadRequest() throws IOException, Exception {
		Member entry = new MemberBuilder()
				.withId(1L)
				.withName(null)
				.withEmail("david@gamil.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		MvcResult result = mockMvc.perform(post("/api/member")
	                .contentType(TestUtil.APPLICATION_JSON_UTF8)
	                .content(TestUtil.convertObjectToJsonBytes(entry)))
	                .andExpect(status().isBadRequest())
	                .andReturn();
		assertThat(result.getResolvedException(), is(notNullValue()));
        verifyZeroInteractions(memberService);
	}
	
	/**
	 * This method test the successfully case when saving member
	 * @throws Exception
	 */
	@Test
    public void test_register_Member_ShouldSaveMemberEntry_AndReturnSavedEntry() throws Exception {
		Member savedEntry = new MemberBuilder()
				.withId(1L)
				.withName("David")
				.withEmail("david@gmail.com")
				.withMembershipStatus(MembershipStatus.ACTIVE)
				.withMembershipStartDate(LocalDateTime.now())
				.build();
		when(memberService.save(any(Member.class))).thenReturn(savedEntry);
		mockMvc.perform(post("/api/member")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(savedEntry)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("David")))
		        .andExpect(jsonPath("$.email", is("david@gmail.com")))
				.andExpect(jsonPath("$.membershipStatus", is("ACTIVE")));
		
		ArgumentCaptor<Member> entryCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberService, times(1)).save(entryCaptor.capture());
        verifyNoMoreInteractions(memberService);
        
        Member entryArgument = entryCaptor.getValue();
        assertThat(entryArgument.getId(), is(1L));
        assertThat(entryArgument.getName(), is("David"));
        assertThat(entryArgument.getEmail(), is("david@gmail.com"));
        assertThat(entryArgument.getMembershipStatus(), is(MembershipStatus.ACTIVE));
	}
	
	@Test
	public void get_TopMemberShouldReturnFoundMember()  throws Exception{
		TopMemberDTO first = new TopMemberDTOBuilder()
				.withName("first")
				.withEmail("first@crossover.com")
				.withMemberId(1L)
				.withBookCount(10)
				.build();
		TopMemberDTO second = new TopMemberDTOBuilder()
				.withName("second")
				.withEmail("second@crossover.com")
				.withMemberId(2L)
				.withBookCount(20)
				.build();
		TopMemberDTO third = new TopMemberDTOBuilder()
				.withName("third")
				.withEmail("third@crossover.com")
				.withMemberId(3L)
				.withBookCount(30)
				.build();
		TopMemberDTO fouth = new TopMemberDTOBuilder()
				.withName("fouth")
				.withEmail("fouth@crossover.com")
				.withMemberId(4L)
				.withBookCount(40)
				.build();
		TopMemberDTO fifth = new TopMemberDTOBuilder()
				.withName("fifth")
				.withEmail("fifth@crossover.com")
				.withMemberId(5L)
				.withBookCount(50)
				.build();
		List<TopMemberDTO> topMemberList = new ArrayList<TopMemberDTO>();
		topMemberList.add(first);
		topMemberList.add(second);
		topMemberList.add(third);
		topMemberList.add(fouth);
		topMemberList.add(fifth);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTime = LocalDateTime.parse("2018-09-18 14:00:00", formatter);
		LocalDateTime endTime = LocalDateTime.parse("2018-09-18 14:03:00", formatter);
		when(memberService.getTopMembers(5L, startTime, endTime)).thenReturn(topMemberList);
		mockMvc.perform(get("/api/member/top-member?startTime=2018-09-18T14:00:00&endTime=2018-09-18T14:03:00"))
		.andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$[0].name", is(first.getName())))
        .andExpect(jsonPath("$[0].email", is(first.getEmail())))
        .andExpect(jsonPath("$[0].memberId", is(1)))
        .andExpect(jsonPath("$[0].bookCount", is(10)))
        .andExpect(jsonPath("$[1].name", is(second.getName())))
        .andExpect(jsonPath("$[1].email", is(second.getEmail())))
        .andExpect(jsonPath("$[1].memberId", is(2)))
        .andExpect(jsonPath("$[1].bookCount", is(20)))
        .andExpect(jsonPath("$[2].name", is(third.getName())))
        .andExpect(jsonPath("$[2].email", is(third.getEmail())))
        .andExpect(jsonPath("$[2].memberId", is(3)))
        .andExpect(jsonPath("$[2].bookCount", is(30)))
        .andExpect(jsonPath("$[3].name", is(fouth.getName())))
        .andExpect(jsonPath("$[3].email", is(fouth.getEmail())))
        .andExpect(jsonPath("$[3].memberId", is(4)))
        .andExpect(jsonPath("$[3].bookCount", is(40)))
        .andExpect(jsonPath("$[4].name", is(fifth.getName())))
        .andExpect(jsonPath("$[4].email", is(fifth.getEmail())))
        .andExpect(jsonPath("$[4].memberId", is(5)))
        .andExpect(jsonPath("$[4].bookCount", is(50)));

		verify(memberService, times(1)).getTopMembers(5L, startTime, endTime);
		verifyNoMoreInteractions(memberService);
	}
}
