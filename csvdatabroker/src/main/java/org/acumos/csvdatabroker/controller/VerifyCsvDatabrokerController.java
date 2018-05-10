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

import org.acumos.csvdatabroker.service.ProtobufService;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/verify")
public class VerifyCsvDatabrokerController {

	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(VerifyCsvDatabrokerController.class);
	
	private byte[] protobufMessage;
	private String messageName1;
	
	@Autowired
	@Qualifier("ProtobufServiceImpl")
	private ProtobufService protoService;
	
	@ApiOperation(value = "Convert input data to protobuf format", response = Byte.class)
	@RequestMapping(path = "/getProtobufMessage", method = RequestMethod.GET)
	public Object getProtobufFormattedData(@RequestParam(value="messageName", required=true) String messageName, @RequestParam(value="InputData", required=true) String input, HttpServletResponse response){
		byte[] result = null;
		try{
			result =  protoService.convertToProtobufFormat(messageName, input);
			protobufMessage = result;
			messageName1 = messageName;
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Not able to convert to protobuf format !!!",e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to convert to protobuf format data !!!");
		}
		return result;
	}
	
	@ApiOperation(value = "Convert Protobuf fromatted data into to String")
	@RequestMapping(path = "/getData1", method = RequestMethod.GET)
	public Object getDataForProtobufFormat1(HttpServletResponse response){
		String result = null;
		try{
			result =  protoService.readProtobufFormat(messageName1,protobufMessage);
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Not able to convert the protobuf data !!!", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to convert the protobuf data !!!");
		}
		return result;
	}
	
}
