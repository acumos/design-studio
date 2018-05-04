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

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.vo.Configuration;

public class ConfigurationService {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ConfigurationService.class);
	
	private static ConfigurationService configuration = new ConfigurationService();
	
	private Configuration conf;
	
	private int resultsetSize;
	
	private int start; 
	
	private boolean shellFileCreated;
	
	private ConfigurationService(){
		resultsetSize = 1; //Default value 1
		start = 0;
	}
	
	public static ConfigurationService getInstance(){
		return configuration;
	}

	/**
	 * To increment the next sart by resultsetSize.
	 */
	public void incrementStart(){
		start = start + resultsetSize;
	}
	
	/**
	 * @return the conf
	 * @throws CloneNotSupportedException 
	 */
	public Configuration getConf() throws CloneNotSupportedException {
		return (null != conf)? (Configuration)conf.clone() : null;
	}

	/**
	 * @param conf the conf to set
	 */
	public void setConf(Configuration conf) {
		this.conf = conf;
		if(conf.getData_broker_map().getFirst_row().equals(Constants.FIRST_ROW_CONTAINS_FIELDNAMES)) {
			start = 1;
		} else {
			start = 0;
		}
	}

	/**
	 * @return the resultsetSize
	 */
	public int getResultsetSize() {
		return resultsetSize;
	}

	/**
	 * @param resultsetSize the resultsetSize to set
	 */
	public void setResultsetSize(int resultsetSize) {
		this.resultsetSize = resultsetSize;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the shellFileCreated
	 */
	public boolean isShellFileCreated() {
		return shellFileCreated;
	}

	/**
	 * @param shellFileCreated the shellFileCreated to set
	 */
	public void setShellFileCreated(boolean shellFileCreated) {
		this.shellFileCreated = shellFileCreated;
	}
	
	
}
