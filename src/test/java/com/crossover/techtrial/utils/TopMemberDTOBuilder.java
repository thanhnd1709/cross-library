package com.crossover.techtrial.utils;

import com.crossover.techtrial.dto.TopMemberDTO;

public class TopMemberDTOBuilder {

	private Long memberId;

	private String name;

	private String email;

	private Integer bookCount;

	public TopMemberDTOBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public TopMemberDTOBuilder withEmail(String email) {
		this.email = email;
		return this;
	}

	public TopMemberDTOBuilder withMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public TopMemberDTOBuilder withBookCount(Integer bookCount) {
		this.bookCount = bookCount;
		return this;
	}

	public TopMemberDTO build() {
		return new TopMemberDTO(memberId, name, email, bookCount);
	}

}