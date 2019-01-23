/**
 * 
 */
package com.crossover.techtrial.dto;

import lombok.Data;

/**
 * @author crossover
 *
 */
@Data
public class TopMemberDTO {

	/**
	 * Constructor for TopMemberDTO
	 * 
	 * @param memberId
	 * @param name
	 * @param email
	 * @param bookCount
	 */
	public TopMemberDTO(Long memberId, String name, String email, Integer bookCount) {
		this.memberId = memberId;
		this.name = name;
		this.email = email;
		this.bookCount = bookCount;
	}

	public TopMemberDTO() {

	}

	private Long memberId;

	private String name;

	private String email;

	private Integer bookCount;
}
