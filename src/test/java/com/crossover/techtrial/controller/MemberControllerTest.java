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
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.utils.MemberBuilder;
import com.crossover.techtrial.utils.TestUtil;

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
}
