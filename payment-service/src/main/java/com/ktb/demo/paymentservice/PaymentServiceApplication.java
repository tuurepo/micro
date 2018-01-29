package com.ktb.demo.paymentservice;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@IntegrationComponentScan
@EnableDiscoveryClient
@SpringBootApplication
public class PaymentServiceApplication {
	
	@Bean
	public AlwaysSampler alwaysSampler(){
		 return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
}

@RefreshScope
@RestController	
class CustomerServiceRestController {
	
	private static final Logger logger = LoggerFactory.getLogger("PaymentService");
	
	private static int COUNT = 0;

	@Value("${paymentmessage}")
	private String message;
	
	@Value("${server.port}")
	private String port;
	
	@RequestMapping("/message")
	public String getMessage(){
		String msg = "This is Payment service [port:"+this.port+"] \r\n message="+this.message + "\r\n Count="+String.valueOf(++COUNT);
		logger.debug(msg);
		return msg;
	}
	
	@RequestMapping("/randomerror")
	public String getRandomError(){
		int randomPercent = randomPercent();
		String msg = "This is Payment service [port:"+this.port+"] \r\n random error :: randomPercent="+randomPercent;
		logger.debug(msg);
		if(randomPercent < 50) {
			logger.error("Random error < 50");
			throw new RuntimeException();
		}
		return msg;
	}
	
	private int randomPercent() {
		Random r = new Random();
	    return r.nextInt(100);
	}
	
}