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

import org.acumos.sqldatabroker.vo.Configuration;

/**
 * This is class holds the details configured using API /configDB. 
 * 
 */

public interface ConfigurationService {
	
	/**
	 * To increment the next sart by resultsetSize.
	 */
	public void incrementStart();
	
	/**
	 * This method return clone of the set Configuration
	 * @return conf
	 * 			return the Configuration instance previously set
	 * @throws CloneNotSupportedException
	 * 			If not able to clone the object then throws the CloneNotSupportedException.
	 */
	public Configuration getConf() throws CloneNotSupportedException;
	
	/**
	 * This method set the Configuration
	 * @param conf 
	 * 		accepts the parameter Configuration instance.
	 */
	public void setConf(Configuration conf);

	/**
	 * This method return the configured resultset size. 
	 * @return the resultsetSize
	 * 
	 */
	public int getResultsetSize() ;

	/**
	 * This method set the resulset size.
	 * @param resultsetSize the resultsetSize to set
	 */
	public void setResultsetSize(int resultsetSize) ;

	/**
	 * This method return the start of resultset
	 * @return the start
	 */
	public int getStart();

	/**
	 * This method set start of the result
	 * @param start the start to set
	 */
	public void setStart(int start) ;
	
	/**
	 * This method convery whether shell file is created or not.
	 * @return the shellFileCreated
	 */
	public boolean isShellFileCreated() ;

	/**
	 * This method sets whether shell file is created. 
	 * @param shellFileCreated the shellFileCreated to set
	 */
	public void setShellFileCreated(boolean shellFileCreated);
	
	
}
