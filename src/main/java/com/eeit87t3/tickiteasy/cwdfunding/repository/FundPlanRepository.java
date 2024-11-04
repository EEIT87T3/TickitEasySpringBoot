package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;

/**
 * @author TingXD (chen19990627)
 */
public interface FundPlanRepository extends JpaRepository<FundPlan, Integer> {

	@Query("from FundPlan plan where plan.fundProj.projectID =:id")
	List<FundPlan> findByProjectID(Integer id);
}
