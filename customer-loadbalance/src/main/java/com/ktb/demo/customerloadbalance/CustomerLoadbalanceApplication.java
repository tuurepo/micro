package com.ktb.demo.customerloadbalance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import feign.hystrix.FallbackFactory;

@IntegrationComponentScan
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class CustomerLoadbalanceApplication {

	@Bean
	public AlwaysSampler alwaysSampler(){
		 return new AlwaysSampler();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerLoadbalanceApplication.class, args);
	}
}

@RefreshScope
@RestController
class ReservationApiGatewayRestController{
	
	private static final Logger logger = LoggerFactory.getLogger("CustomerLoadBalance");
	
	@Value("${server.port}")
	private String port;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(method=RequestMethod.GET, value="/message")
	public String getMessage(){
		logger.debug("This is loadbalancer [api /message]");
		String response = customerService.message();
		return "Load Balance Port: "+port+" call customer response: "+response;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/randomerror")
	public String randomError(){
		logger.debug("This is loadbalancer [api /randomerror]");
		String response = customerService.randomError();
		return response;
	}
	
}

@FeignClient(name = "customer-service", fallbackFactory = HystrixClientFallbackFactory.class)
interface CustomerService{
	
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	String message();
	
	@RequestMapping(value = "/randomerror", method = RequestMethod.GET)
	String randomError();
}

@Component
class HystrixClientFallback implements CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger("CustomerLoadBalance");
    @Override
	public String message(){
    	logger.error("LoadBalance [Message]: Hystrix CircuitBreaker");
		return "LoadBalance [Message]: Hystrix CircuitBreaker";
    }
    
	@Override
	public String randomError() {
		logger.error("LoadBalance [RandomError]: Hystrix CircuitBreaker");
		return "LoadBalance [RandomError]: Hystrix CircuitBreaker";
	}
}


@Component
class HystrixClientFallbackFactory implements FallbackFactory<CustomerService> {
	
	private static final Logger logger = LoggerFactory.getLogger("CustomerLoadBalance");
	
	@Override
	public CustomerService create(Throwable cause) {
		return new CustomerService() {

			@Override
			public String message(){
		    	logger.error("LoadBalance [Message]: Hystrix CircuitBreaker", cause);
				return "LoadBalance [Message]: Hystrix CircuitBreaker";
		    }
		    
			@Override
			public String randomError() {
				logger.error("LoadBalance [RandomError]: Hystrix CircuitBreaker", cause);
				return "LoadBalance [RandomError]: Hystrix CircuitBreaker";
			}
		};
	}
}

/*
@RestController
@RibbonClient(name = "customer-service", configuration = RibbonConfiguration.class)
class ReservationApiGatewayRestController{

	private static final Logger logger = LoggerFactory.getLogger("CustomerLoadBalance");
	
	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;
	
	public String getMessageFallBack(Throwable ex){
		logger.error("LoadBalance [Message]: Hystrix CircuitBreaker", ex);
		return "LoadBalance [Message]: Hystrix CircuitBreaker";
	}
	
	public String getRandomErrorFallBack(Throwable ex){
		logger.error("LoadBalance [RandomError]: Hystrix CircuitBreaker", ex);
		return "LoadBalance [RandomError]: Hystrix CircuitBreaker";
	}
	
	@HystrixCommand(fallbackMethod="getMessageFallBack")
	@RequestMapping(method=RequestMethod.GET, value="/message")
	public String getMessage(){
		logger.debug("This is loadbalancer [api /message]");
		ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {
		};
		ResponseEntity<String> exchange = this.restTemplate.exchange("http://customer-service/message", HttpMethod.GET, null, ptr);
		String response = exchange.getBody();
		return response;
	}
	
	@HystrixCommand(fallbackMethod="getRandomErrorFallBack")
	@RequestMapping(method=RequestMethod.GET, value="/randomerror")
	public String getRandomError(){
		logger.debug("This is loadbalancer [api /randomerror]");
		ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {
		};
		ResponseEntity<String> exchange = this.restTemplate.exchange("http://customer-service/randomerror", HttpMethod.GET, null, ptr);
		String response = exchange.getBody();
		return response;
	}
	
}

class RibbonConfiguration {

	  @Autowired
	  IClientConfig ribbonClientConfig;

	  @Bean
	  public IPing ribbonPing(IClientConfig config) {
	    return new PingUrl();
	  }

	  @Bean
	  public IRule ribbonRule(IClientConfig config) {
	    return new AvailabilityFilteringRule();
	  }

}*/