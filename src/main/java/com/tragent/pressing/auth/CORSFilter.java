package com.tragent.pressing.auth;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		
		HttpServletResponse response = (HttpServletResponse) servletResponse;
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, X-Requested-With, Authorization");
	    response.setHeader("Access-Control-Expose-Headers", "Authorization");
	    filterChain.doFilter(servletRequest, response);
	}

	@Override
	public void destroy() {	}

}
