package com.eeit87t3.tickiteasy.linepay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class LinePayController {

	@Autowired
	private LinePayService linePayService;
	
	
//	@GetMapping("/linepay/test")
//	public String testAPI() {
//		//發出請求後回傳的頁面（可能是請求成功頁面aka付款頁面；或請求失敗頁面）
//		 linePayService.requestAPI();
//		return "cwdfunding/linepayOK";
//	}
	
	@ResponseBody
	@PostMapping("api/linepay/request")
    public ResponseEntity<JsonNode> requestLinePay(@RequestBody Map<String, Object> paymentData) {
		//前端傳來payment，使用formMaker()添加完整資訊（如redirect:url等等）
		Map<String, Object> paymentDataFull = linePayService.formMaker(paymentData);
		
		// requestAPI(): 向linepay request api發送請求
		// root: 請求無論成功失敗，linepay request api都會回傳response body
        JsonNode root = linePayService.requestAPI(paymentDataFull);

        if (root != null && root.has("info")) {
            //在這邊跟資料庫互動
        	
        	return ResponseEntity.ok(root);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(root);
        }
    }
	

	
	// line pay頁面，付款成功會導向至這裡
	@GetMapping("/test/linepay/requestOK")
	public String linepayReqOK() {
		//*********************
		// 這裡要放confirmAPI，即完成請款且已與資料庫互動
		// 接著return 付款且請款成功的page!
		
		//*********************
		return "cwdfunding/linepayOK";
	}
	
	
	// line pay付款失敗畫面，付款失敗會導向至這裡
	@GetMapping("/test/linepay/requestNO")
	public String linepayReqReject() {
		return "cwdfunding/linepayNO";
	}
}
