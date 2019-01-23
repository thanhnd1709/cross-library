/**
 * 
 */
package com.crossover.techtrial.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author crossover
 */

@RestController
@Slf4j
public class MemberController {

	@Autowired
	MemberService memberService;

	/*
	 * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@PostMapping(path = "/api/member")
	public ResponseEntity<Member> register(@Valid @RequestBody Member p) {
		p.setMembershipStartDate(LocalDateTime.now());
		Member savedMember = memberService.save(p);
		log.info("Member {} was registered successfully", savedMember);
		return ResponseEntity.ok(savedMember);
	}

	/*
	 * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@GetMapping(path = "/api/member")
	public ResponseEntity<List<Member>> getAll() {
		List<Member> members = memberService.getAll();
		if (CollectionUtils.isEmpty(members)) {
			return ResponseEntity.notFound().build();
		} else {
	    	log.info("Getting all members successfully");
	    	return ResponseEntity.ok(members);
	    }
	}

	/*
	 * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
	 */
	@GetMapping(path = "/api/member/{member-id}")
	public ResponseEntity<Member> getMemberById(@PathVariable(name = "member-id", required = true) Long memberId) {
		Member member = memberService.findById(memberId);
		if (member != null) {
			log.info("Getting member with memberId {} successfully", memberId);
			return ResponseEntity.ok(member);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * This API returns the top 5 members who issued the most books within the
	 * search duration. Only books that have dateOfIssue and dateOfReturn within the
	 * mentioned duration should be counted. Any issued book where dateOfIssue or
	 * dateOfReturn is outside the search, should not be considered.
	 * 
	 * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
	 * 
	 * @return
	 */
	@GetMapping(path = "/api/member/top-member")
	public ResponseEntity<List<TopMemberDTO>> getTopMembers(
			@RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
			@RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
		List<TopMemberDTO> topDrivers = new ArrayList<>();
		/**
		 * Your Implementation Here.
		 * 
		 */
		Long count = 5L;
		topDrivers = memberService.getTopMembers(count, startTime, endTime);
		/**
		 * Implementation End.
		 * 
		 */
		return ResponseEntity.ok(topDrivers);
	}
}
