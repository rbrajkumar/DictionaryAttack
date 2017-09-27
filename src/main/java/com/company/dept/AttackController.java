package com.company.dept;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttackController {

	@RequestMapping("/dictionary-attack")
	public String process(){
		return "started";
	}
}
