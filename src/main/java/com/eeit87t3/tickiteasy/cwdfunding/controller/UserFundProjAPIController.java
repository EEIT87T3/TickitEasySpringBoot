package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundOrder;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundOrderService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.util.JWTUtil;

@RestController
@RequestMapping("/member")
public class UserFundProjAPIController {

	@Autowired
	private FundProjService projService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FundOrderService fundOrderService;
	
	
	/* [API] 查詢募資活動(可以by categoryID或不By categoryID)*/
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam(defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false) Integer categoryID,Model model) {
		Integer pageSize = 9;
		Page<FundProjDTO> page = projService.findFundProjByPageAndStatus(pageNumber, pageSize, categoryID);
		return page;
	}

	/* [API] 查詢單筆募資活動by ID */
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjDTOById(projectID);
	}

	/* [API] 接收前端傳的token, 並回傳memberID */
	@GetMapping("/api/fundproject/getMemberID")
	public ResponseEntity<?> getMemberID(
	        @RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();

	    try {
	        // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");
	        System.out.println("token"+token);
	        // 從 Token 中獲取電子郵件
	        String email = jwtUtil.getEmailFromToken(token);
	        System.out.println("email"+email);
	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        System.out.println();
	        if (member == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
	        }
	        
	        // 取得此member的ID
	        String memberID = member.getMemberID().toString();

	        // 進行後續操作...
	        response.put("memberID", memberID);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        // 處理 Token 無效或過期的情況
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
	    }
	}

	/* [API] 查詢募資訂單by memberID */
	@GetMapping("/api/fundOrderList")
	public List<FundOrder> findOrderByMemberID(@RequestParam Integer memberID) {
		return fundOrderService.findFundOrderByMember(memberID);
	}

}
