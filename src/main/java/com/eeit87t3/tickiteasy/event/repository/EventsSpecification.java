package com.eeit87t3.tickiteasy.event.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 封裝 Events 的條件邏輯。
 * 
 * @author Chuan (chuan13)
 */
// 封裝查詢邏輯，不需 Spring 管理
public class EventsSpecification {
	
	public static Specification<EventsEntity> hasStatuses(List<Short> statuses) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (statuses == null || statuses.size() == 0) {
					return criteriaBuilder.conjunction();
				} else {
					CriteriaBuilder.In<Short> inPredicate = criteriaBuilder.in(root.get("status"));
					for (Short status : statuses) {
					    inPredicate.value(status);
					}
					return inPredicate;
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasEventName(String eventName) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("null")
			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (eventName == null || eventName.isBlank()) {
					return criteriaBuilder.conjunction();
				} else {
					return criteriaBuilder.like(root.get("eventName"), "%" + eventName + "%");
				}
			}
		};
	}

	public static Specification<EventsEntity> hasCategoryString(String categoryString) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("null")
			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (categoryString == null || categoryString.isBlank()) {
					return criteriaBuilder.conjunction();
				} else {
					return criteriaBuilder.equal(root.get("eventCategory").get("categoryString"), categoryString);
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasTagString(String tagString) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("null")
			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (tagString == null || tagString.isBlank()) {
					return criteriaBuilder.conjunction();
				} else {
					return criteriaBuilder.equal(root.get("eventTag").get("tagString"), tagString);
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasCityName(String cityName) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (cityName == null || cityName.isBlank()) {
					return criteriaBuilder.conjunction();
				} else {
					return criteriaBuilder.like(root.get("address"), cityName + "%");
				}
			}
		};
	}
	
	public static Specification<EventsEntity> hasSerchingTime(LocalDateTime searchingStartTime, LocalDateTime searchingEndTime) {
		return new Specification<EventsEntity>() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("null")
			@Override
			public Predicate toPredicate(Root<EventsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				if (searchingStartTime == null && searchingEndTime == null) {
					return criteriaBuilder.conjunction();
				} else {
					
					// 狀況 1：查詢開始時間在活動期間內
					Predicate startTimeBetween = criteriaBuilder.and(
						criteriaBuilder.lessThanOrEqualTo(root.get("eventStartTime"), searchingStartTime),  // 活動開始時間比查詢開始時間早
						criteriaBuilder.greaterThanOrEqualTo(root.get("eventEndTime"), searchingStartTime)  // 活動結束時間比查詢開始時間晚
						);
					
					// 狀況 2：查詢結束時間在活動期間內
					Predicate endTimeBetween = criteriaBuilder.and(
						criteriaBuilder.lessThanOrEqualTo(root.get("eventStartTime"), searchingEndTime),  // 活動開始時間比查詢結束時間早
						criteriaBuilder.greaterThanOrEqualTo(root.get("eventEndTime"), searchingEndTime)  // 活動結束時間比查詢結束時間晚
						);
					
					// 狀況 3：查詢時間完全包含活動期間
					Predicate timeCover = criteriaBuilder.and(
						criteriaBuilder.greaterThanOrEqualTo(root.get("eventStartTime"), searchingStartTime),  // 活動開始時間比查詢開始時間晚
						criteriaBuilder.lessThanOrEqualTo(root.get("eventEndTime"), searchingEndTime)  // 活動結束時間比查詢結束時間早
						);
					return criteriaBuilder.or(startTimeBetween, endTimeBetween, timeCover);
				}
			}
		};
	}
}
