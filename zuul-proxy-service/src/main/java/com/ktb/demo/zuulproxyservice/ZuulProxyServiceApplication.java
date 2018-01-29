package com.ktb.demo.zuulproxyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;

@IntegrationComponentScan
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulProxyServiceApplication {
	
	@Bean
	public AlwaysSampler alwaysSampler(){
		 return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(ZuulProxyServiceApplication.class, args);
	}
}

