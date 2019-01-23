/**
 * 
 */
package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.model.Member;

/**
 * Person repository for basic operations on Person entity.
 * @author crossover
 */
@RestResource(exported=false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

	@Query(value = "SELECT P.name as name, P.email as email, SUM(R.duration) as totalRideDurationInSeconds, max(R.duration) as maxRideDurationInSecods, avg(R.distance) as averageDistance   " + 
			" FROM ( " + 
			"	SELECT *, TIMESTAMPDIFF(SECOND,start_time,end_time) as duration   " + 
			"	FROM ride   " + 
			"	WHERE start_time >= :startTime AND end_time <= :endTime  " + 
			"	) R   " + 
			" LEFT JOIN person P ON R.driver_id = P.id   " + 
			" GROUP BY name, email   " + 
			" ORDER BY totalRideDurationInSeconds DESC " + 
			" LIMIT :count", 
			nativeQuery = true)
	List<Object[]> getTopMembers(Long count, LocalDateTime startTime, LocalDateTime endTime);
}
