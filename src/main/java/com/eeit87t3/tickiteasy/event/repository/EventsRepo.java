package com.eeit87t3.tickiteasy.event.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;

/**
 * @author Chuan(chuan13)
 */
public interface EventsRepo extends JpaRepository<EventsEntity, Integer>, JpaSpecificationExecutor<EventsEntity> {

	List<EventsEntity> findByEventCategory(CategoryEntity eventCategory);
	List<EventsEntity> findByEventTag(TagEntity eventTag);
	
	EventsEntity findByEventName(String eventName);
	
	List<EventsEntity> findByStatusIn(List<Integer> statuses);
	
	@Query("""
			SELECT e FROM EventsEntity AS e WHERE 1 = 1 
				AND (:eventName IS NULL OR e.eventName LIKE %:eventName%)
				AND (:statuses IS NULL OR e.status IN :statuses)
				AND (:categoryString IS NULL OR e.eventCategory.categoryString = :categoryString)
				AND ((:searchingStartTime IS NULL OR :searchingStartTime BETWEEN e.eventStartTime AND e.eventEndTime)
					OR (:searchingEndTime IS NULL OR :searchingEndTime BETWEEN e.eventStartTime AND e.eventEndTime)
					OR ((:searchingStartTime IS NULL OR :searchingStartTime < e.eventStartTime)
						AND (:searchingEndTime IS NULL OR :searchingEndTime > e.eventEndTime)))
			""")
	List<EventsEntity> findByDynamic(@Param("eventName") String eventName,
			@Param("statuses") List<Short> statuses,
			@Param("categoryString") String categoryString,
			@Param("searchingStartTime") LocalDateTime searchingStartTime,
			@Param("searchingEndTime") LocalDateTime searchingEndTime);
}
