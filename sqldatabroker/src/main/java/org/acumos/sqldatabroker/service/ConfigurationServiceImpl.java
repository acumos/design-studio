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

package org.acumos.sqldatabroker.service;

import org.acumos.sqldatabroker.util.DatabrokerConstants;
import org.acumos.sqldatabroker.util.EELFLoggerDelegator;
import org.acumos.sqldatabroker.vo.Configuration;
import org.springframework.stereotype.Component;


@Component("ConfigurationServiceImpl")
public class ConfigurationServiceImpl implements ConfigurationService {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ConfigurationServiceImpl.class);
	
	
	private Configuration conf;
	
	private int resultsetSize;
	
	private int start; 
	
	private boolean shellFileCreated;
	
	/**
	 * To increment the next sart by resultsetSize.
	 */
	public void incrementStart(){
		start = start + resultsetSize;
	}
	
	/**
	 * @return the conf
	 * 				return Configuration
	 * @throws CloneNotSupportedException 
	 * 		This method throws CloneNotSupportedException 
	 */
	public Configuration getConf() throws CloneNotSupportedException {
		return (null != conf)? (Configuration)conf.clone() : null;
	}

	/**
	 * @param conf
	 * 			This method accepts conf 
	 */
	public void setConf(Configuration conf) {
		this.conf = conf;
		if(conf.getFirst_row().equals(DatabrokerConstants.FIRST_ROW_CONTAINS_FIELDNAMES)) {
			start = 1;
		} else {
			start = 0;
		}
		shellFileCreated = false;
		resultsetSize = 1;
	}

	/**
	 * @return the resultsetSize
	 * 				This method returns resultsetSize
	 * 			
	 */
	public int getResultsetSize() {
		return resultsetSize;
	}

	/**
	 * @param resultsetSize the resultsetSize to set
	 * 					This method returns resultsetSize
	 */
	public void setResultsetSize(int resultsetSize) {
		this.resultsetSize = resultsetSize;
	}

	/**
	 * @return the start
	 * 			This method returns start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 * 				This method sets start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the shellFileCreated
	 * 				This method return shellFileCreated
	 */
	public boolean isShellFileCreated() {
		return shellFileCreated;
	}

	/**
	 * @param shellFileCreated the shellFileCreated to set
	 * 			This method sets shellFileCreated
	 */
	public void setShellFileCreated(boolean shellFileCreated) {
		this.shellFileCreated = shellFileCreated;
	}
	
	
}
