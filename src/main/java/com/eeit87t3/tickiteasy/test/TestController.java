package com.eeit87t3.tickiteasy.test;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeit87t3.tickiteasy.admin.entity.AdminBean;
import com.eeit87t3.tickiteasy.admin.repository.AdminRepo;


@Controller
public class TestController {

	@Autowired
	private AdminRepo adminRepo;
	
	@ResponseBody
	@GetMapping("/test/connection")
	public String getMethodName() {
		Optional<AdminBean> optional = adminRepo.findById(1);
		if (optional.isPresent()) {
			if ("T3管理員".equals(optional.get().getName())) {
				return "連線成功！";
			}
		}
		return "連線失敗。";
	}
	
	@GetMapping("/test/template")
	public String testTemplate() {
		return "test/adminTemplate";
	}
	
}
