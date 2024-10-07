package com.eeit87t3.tickiteasy.event.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

// 封裝查詢邏輯，不需 Spring 管理
public class EventsSpecification {
	
	public static Specification<EventsEntity> hasEventName(String eventName) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (eventName == null || eventName.isBlank()) {
					return null;
				} else {
					return criteriaBuilder.like(root.get("eventName"), "%" + eventName + "%");
				}
			}
		};
	}

	public static Specification<EventsEntity> hasCategoryString(String categoryString) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (categoryString == null || categoryString.isBlank()) {
					return null;
				} else {
					return criteriaBuilder.equal(root.get("eventCategory").get("categoryString"), categoryString);
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasTagString(String tagString) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (tagString == null || tagString.isBlank()) {
					return null;
				} else {
					return criteriaBuilder.equal(root.get("eventTag").get("tagString"), tagString);
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasSerchingTime(LocalDateTime searchingTime) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (searchingTime == null) {
					return null;
				} else {
					Predicate earliestPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("earliestSessionTime"), searchingTime);
					Predicate latestPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("latestSessionTime"), searchingTime);
					return criteriaBuilder.and(earliestPredicate, latestPredicate);
				}
				
			}
		};
		
	}
}
