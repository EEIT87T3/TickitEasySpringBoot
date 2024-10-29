package com.eeit87t3.tickiteasy.order.controller;

import java.sql.Date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.catalina.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.service.Impl.ProdOrdersServiceImpl;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;
import com.eeit87t3.tickiteasy.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

@CrossOrigin
@Controller
@RequestMapping("admin/order")
public class ProdOrdersController {
	
	@Autowired
	private ProdOrdersServiceImpl prodOrdersService;
	
	
	// 首頁方法
		@GetMapping
		public String home(@RequestParam(value = "prodOrderID", required = false, defaultValue = "1") Integer id,Model model) {
			// 獲取某頁5筆訂單
			Page<ProdOrders> allPage = prodOrdersService.findAllPage(id);
			model.addAttribute("allPage", allPage);
			return "/order/prodOrdersHomePage"; // 返回首頁				
		}
		//前台結帳頁面
		@GetMapping("ClientSideProdOrderCheckOutCart")
		public String ClientSideProdOrderCheckOutCart() {
			return "/order/ClientSideProdOrderCheckOutCart";
		}
		
		// 後台新增方法
		@PostMapping("insert")
		public String prodOrdersInsert(
				@RequestParam(value = "prodOrderID", required = false, defaultValue = "0") Integer prodOrderID,
				@RequestParam(value = "memberID", required = false, defaultValue = "0") int memberID,
				@RequestParam(value = "orderDate", required = false, defaultValue = "1990-01-01 00:00") String orderDate,
				@RequestParam(value = "payments", required = false, defaultValue = "123") String payments,
				@RequestParam(value = "paymentInfo", required = false, defaultValue = "123") String paymentInfo,
				@RequestParam(value = "status", required = false, defaultValue = "未付款") String status,
				@RequestParam(value = "totalAmount", required = false, defaultValue = "0") int totalAmount,
				@RequestParam(value = "shippingStatus", required = false, defaultValue = "未配送") String shippingStatus,
				@RequestParam(value = "shippingID", required = false, defaultValue = "0") int shippingID,
				@RequestParam(value = "recipientName", required = false, defaultValue = "未輸入") String recipientName,
				@RequestParam(value = "address", required = false, defaultValue = "未輸入") String address,
				@RequestParam(value = "phone", required = false, defaultValue = "未輸入") String phone, Model model) {
			
			orderDate = orderDate.replace("T", " ");
			orderDate += ":00";
			Member member = new Member();
			member.setMemberID(memberID);
			ProdOrders prodOrdersBean = new ProdOrders(prodOrderID, member, Timestamp.valueOf(orderDate), payments, paymentInfo,
					status, totalAmount, shippingStatus, shippingID, recipientName, address, phone);
			
			if(prodOrderID != 0) {
				prodOrdersService.saveOrder(prodOrdersBean);
				Integer prodOrderID1 = 1;
				model.addAttribute("prodOrderID",prodOrderID1);
				return "redirect:/admin/order";
			}
			ProdOrders latestProdOrder = prodOrdersService.findLatestProdOrder();
			model.addAttribute("prodOrderId",latestProdOrder.getProdOrderID());
			return "/order/prodOrdersAdd";
		}
		
		//後台刪除方法
		@GetMapping("delete")
		public String prodOrdersDelete(@RequestParam int prodOrderID, Model model) {

			prodOrdersService.deleteOrderById(prodOrderID);
			return "redirect:/admin/order";
		}
		
		//後台更新方法
		@PostMapping("update")
		public String prodOrdersUpdate(@RequestParam int prodOrderID,
				@RequestParam(value = "memberID", required = false, defaultValue = "0") int memberID,
				@RequestParam(value = "orderDate", required = false, defaultValue = "1990-01-01 00:00") String orderDate,
				@RequestParam(value = "payments", required = false, defaultValue = "123") String payments,
				@RequestParam(value = "paymentInfo", required = false, defaultValue = "123") String paymentInfo,
				@RequestParam(value = "status", required = false, defaultValue = "未付款") String status,
				@RequestParam(value = "totalAmount", required = false, defaultValue = "0") int totalAmount,
				@RequestParam(value = "shippingStatus", required = false, defaultValue = "未配送") String shippingStatus,
				@RequestParam(value = "shippingID", required = false, defaultValue = "0") int shippingID,
				@RequestParam(value = "recipientName", required = false, defaultValue = "未輸入") String recipientName,
				@RequestParam(value = "address", required = false, defaultValue = "未輸入") String address,
				@RequestParam(value = "phone", required = false, defaultValue = "未輸入") String phone,
				Model model) {
			
			orderDate = orderDate.replace("T", " ");
			orderDate += ":00";
			Member member = new Member();
			member.setMemberID(memberID);
			if(memberID == 0) {
				ProdOrders selectByprodOrderID = prodOrdersService.selectOrderById(prodOrderID);
				model.addAttribute("prodOrders",selectByprodOrderID);
				return "/order/prodOrdersUpdate";
			}
			
			ProdOrders prodOrdersBean = new ProdOrders(prodOrderID, member, Timestamp.valueOf(orderDate), payments, paymentInfo,
					status, totalAmount, shippingStatus, shippingID, recipientName, address, phone);
			prodOrdersService.updateOrder(prodOrdersBean);
			return "redirect:/admin/order";
		}
		
		//後台查詢分頁方法
		@GetMapping("page")
		@ResponseBody
		public Page<ProdOrders> findByPage(@RequestParam Integer pageNumber,@RequestParam Integer records) { 
			
			return prodOrdersService.findAllPage(pageNumber,records);
		}
		
		//後台查詢分頁方法 (依日期)
		@PostMapping("findByProdidOrMemberidOrDate")
		@ResponseBody
		public Page<ProdOrders> findByDate(
				@RequestParam String name,
				@RequestParam String number,
				@RequestParam int pageNumber,
				@RequestParam Integer records){
			
			switch (name) {
			case "訂單編號":
				if(StringUtils.isNotBlank(number)) {
					int numberProdOrderId = Integer.parseInt(number);					
					return prodOrdersService.findByProdOrdersId(numberProdOrderId, pageNumber,records);
				}
				return prodOrdersService.findAllPage(pageNumber);
			case "會員編號":
				if(StringUtils.isNotBlank(number)) {
					int	numberMemberId = Integer.parseInt(number);					
					return prodOrdersService.findByMemberId(numberMemberId, pageNumber,records);
				}
				return prodOrdersService.findAllPage(pageNumber);
			case "訂單日期":
				return prodOrdersService.findByDate(number,pageNumber,records);
			}
			
			return null;
		}

		
		@Autowired
		JWTUtil jwtUtil;
		//串接綠界
		@PostMapping("ECPay")
		@ResponseBody
		public String ECPay(
				@RequestParam("ticketTypesCartToCheckoutJson") String ticketTypesCartToCheckoutJson,
				@RequestParam("checkoutItems") String checkoutItem,
                @RequestParam("totalAmount") String totalAmount,
                @RequestParam("name") String name,
                @RequestParam("email") String email,
                @RequestParam("phone") String phone,
                @RequestParam("address") String address,
                @RequestParam("paymentMethod") String paymentMethod,
                @RequestParam("jwtToken") String jwtToken, // token
                Model model) throws JsonMappingException, JsonProcessingException {
			String memberEmail = jwtUtil.getEmailFromToken(jwtToken);
			
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String,Object>> ticketTypesCartToCheckoutJsons  = objectMapper.readValue(ticketTypesCartToCheckoutJson, List.class); //將json轉成list
		 	List<Map<String,Object>> checkoutItems  = objectMapper.readValue(checkoutItem, List.class); //將json轉成list
		 	String form = prodOrdersService.ECPay(ticketTypesCartToCheckoutJsons,checkoutItems, totalAmount,memberEmail);

		 	String html = "<html><body>" + form + 
	                  "<script type='text/javascript'>document.forms[0].submit();</script>" +
	                  "</body></html>";

			return html;
		}
		
		//綠界回傳
		@PostMapping("ECPayReturn")
		@ResponseBody
		public String ECPayReturn(@RequestParam Map<String, String> paymentResult) throws MessagingException, ParseException {
			
	        String merchantTradeNo = paymentResult.get("MerchantTradeNo"); //（商店訂單編號）寄給消費者用此
	        String tradeNo = paymentResult.get("TradeNo"); //（綠界的交易編號）
	        String rtnCode = paymentResult.get("RtnCode"); //（交易狀態）
	        String tradeAmt = paymentResult.get("TradeAmt"); // （交易金額）
	        String paymentDate =paymentResult.get("PaymentDate");//（付款時間）
	        String paymentType = paymentResult.get("PaymentType");//（付款方式）
	        String tradeDate = paymentResult.get("TradeDate"); //（訂單成立時間）

	        if("1".equals(rtnCode)) {//如果綠界回傳1 代表交易成功
	        	prodOrdersService.emailSend(5,merchantTradeNo,tradeDate,tradeAmt,paymentType); //memberId先填死測試
	        	ProdOrders prodOrders = prodOrdersService.findBypaymentInfo(merchantTradeNo);
	        	tradeDate = tradeDate.replace("/", "-");
	        	prodOrders.setOrderDate(Timestamp.valueOf(tradeDate));
	        	prodOrders.setPayments(paymentType);
	        	prodOrders.setPaymentInfo(merchantTradeNo);
	        	prodOrders.setStatus("已付款");
	        	prodOrders.setTotalAmount(Integer.valueOf(tradeAmt));
	        	prodOrdersService.saveOrder(prodOrders);
	        	return "1|OK";
	        }else {
	        	prodOrdersService.emailSend(5,merchantTradeNo,tradeDate,tradeAmt,paymentType); //memberId先填死測試
	        	ProdOrders prodOrders = new ProdOrders();
	        	Member member = new Member();
				member.setMemberID(5);
	        	prodOrders.setMember(member); //memberId先填死測試
	        	tradeDate = tradeDate.replace("/", "-");
	        	prodOrders.setOrderDate(Timestamp.valueOf(tradeDate));
	        	prodOrders.setPayments(paymentType);
	        	prodOrders.setPaymentInfo(merchantTradeNo);
	        	prodOrders.setStatus("未付款");
	        	prodOrders.setTotalAmount(Integer.valueOf(tradeAmt));
	        	prodOrdersService.saveOrder(prodOrders);
	        	return "2|false";
	        }
	        
		}
		
		//生成圖表 (付款狀態:待付款、已付款)
		@PostMapping("chartjs")
		public ResponseEntity<Map<String, Integer>> chartjs() {
			Map<String, Integer> result = new HashMap<>();
		    result.put("paid", prodOrdersService.chartjsFindStatusPaid());
		    result.put("notPaid", prodOrdersService.chartjsFindStatusNotPaid());
		    return ResponseEntity.ok(result);
		}
		
		//串接LinePay
		@PostMapping("LinePay")
		public void LinePay(@RequestParam("ticketTypesCartToCheckoutJson") String ticketTypesCartToCheckoutJson,
				@RequestParam("checkoutItems") String checkoutItem,
                @RequestParam("totalAmount") String totalAmount,
                @RequestParam("name") String name,
                @RequestParam("email") String email,
                @RequestParam("phone") String phone,
                @RequestParam("address") String address,
                @RequestParam("paymentMethod") String paymentMethod,
                @RequestParam("jwtToken") String jwtToken, // token
                HttpServletResponse response) throws Exception {
			String memberEmail = jwtUtil.getEmailFromToken(jwtToken);
			
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String,Object>> ticketTypesCartToCheckoutJsons  = objectMapper.readValue(ticketTypesCartToCheckoutJson, List.class); //將json轉成list
		 	List<Map<String,Object>> checkoutItems  = objectMapper.readValue(checkoutItem, List.class); //將json轉成list
			String linePay = prodOrdersService.LinePay(ticketTypesCartToCheckoutJsons, checkoutItems, totalAmount, memberEmail);
			
			int indexOf = linePay.indexOf("web") + 6;
			linePay = linePay.substring(indexOf);
            int indexOf2 = linePay.indexOf("\"");
            linePay = linePay.substring(0, indexOf2);
  
            response.sendRedirect(linePay);
		}
		
		
		
		@GetMapping("LinePayReturn")
		public String LinePay(@RequestParam String transactionId,@RequestParam String orderId) throws Exception{
			System.out.println("transactionId:"+transactionId);
			System.out.println("orderId:"+orderId);
			
			String targetUrl = "https://sandbox-api-pay.line.me/v3/payments/" + transactionId + "/confirm";

			String linePayReturn = prodOrdersService.LinePayReturn(targetUrl, orderId, transactionId);
		   
	        return "redirect:http://localhost:8080/TickitEasy/user/clientSide/orderPaymentCompleted";
		}
		
}
