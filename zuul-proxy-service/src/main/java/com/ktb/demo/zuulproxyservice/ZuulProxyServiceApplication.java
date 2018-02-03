package com.ktb.demo.zuulproxyservice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.exception.HystrixTimeoutException;

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

@Component
class MyFallbackProvider implements FallbackProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonConstants.ZUUL_PROXY_SERVICE_LOGGER);

    @Override
    public String getRoute() {
        return "*";
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
        	@Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }
            @Override
            public InputStream getBody() throws IOException {
            	logger.error("getBody fallback");
                return new ByteArrayInputStream("fallback".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

	@Override
	public ClientHttpResponse fallbackResponse(Throwable cause) {
		logger.error("Throwable: "+cause.getMessage(), cause);
		if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.OK);
        }
	}

	@Override
	public ClientHttpResponse fallbackResponse() {
		logger.error("fallbackResponse!!!");
		return response(HttpStatus.OK);
	}
}
