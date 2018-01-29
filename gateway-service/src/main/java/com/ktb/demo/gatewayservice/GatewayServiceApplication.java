package com.ktb.demo.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableCircuitBreaker
@IntegrationComponentScan
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServiceApplication {
	
	@Bean
	public AlwaysSampler alwaysSampler(){
		 return new AlwaysSampler();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
}

@RestController
class ApiGatewayRestController{
	
	private static final Logger logger = LoggerFactory.getLogger("GatewayService");
	
	public String getCustomerLoadbalanceFallBack(String api, Throwable ex){
		logger.error("Gateway: Hystrix CircuitBreaker", ex);
		return "Gateway: Hystrix CircuitBreaker";
	}
	
	@HystrixCommand(fallbackMethod="getCustomerLoadbalanceFallBack")
	@RequestMapping(method=RequestMethod.GET, value="/customer/{api}")
	public String customerApi(@PathVariable String api){
		logger.debug("Gateway call customer-loadbalance api : "+api);
		//Call Customer Load balance
		ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {
		};
		ResponseEntity<String> exchange = this.restTemplate.exchange("http://customer-loadbalance:8200/"+api, HttpMethod.GET, null, ptr);
		String response = exchange.getBody();
		return response;
	}
	
	public String getPaymentFallBack(String api, Throwable ex){
		logger.error("Gateway: Hystrix CircuitBreaker", ex);
		return "Gateway: Hystrix CircuitBreaker";
	}
	
	@HystrixCommand(fallbackMethod="getPaymentFallBack")
	@RequestMapping(method=RequestMethod.GET, value="/payment/{api}")
	public String paymentApi(@PathVariable String api){
		logger.debug("Gateway call payment-service api : "+api);
		ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {
		};
		ResponseEntity<String> exchange = this.restTemplate.exchange("http://payment-service:8031/"+api, HttpMethod.GET, null, ptr);
		String response = exchange.getBody();
		return response;
	}
	
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	@Autowired
	RestTemplate restTemplate;
}

