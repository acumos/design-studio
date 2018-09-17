/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.sqldatabroker.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.service.ConfigurationService;
import org.acumos.sqldatabroker.service.ProtobufService;
import org.acumos.sqldatabroker.service.ProtobufServiceImpl;
import org.acumos.sqldatabroker.service.SQLDatabrokerService;
import org.acumos.sqldatabroker.util.EELFLoggerDelegator;
import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

@RestController
public class SQLDatabrokerController {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SQLDatabrokerController.class);
	
	@Autowired
	@Qualifier("SQLDatabrokerServiceImpl")
	private SQLDatabrokerService service;
	
	@Autowired
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@Autowired
	@Qualifier("ProtobufServiceImpl")
	private ProtobufService protoService;
	
	@ApiOperation(value = "Set the environment configuration.", response = Result.class)
	@RequestMapping(path = "/configDB", method = RequestMethod.PUT)
	@ResponseBody
	public Object configureEnvironment(@RequestBody Configuration conf, HttpServletResponse response) {
		Result result = null;
		try {
			//1. set the configuration
			confService.setConf(conf);
			//2. process protobuf 
			protoService.processProtobuf(conf);
			result = new Result(HttpServletResponse.SC_OK, "Environment configured successfully !!!" );
			
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error while setting Environment configuration", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result = new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while setting Environment configuration");
		}
		return result;
	}
	
	@ApiOperation(value = "Get the Data")
	@RequestMapping(path = "/pullData", method = RequestMethod.GET)
	public Object pullData(HttpServletResponse response) {
		
		int resultsetSize = confService.getResultsetSize();
		if(resultsetSize == 1){
			byte[] result = null;
			try {
				result = service.getOneRecord();
			} catch (Exception e) {
				logger.error(EELFLoggerDelegator.errorLogger, "No Data Found !!!", e);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No Data Found !!!");
			}
			return result;
		} else if (resultsetSize == -1){
	        return new StreamingResponseBody() {
	            @Override
	            public void writeTo (OutputStream out) throws IOException {
	            	try {
	            		service.writeDataTo(out);
	            	} catch (Exception e){
	            		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            		out.write((new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()).toString()).getBytes());
	            	}
	            }
	        };
			} else {
				//TODO : Need to implement to return set of n records. 
				return new Result();
			}
		
	}
	
}
