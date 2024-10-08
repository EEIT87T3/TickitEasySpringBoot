package com.eeit87t3.tickiteasy.member.repository;

import java.time.LocalDate;
import java.util.List;	

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.eeit87t3.tickiteasy.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
	 // 根據 email 查詢會員（用於登入）
    Member findByEmail(String email);

    // 根據 status 查詢會員列表
    List<Member> findByStatus(Member.MemberStatus status);

    // 查詢最近六個月內註冊的會員
    List<Member> findByRegisterDateAfter(LocalDate date);

    // 查詢年齡範圍內的會員
    List<Member> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}