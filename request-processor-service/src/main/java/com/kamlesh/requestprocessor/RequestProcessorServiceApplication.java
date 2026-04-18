package com.kamlesh.requestprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RequestProcessorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequestProcessorServiceApplication.class, args);
	}

}
