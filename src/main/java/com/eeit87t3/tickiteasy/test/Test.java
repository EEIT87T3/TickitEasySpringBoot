package com.eeit87t3.tickiteasy.test;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class Test {

	@Autowired
	private TestRe testRe;
	
	@ResponseBody
	@GetMapping("/d")
	public AdminBean getMethodName() {
		Optional<AdminBean> optional = testRe.findById(1);
		return optional.get();
	}
	
}
