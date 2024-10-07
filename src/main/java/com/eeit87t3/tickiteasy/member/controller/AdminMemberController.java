package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminMemberController {

    @Autowired
    private MemberService memberService;

    // 會員列表頁面
    @GetMapping("/member")
    public String memberList() {
        return "admin/memberList"; // 返回會員列表頁面
    }

    // 會員圖表分析頁面
    @GetMapping("/member/analytics")
    public String memberAnalytics() {
        return "admin/memberAnalytics"; // 返回會員分析頁面
    }

    // 取得所有會員的 JSON 資料
    @GetMapping("/member/api")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/member/api/age-distribution")
    public ResponseEntity<Map<String, Long>> getAgeDistribution() {
        List<Member> members = memberService.getAllMembers();
        // 根據生日計算年齡，並分組統計
        Map<String, Long> ageDistribution = members.stream().collect(Collectors.groupingBy(
            member -> calculateAgeRange(member.getBirthDate()), Collectors.counting()
        ));
        return ResponseEntity.ok(ageDistribution);
    }

    @GetMapping("/member/api/registration-trend")
    public ResponseEntity<Map<String, Long>> getRegistrationTrend() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Member> recentMembers = memberService.getRecentRegisteredMembers(sixMonthsAgo);
        // 按月份分組並統計
        Map<String, Long> registrationTrend = recentMembers.stream().collect(Collectors.groupingBy(
            member -> member.getRegisterDate().getMonth().toString(), Collectors.counting()
        ));
        return ResponseEntity.ok(registrationTrend);
    }


    // 更改會員狀態
    @PutMapping("/member/api/{memberId}/status")
    public ResponseEntity<String> updateMemberStatus(@PathVariable Integer memberId, @RequestBody Map<String, String> request) {
        Optional<Member> member = memberService.getMemberById(memberId);
        if (member.isPresent()) {
            String newStatus = request.get("status");
            memberService.updateMemberStatus(memberId, Member.MemberStatus.valueOf(newStatus));
            return ResponseEntity.ok("會員狀態已更新");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 移除會員頭貼
    @DeleteMapping("/member/api/{memberId}/profile-pic")
    public ResponseEntity<String> removeProfilePic(@PathVariable Integer memberId) {
        try {
            memberService.removeProfilePic(memberId);
            return ResponseEntity.ok("會員頭貼已移除");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("移除頭貼失敗");
        }
    }

    // 輔助方法：計算年齡範圍
    private String calculateAgeRange(LocalDate birthDate) {
        int age = LocalDate.now().getYear() - birthDate.getYear();
        if (age < 20) {
            return "20歲以下";
        } else if (age < 30) {
            return "20-29";
        } else if (age < 40) {
            return "30-39";
        } else if (age < 50) {
            return "40-49";
        } else {
            return "50 and above";
        }
    }
}