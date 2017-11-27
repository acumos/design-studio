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

import org.acumos.designstudio.cdump.FieldMap;
import org.acumos.designstudio.cdump.Nodes;
import org.acumos.designstudio.cdump.Property;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.json.JSONArray;

/**
 * 
 * 
 *
 */
public interface SolutionService {
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public String getSolutions(String userId) throws ServiceException;

	/**
	 * @param userId
	 * @return
	 */
	public String createNewCompositeSolution(String userId);

	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @param linkName
	 * @param linkId
	 * @param sourceNodeName
	 * @param sourceNodeId
	 * @param targetNodeName
	 * @param targetNodeId
	 * @param sourceNodeRequirement
	 * @param targetNodeCapabilityName
	 * @param cid
	 * @param propertieValues
	 * @return
	 */
	public boolean addLink(String userId, String solutionId, String version, String linkName, String linkId,
			String sourceNodeName, String sourceNodeId, String targetNodeName, String targetNodeId,
			String sourceNodeRequirement, String targetNodeCapabilityName, String cid, Property propertieValues);

	/**
	 * 
	 * @param userId
	 * @param solutionID
	 * @param version
	 * @return
	 */
	public String readCompositeSolutionGraph(String userId, String solutionID, String version);

	/**
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @param cid
	 * @param nodeId
	 * @param nodeName
	 * @param ndata
	 * @return
	 */
	public String modifyNode(String userId, String solutionId, String version, String cid, String nodeId,
			String nodeName, String ndata, FieldMap field_map);

	/**
	 * @param userId
	 * @param cid
	 * @param solutionId
	 * @param version
	 * @param linkId
	 * @param linkName
	 * @return
	 */
	public String modifyLink(String userId, String cid, String solutionId, String version, String linkId,
			String linkName);

	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @param cid
	 * @param nodeId
	 * @return
	 */
	public boolean deleteNode(String userId, String solutionId, String version, String cid, String nodeId);
	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @param cid
	 * @param linkId
	 * @return
	 */
	public boolean deleteLink(String userId, String solutionId, String version, String cid, String linkId);

	/**
	 * @param nodeElements
	 * @return
	 */
	public String addNode(String userId, String solutionId, String version, String cid, Nodes nodes);
	/**
	 * 
	 * @param userId
	 * @param portType
	 * @param protobufJsonString
	 * @return
	 * @throws Exception
	 */
	public String getMatchingModels(String userId, String portType, JSONArray protobufJsonString) throws Exception;

}
