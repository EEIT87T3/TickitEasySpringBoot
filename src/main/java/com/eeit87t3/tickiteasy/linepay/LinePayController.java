package com.eeit87t3.tickiteasy.linepay;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundOrderService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.cwdfunding.testemail.TestEmailService;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class LinePayController {

	@Autowired
	private LinePayService linePayService;
	
	@Autowired
	private FundOrderService fundOrderService;
	
	@Autowired
	private FundProjService fundProjService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private TestEmailService testEmailService;
	
	@ResponseBody
	@PostMapping("api/linepay/request")
    public ResponseEntity<JsonNode> requestLinePay(@RequestBody Map<String, Object> paymentData) {
		//前端傳來payment，使用lineformMaker()整理必要資訊（如redirect:url等等）
		Map<String, Object> paymentDataFull = linePayService.lineformMaker(paymentData);
		
		// requestAPI(): 向linepay request api發送請求
		// root: 請求無論成功失敗，linepay request api都會回傳response body
        JsonNode root = linePayService.requestAPI(paymentDataFull);

        /*寄信邏輯變數初始化*/
        Integer projectID = Integer.parseInt(paymentData.get("projectID").toString());
        		
        
		/* BUG 只要有請求成功到付款頁面，不論是否有付款成功皆會存進資料庫 */
        if ("0000".equals(root.get("returnCode").asText())) {
            // 在這邊跟資料庫互動
        	boolean isSaved = fundOrderService.saveFundOrder(paymentData, paymentDataFull);
        	// 寄募資成功信
        	if(isSaved) {
        		boolean isReached = fundOrderService.isReached(projectID); // 達目標金額
	        	if (isReached) {
					List<String> donatedMembersEmail = testEmailService.findDonatedMembersEmail(projectID);
					//System.out.println("寄信list:"+donatedMembersEmail);
					int count = 0;
					for (String email : donatedMembersEmail) {
						count = count + 1 ;
						testEmailService.sendDonateSuccessEmail(email);
					}
					//System.out.println("迴圈次數應和寄信名單人數相同："+count);
				}
        	}
        	return ResponseEntity.ok(root);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(root);
        }
    }
	


}
