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

package org.acumos.csvdatabroker.controller;

import javax.servlet.http.HttpServletResponse;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.service.CSVDatabrokerService;
import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.service.ProtobufServiceImpl;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.Protobuf;
import org.acumos.csvdatabroker.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/admin")
public class AdminCsvDatabrokerController {

private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(AdminCsvDatabrokerController.class);
	
	@Autowired
	@Qualifier("CSVDatabrokerServiceImpl")
	CSVDatabrokerService service;
	
	@ApiOperation(value = "Set the resultset size.")
	@RequestMapping(value = "/setResultsetSize", method = RequestMethod.PUT)
	public Object setResultsetSize(@RequestParam(value = "size", defaultValue = "1", required = true) int size) {
		Result result = null;
		ObjectMapper mapper = new ObjectMapper();
		ConfigurationService confService = ConfigurationService.getInstance();
		try {
			if(size < -1 || size == 0 ){
				throw new ServiceException();
			}
			//1. set the ResultsetSize
			confService.setResultsetSize(size);
			
			result = new Result(HttpServletResponse.SC_OK, "RestulsetSize updated successfully !!!" );
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error while updating ResultsetSize", e);
			result = new Result(HttpServletResponse.SC_EXPECTATION_FAILED, "Error while updating ResultsetSize");
		}
		return result;
	}
	
	@ApiOperation(value = "Reset the offset to the start")
	@RequestMapping(value = "/resetOffset", method = RequestMethod.PUT)
	public Object resetOffset() {
		Result result = null;
		ObjectMapper mapper = new ObjectMapper();
		ConfigurationService confService = ConfigurationService.getInstance();
		int start = 0; 
		try {
			
			Configuration conf  = confService.getConf();
			if(conf.getData_broker_map().getFirst_row().equals(Constants.FIRST_ROW_CONTAINS_FIELDNAMES)) {
				start = 1;
			} 
			confService.setStart(start);
			result = new Result(HttpServletResponse.SC_OK, "Offset reset to 0 successfully !!!" );
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error while restting offset", e);
			result = new Result(HttpServletResponse.SC_EXPECTATION_FAILED, "Error while resetting offset");
		}
		return result;
	}
	
	@ApiOperation(value = "Get resultset offset.")
	@RequestMapping(value = "/getOffset", method = RequestMethod.GET)
	public Object getOffset() {
		Object result = null;
		ConfigurationService confService = ConfigurationService.getInstance();
		try {
			result = confService.getStart();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error while fetching offset", e);
			result = new Result(HttpServletResponse.SC_EXPECTATION_FAILED, "Error while fetching offset");
		}
		return result;
	}
	
	@ApiOperation(value = "Get the environment configuration.")
	@RequestMapping(path = "/getconfigDB", method = RequestMethod.GET)
	@ResponseBody
	public Object getEnvironmentConfiguration() {
		Configuration conf = null;
		try{
			conf =  ConfigurationService.getInstance().getConf();
			if(null != conf.getPassword()){
				conf.setPassword("####");
			}
			return conf;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Environment Configuration is null or not set", e);
			return new Result(HttpServletResponse.SC_EXPECTATION_FAILED, "No environment configuration found!  Please set the Environment configuration.");
		}
		
	}
	
	@ApiOperation(value = "Get the .proto file")
	@RequestMapping(path = "/getProtobuf", method = RequestMethod.GET)
	@ResponseBody
	public Object getProtobuf(){
		Protobuf protobuf = null;
		try {
			protobuf = ProtobufServiceImpl.getInstance().getProtobuf();
			return protobuf.toString();
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Protobuf is null or not set", e);
			return new Result(HttpServletResponse.SC_EXPECTATION_FAILED, "No Protobuf found!  Please set the Environment configuration.");
		}
	}
	
}
