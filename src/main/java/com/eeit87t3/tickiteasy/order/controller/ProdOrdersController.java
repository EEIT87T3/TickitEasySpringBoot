package com.eeit87t3.tickiteasy.order.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.service.Impl.ProdOrdersServiceImpl;

import io.micrometer.common.util.StringUtils;

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

		@PostMapping("insert")
		public String prodOrdersInsert(
				@RequestParam(value = "prodOrderID", required = false, defaultValue = "0") Integer prodOrderID,
				@RequestParam(value = "memberID", required = false, defaultValue = "0") int memberID,
				@RequestParam(value = "orderDate", required = false, defaultValue = "1990-01-01") Date orderDate,
				@RequestParam(value = "payments", required = false, defaultValue = "123") String payments,
				@RequestParam(value = "paymentInfo", required = false, defaultValue = "123") String paymentInfo,
				@RequestParam(value = "status", required = false, defaultValue = "未付款") String status,
				@RequestParam(value = "totalAmount", required = false, defaultValue = "0") int totalAmount,
				@RequestParam(value = "shippingStatus", required = false, defaultValue = "未配送") String shippingStatus,
				@RequestParam(value = "shippingID", required = false, defaultValue = "0") int shippingID,
				@RequestParam(value = "recipientName", required = false, defaultValue = "未輸入") String recipientName,
				@RequestParam(value = "address", required = false, defaultValue = "未輸入") String address,
				@RequestParam(value = "phone", required = false, defaultValue = "未輸入") String phone, Model model) {

			ProdOrders prodOrdersBean = new ProdOrders(prodOrderID, memberID, orderDate, payments, paymentInfo,
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

		@GetMapping("delete")
		public String prodOrdersDelete(@RequestParam int prodOrderID, Model model) {

			prodOrdersService.deleteOrderById(prodOrderID);
			return "redirect:/admin/order";
		}

		@PostMapping("update")
		public String prodOrdersUpdate(@RequestParam int prodOrderID,
				@RequestParam(value = "memberID", required = false, defaultValue = "0") int memberID,
				@RequestParam(value = "orderDate", required = false, defaultValue = "1990-01-01") Date orderDate,
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

			if(memberID == 0) {
				ProdOrders selectByprodOrderID = prodOrdersService.selectOrderById(prodOrderID);
				model.addAttribute("prodOrders",selectByprodOrderID);
				return "/order/prodOrdersUpdate";
			}
			
			ProdOrders prodOrdersBean = new ProdOrders(prodOrderID, memberID, orderDate, payments, paymentInfo,
					status, totalAmount, shippingStatus, shippingID, recipientName, address, phone);
			prodOrdersService.updateOrder(prodOrdersBean);
			return "redirect:/admin/order";
		}
		
		@GetMapping("page")
		@ResponseBody
		public Page<ProdOrders> findByPage(@RequestParam Integer pageNumber,@RequestParam Integer records) { 
			
			return prodOrdersService.findAllPage(pageNumber,records);
		}
		
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

}
