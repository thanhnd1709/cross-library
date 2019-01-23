/**
 * 
 */
package com.crossover.techtrial.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

/**
 * @author crossover
 *
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MemberServiceImpl implements MemberService {
	
	private static final int POSITION_ZERO = 0;
	private static final int POSITION_ONE = 1;
	private static final int POSITION_TWO = 2;
	private static final int POSITION_THREE = 3;

	@Autowired
	MemberRepository memberRepository;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut (value = "member", key = "#result.id")
	public Member save(Member member) {
		return memberRepository.save(member);
	}
	
	@Autowired
	@Cacheable (value = "member", key = "#memberId")
	public Member findById(Long memberId) {
		Optional<Member> optionalMember = memberRepository.findById(memberId);
	    return optionalMember.orElse(null);
	}

	public List<Member> getAll() {
		List<Member> members = new ArrayList<>();
		memberRepository.findAll().forEach(members::add);
		return members;
	}

	@Override
	public List<TopMemberDTO> getTopMembers(Long count, LocalDateTime startTime, LocalDateTime endTime) {
		List<TopMemberDTO> memberList = new ArrayList<>();
		List<Object[]> objectList = memberRepository.getTopMembers(count, startTime, endTime);
		for (Object[] currentObject : objectList) {
			Long memberId = (Long) currentObject[POSITION_ZERO];
			String name = (String) currentObject[POSITION_ONE];
			String email = (String) currentObject[POSITION_TWO];
			Integer bookCount = (Integer) currentObject[POSITION_THREE];
			TopMemberDTO tempTopMemberDTO = new TopMemberDTO(memberId, name, email, bookCount);
			memberList.add(tempTopMemberDTO);
		}
		return memberList;
	}

}
