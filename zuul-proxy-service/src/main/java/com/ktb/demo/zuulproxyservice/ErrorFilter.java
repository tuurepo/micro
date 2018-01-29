package com.ktb.demo.zuulproxyservice;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ErrorFilter extends ZuulFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonConstants.ZUUL_PROXY_SERVICE_LOGGER);

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
	    Throwable throwable = RequestContext.getCurrentContext().getThrowable();
		logger.error("ErrorFilter: " + String.format("response status is %d", response.getStatus()), throwable);
		return null;
	}
}
