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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.Result;
import org.springframework.stereotype.Component;

@Component("CSVDatabrokerServiceImpl")
public class CSVDatabrokerServiceImpl implements CSVDatabrokerService {

	@Override
	public void writeDataTo(OutputStream out) throws ServiceException, IOException {
		Configuration conf = null;
		ConfigurationService confService = null;
		RemoteScriptExecutor executor = null;
		try {
			confService = ConfigurationService.getInstance();
			conf = confService.getConf();

			executor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserName(), conf.getPassword(),
					conf.getRemoteDir(), "default.sh");
			if(!confService.isShellFileCreated()){
				executor.createshellFile(conf.getData_broker_map().getScript());
				confService.setShellFileCreated(true);
			}
			// executor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserName(), conf.getPassword(),"/home/techmahindra/vaibhav/", "default.sh");
			executor.executeShell(out);
		} catch (CloneNotSupportedException e) {
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in writeDataTo()");
		}
	}

	@Override
	public byte[] getOneRecord() throws ServiceException {
		ConfigurationService confService = ConfigurationService.getInstance();
		Configuration conf = null;
		RemoteScriptExecutor executor = null;
		byte[] result = null;
		try {
			conf = confService.getConf();
			
			executor = new RemoteScriptExecutor(conf.getHost(), conf.getPort(), conf.getUserName(), conf.getPassword(),
					conf.getRemoteDir(), "default.sh");
			if(!confService.isShellFileCreated()){
				executor.createshellFile(conf.getData_broker_map().getScript());
				confService.setShellFileCreated(true);
			}
			result = executor.executeShell(confService.getStart());
			confService.incrementStart();
		} catch (CloneNotSupportedException e) {
			throw new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getData()");
		}
		return result;
	}

}
