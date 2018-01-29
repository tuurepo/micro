package com.ktb.demo.zuulproxyservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class RouteFilter extends ZuulFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonConstants.ZUUL_PROXY_SERVICE_LOGGER);

	@Override
	public String filterType() {
		return "route";
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

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		logger.debug("RouteFilter: "
				+ String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		return null;
	}
}