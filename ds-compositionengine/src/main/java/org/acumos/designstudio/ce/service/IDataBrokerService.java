/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Nodes;

public interface IDataBrokerService {
	/**
	 * This method create and deploy the Databroker as per the legacy implementation
	 * @param cdump
	 * 		Accepts the parameter Cump Vo 
	 * @param nodeId
	 * 		Accepts the parameter Node Id
	 * @param userId
	 * 		Accepts the parameter User Id
	 * @return
	 * 		Returns the Docker Image URI of the newly uploaded docker impage.
	 * @throws ServiceException
	 * 		This method throws ServiceException
	 */
	@Deprecated
	public String createDeployDataBroker(Cdump cdump, String nodeId, String userId) throws ServiceException;
	
	/**
	 * This Method returns the databroker image URI as per the databroker type. 
	 * @param node
	 * 		Accepts the Cdump Node 
	 * @return
	 * 		Returns the Docker Image URI w.r.t to databroker type. 
	 * 
	 * @throws ServiceException
	 * 		This method throws ServiceException
	 */
	public String getDataBrokerImageURI(Nodes node) throws ServiceException;
}
