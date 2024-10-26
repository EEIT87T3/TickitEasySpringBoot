package com.eeit87t3.tickiteasy.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsSpecification;

/**
 * @author Chuan(chuan13)
 */
public class EventsSearchingDTO {

	private Integer pageNumber = 1;
	private Integer pageSize = 10;
	private String directionString = "ASC";
	private Direction orderByDirection = Direction.ASC;
	private String orderByProperty = "eventID";
	
	private List<Short> statuses;
	private String eventName;
	private String categoryString;
	private String tagString;
	private String cityName;
	private LocalDate searchingDate;
	private LocalDateTime searchingStartTime;
	private LocalDateTime searchingEndTime;
	
	public EventsSearchingDTO() {
	}	
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getDirectionString() {
		return directionString;
	}
	public void setDirectionString(String directionString) {
		this.directionString = directionString;
		switch (directionString) {
			case "ASC":
				this.orderByDirection = Direction.ASC;
				break;
			case "DESC":
				this.orderByDirection = Direction.DESC;
				break;
		}
	}
	public Direction getOrderByDirection() {
		return orderByDirection;
	}
	public void setOrderByDirection(Direction orderByDirection) {
		this.orderByDirection = orderByDirection;
	}
	public String getOrderByProperty() {
		return orderByProperty;
	}
	public void setOrderByProperty(String orderByProperty) {
		this.orderByProperty = orderByProperty;
	}
	public List<Short> getStatuses() {
		return statuses;
	}
	public void setStatuses(List<Short> statuses) {
		this.statuses = statuses;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public String getTagString() {
		return tagString;
	}
	public void setTagString(String tagString) {
		this.tagString = tagString;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public LocalDate getSearchingDate() {
		return searchingDate;
	}
	public void setSearchingDate(LocalDate searchingDate) {
		if (searchingDate != null) {
			this.searchingDate = searchingDate;
			this.searchingStartTime = searchingDate.atTime(0, 0, 0, 0);
			this.searchingEndTime = searchingDate.atTime(23, 59, 59, 999999999);
		}
	}
	public LocalDateTime getSearchingStartTime() {
		return searchingStartTime;
	}
	public void setSearchingStartTime(LocalDateTime searchingStartTime) {
		this.searchingStartTime = searchingStartTime;
	}
	public LocalDateTime getSearchingEndTime() {
		return searchingEndTime;
	}
	public void setSearchingEndTime(LocalDateTime searchingEndTime) {
		this.searchingEndTime = searchingEndTime;
	}
	
	
	public Specification<EventsEntity> getSpecification() {
		return Specification.where(
				EventsSpecification.hasStatuses(statuses)
				.and(EventsSpecification.hasEventName(eventName))
				.and(EventsSpecification.hasCategoryString(categoryString))
				.and(EventsSpecification.hasTagString(tagString))
				.and(EventsSpecification.hasCityName(cityName))
				.and(EventsSpecification.hasSerchingTime(searchingStartTime, searchingEndTime))
				);
	}
	
	public Pageable getPageable() {
		return PageRequest.of(pageNumber - 1, pageSize, orderByDirection, orderByProperty);  // （第幾頁（從 0 起算）, 一頁幾筆, 排序方向, 排序依據欄位）
	}
	
}
