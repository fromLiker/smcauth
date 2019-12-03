package com.ibm.fsdsmc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = "com.ibm.fsdsmc.service.*")
//@EnableJpaRepositories(basePackages = "com.ibm.fsdsmc.repository.*") 
//@EntityScan("com.ibm.fsdsmc.entity.*")
@SpringBootApplication
public class FsdsmcApplication {

	public static void main(String[] args) {
		SpringApplication.run(FsdsmcApplication.class, args);
	}

}
