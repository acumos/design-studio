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

package org.acumos.csvdatabroker.service;

import java.io.OutputStream;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("CSVDatabrokerServiceImpl")
public class CSVDatabrokerServiceImpl implements CSVDatabrokerService {

	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(CSVDatabrokerServiceImpl.class);
	
	@Autowired
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@Autowired
	@Qualifier("ProtobufServiceImpl")
	private ProtobufService protoService;
	
	private RemoteScriptExecutor executor ;
	@Override
	public void writeDataTo(OutputStream out) throws ServiceException {
		Configuration conf = null;
		
		try {
			conf = confService.getConf();
			if (null == executor) {
				executor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserName(),
						conf.getPassword(), conf.getRemoteDir(), "default.sh");
			}
			executor.setProtobufService(protoService);
			if (!confService.isShellFileCreated()) {
				executor.createshellFile(conf.getData_broker_map().getScript());
				confService.setShellFileCreated(true);
			}
			executor.executeShell(out);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No environment configuration found!!", e);
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in writeDataTo()", e);
		}
	}

	@Override
	public byte[] getOneRecord() throws ServiceException {
		Configuration conf = null;
		byte[] result = null;
		try {
			conf = confService.getConf();
			if (null == executor) {
				executor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserName(),
						conf.getPassword(), conf.getRemoteDir(), "default.sh");
			}
			executor.setProtobufService(protoService);
			if(!confService.isShellFileCreated()){
				executor.createshellFile(conf.getData_broker_map().getScript());
				confService.setShellFileCreated(true);
			}
			result = executor.executeShell(confService.getStart());
			confService.incrementStart();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No environment configuration found!!", e);
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getData()", e);
		}
		return result;
	}

	/**
	 * Used for Junit Test case
	 * @param executor
	 */
	public void setRemoteScriptExecutor(RemoteScriptExecutor executor){
		this.executor = executor;
	}
}
