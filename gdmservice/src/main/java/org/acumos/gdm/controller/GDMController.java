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

package org.acumos.gdm.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.acumos.gdm.service.IGDMService;
import org.acumos.gdm.util.EELFLoggerDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/")
public class GDMController {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GDMController.class);
	
	@Autowired
	@Qualifier("GDMServiceImpl")
	private IGDMService gdmService;
	
	@ApiOperation(value = "transforms the protbuf format input data to the output and return outputdata in protobuf format", response = Byte.class)
	@RequestMapping(value = "/mapData", method = RequestMethod.POST)
	public byte[] mapData(@RequestBody byte[] inputData){
		byte[] result = null;
		logger.debug(EELFLoggerDelegator.debugLogger,"mapData : Begin");
		InputStream inputStream = new ByteArrayInputStream(inputData);
		OutputStream outputStream;
		try {
			outputStream = gdmService.mapData(inputStream);
			if(null == outputStream){
				result = new byte[0];
			} else {
				result = ((ByteArrayOutputStream) outputStream).toByteArray();
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error in mapData could not process successfully !!!", e);
		}
		
		
		logger.debug(EELFLoggerDelegator.debugLogger,"mapData : End");
		return result;
	}

}
