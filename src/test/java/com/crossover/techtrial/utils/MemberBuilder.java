package com.crossover.techtrial.utils;

import java.time.LocalDateTime;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;

public class MemberBuilder {

	private Long id;
	private String name;
	private String email;
	private MembershipStatus membershipStatus;
	private LocalDateTime membershipStartDate;
	public MemberBuilder withId(Long id) {
		this.id = id;
		return this;
	}
	public MemberBuilder withName(String name) {
		this.name = name;
		return this;
	}
	public MemberBuilder withEmail(String email) {
		this.email = email;
		return this;
	}
	
	public MemberBuilder withMembershipStatus(MembershipStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
		return this;
	}
	public MemberBuilder withMembershipStartDate(LocalDateTime membershipStartDate) {
		this.membershipStartDate = membershipStartDate;
		return this;
	}
	
	public Member build() {
		return new Member(id, name, email, membershipStatus, membershipStartDate);
	}
}
