package com.eeit87t3.tickiteasy.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;
import com.eeit87t3.tickiteasy.order.service.Impl.ProdOrderDetailsServiceImpl;

@Controller
@RequestMapping("/admin/order")
public class ProdOrderDetailsController {
	
	@Autowired
	private ProdOrderDetailsServiceImpl podsi;
	
	@PostMapping("prodOrderDetails")
	public String findAllByIdA(@RequestParam Integer prodOrderID,Model model){
		List<ProdOrderDetails> allByIdA = podsi.findAllByIdA(prodOrderID);
		model.addAttribute("allByIdA", allByIdA);
		return "/order/prodOrderDetails";
	}
}
