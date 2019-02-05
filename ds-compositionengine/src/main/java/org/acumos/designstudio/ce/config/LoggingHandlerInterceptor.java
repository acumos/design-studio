/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */

package org.acumos.designstudio.ce.config;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acumos.designstudio.ce.util.DSLogAdapter;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Logging Interceptor called by Spring MVC for each controller handling a RESTful Http request as a server.
 * 
 * Handles logging that should occur before and/or after each HTTP request
 *
 */

@Component
public class LoggingHandlerInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * Invokes LogAdapter.
	 * Intercept the execution of a handler. Called after HandlerMapping determined an appropriate handler object,
	 * but before HandlerAdapter invokes the handler.
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {	
		
		 DSLogAdapter logAdapter = new DSLogAdapter(LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()));
	 	 logAdapter.entering(request);	 	 
		 return true;		
	}	
	
	/**
	 * Callback after completion of request processing, that is, after rendering the view. 
	 * Will be called on any outcome of handler execution, thus allows for proper resource cleanup.
	 * 
	 */
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		final DSLogAdapter adapter = new DSLogAdapter(LoggerFactory.getLogger((MethodHandles.lookup().lookupClass())));
		adapter.exiting();	
	}
	
	

}
