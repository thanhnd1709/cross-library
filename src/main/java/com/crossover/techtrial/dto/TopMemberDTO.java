/**
 * 
 */
package com.crossover.techtrial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crossover
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMemberDTO {

	/**
	 * Constructor for TopMemberDTO
	 * 
	 * @param memberId
	 * @param name
	 * @param email
	 * @param bookCount
	 */

	private Long memberId;

	private String name;

	private String email;

	private Integer bookCount;
}
