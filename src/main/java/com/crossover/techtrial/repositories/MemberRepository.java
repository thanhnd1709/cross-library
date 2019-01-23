/**
 * 
 */
package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Member;

/**
 * Person repository for basic operations on Person entity.
 * @author crossover
 */
@RestResource(exported=false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

	@Query(value = "select M.id as memberId, M.name as name, M.email as email, count(*) as bookCount " + 
			"from member M inner join transaction T " + 
			"on M.id = T.member_id " + 
			"where T.date_of_issue >= :startTime AND T.date_of_return <= :endTime " + 
			"GROUP BY memberId, name, email " + 
			"ORDER BY bookCount DESC " + 
			"limit :count",
			nativeQuery = true)
	List<Object[]> getTopMembers(@Param("count") Long count, @Param("startTime")  LocalDateTime startTime, @Param("endTime")  LocalDateTime endTime);
}
