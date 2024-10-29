package com.eeit87t3.tickiteasy.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.service.TicketTypesProcessingService;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.order.entity.ClientSideOrderDetails;
import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;
import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.service.ProdOrderDetailsService;
import com.eeit87t3.tickiteasy.order.service.ProdOrdersService;
import com.eeit87t3.tickiteasy.order.service.Impl.ProdOrderDetailsServiceImpl;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;
import com.eeit87t3.tickiteasy.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("user/clientSide")
public class ClientSideOrder {
	
	@Autowired
	private ProdOrdersService prodOrdersService;
	@Autowired
	private ProdOrderDetailsService prodOrderDetailsService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TicketTypesProcessingService ticketTypesProcessingService;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private MemberService memberService;
	
	//訂單查詢頁面
	@GetMapping("order")
	public String order() {
		return "order/ClientSideOrder";
	}
	
	//訂單
	@GetMapping("selectMemberOrder")
	@ResponseBody
	public ResponseEntity<List<ProdOrders>>  selectMemberOrder(@RequestHeader("Authorization") String authHeader) throws JsonProcessingException {
		String token = authHeader.replace("Bearer ", "").trim();
		String emailFromToken = jwtUtil.getEmailFromToken(token);
		Member member = memberService.findByEmail(emailFromToken);
		List<ProdOrders> selectByMemberID = prodOrdersService.findOrdersByMemberId(member.getMemberID());
		return ResponseEntity.ok(selectByMemberID);
	}
	
	//訂單詳細
	@PostMapping("orderDetail")
	public String orderDetail(@RequestParam("orderId") Integer orderId,Model model) {
		List<ProdOrderDetails> allByIdA = prodOrderDetailsService.findAllByIdA(orderId); 
		List lists = new ArrayList();
		for(ProdOrderDetails pod: allByIdA) {
			ClientSideOrderDetails clientSideOrderDetails = new ClientSideOrderDetails();
			if(pod.getProductId() != null) {
				ProductEntity productById = productService.findProductById(pod.getProductId());
				clientSideOrderDetails.setPrice(pod.getPrice());
				clientSideOrderDetails.setQuantity(pod.getQuantity());
				clientSideOrderDetails.setProductById(productById);
				lists.add(clientSideOrderDetails);
			}
			if(pod.getTicketTypeId() != null) {
				TicketTypesEntity ticketTypeById = ticketTypesProcessingService.findById(pod.getTicketTypeId());
				clientSideOrderDetails.setPrice(pod.getTicketPrice());
				clientSideOrderDetails.setQuantity(pod.getTicketQuantity());
				clientSideOrderDetails.setTicketTypeById(ticketTypeById);
				lists.add(clientSideOrderDetails);
			}
			
		}
		model.addAttribute("orderDetail", lists);
		return "order/ClientSideOrderDetails";
	}
	
	//訂單詳細
	@GetMapping("orderPaymentCompleted")
	public String orderPaymentCompleted() {
		return "order/ClientSideOrderPaymentCompleted";
    
	}
}
