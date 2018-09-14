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
import org.acumos.csvdatabroker.util.LocalScriptExecutor;
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
	
	private RemoteScriptExecutor remoteExecutor;
	
	private LocalScriptExecutor localExecutor;
	@Override
	public void writeDataTo(OutputStream out) throws ServiceException {
		Configuration conf = null;
		
		try {
			conf = confService.getConf();
			if(conf.isRemoteFile()) { 
				if (null == remoteExecutor) {
					remoteExecutor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserId(),
							conf.getPassword(), conf.getRemoteDir(), "default.sh");
				}
				remoteExecutor.setProtobufService(protoService);
				remoteExecutor.getData(out, conf.getRemoteFilePath());
			} else {
				if(null == localExecutor) {
					localExecutor = new LocalScriptExecutor(conf.getLocalPath(),protoService);
				}
				localExecutor.getData(out, conf.getLocalFilePath());
			}
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
			if(conf.isRemoteFile()){ //data file is at remote host.
				if(null == remoteExecutor) {
					remoteExecutor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserId(),
								conf.getPassword(), conf.getRemoteDir(), "default.sh");
					remoteExecutor.setProtobufService(protoService);
				}
				result = remoteExecutor.getData(confService.getStart(), conf.getRemoteFilePath());
			} else { //data file is local.
				if(null == localExecutor) {
					localExecutor = new LocalScriptExecutor(conf.getLocalPath(),protoService);
				}
				result = localExecutor.getData(confService.getStart(), conf.getLocalFilePath());
			}
			confService.incrementStart();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No environment configuration found!!", e);
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getData()", e);
		}
		return result;
	}

	/**
	 * Used for Junit Test case
	 * @param remoteExecutor
	 * 		This method accepts remoteExecutor
	 */
	public void setRemoteScriptExecutor(RemoteScriptExecutor remoteExecutor){
		this.remoteExecutor = remoteExecutor;
	}
}
