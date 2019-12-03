package com.ibm.fsdsmc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class LoginTestCase {

	@RequestMapping("/hello")
	@ResponseBody
	String home() {
		return "Hello ,spring security!";
	}			
}
