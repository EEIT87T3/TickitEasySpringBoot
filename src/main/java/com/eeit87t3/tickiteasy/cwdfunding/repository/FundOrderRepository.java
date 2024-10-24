package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundOrder;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;

public interface FundOrderRepository extends JpaRepository<FundOrder, Integer>{

	@Query(value = "select * from fundingOrder where memberID= :m", nativeQuery = true)
	List<FundOrder> findByMemberID(@Param("m") Integer memberID);
	
	@Query(value = "select * from fundingOrder where tickitID= :t",nativeQuery = true)
	FundOrder findByTickitID(@Param("t") String tickitID);
	
	@Query(value = "select count(*) from fundingOrder where projectID= :p",nativeQuery = true)
	Integer countMembers(@Param("p") Integer projectID);

}
