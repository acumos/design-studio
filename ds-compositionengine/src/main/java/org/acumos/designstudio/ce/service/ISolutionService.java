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

import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.cdump.Nodes;
import org.acumos.designstudio.ce.vo.cdump.Property;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMap;
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.FieldMap;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMap;
import org.json.JSONArray;

public interface ISolutionService {

	/**
	 * This method used for get the Solutions form CDS
	 * @param userId
	 * This method accepts userId as Parameter
	 * @return
	 * This method returns solutions
	 * @throws ServiceException
	 * If in case exception occurs it will throw ServiceException
	 */
	public String getSolutions(String userId) throws ServiceException;

	/**
	 * This method used for to create the new composite solution 
	 * @param userId
	 * This method accepts userId as Parameter
	 * @return
	 * This method returns success or failure response in string format 
	 * @throws AcumosException
	 * If in case exception occurs it will throw AcumosException
	 */
	public String createNewCompositeSolution(String userId) throws AcumosException;

	/**
	 * This method used for to add the Link between two models
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionId
	 * This method accepts solutionId as Parameter
	 * @param version
	 * This method accepts version as Parameter
	 * @param linkName
	 * This method accepts linkName as Parameter
	 * @param linkId
	 * This method accepts linkId as Parameter
	 * @param sourceNodeName
	 * This method accepts sourceNodeName as Parameter
	 * @param sourceNodeId
	 * This method accepts sourceNodeId as Parameter
	 * @param targetNodeName
	 * This method accepts targetNodeName as Parameter
	 * @param targetNodeId
	 * This method accepts targetNodeId as Parameter
	 * @param sourceNodeRequirement
	 * This method accepts sourceNodeRequirement as Parameter
	 * @param targetNodeCapabilityName
	 * This method accepts targetNodeCapabilityName as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param propertieValues
	 * This method accepts propertieValues as Parameter
	 * @return
	 * This method returns success as true or false
	 * 
	 */
	public boolean addLink(String userId, String solutionId, String version, String linkName, String linkId,
			String sourceNodeName, String sourceNodeId, String targetNodeName, String targetNodeId,
			String sourceNodeRequirement, String targetNodeCapabilityName, String cid, Property propertieValues);

	/**
	 * This method used for to read the composite solution graph
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionID
	 * This method accepts solutionID as Parameter
	 * @param version
	 * This method accepts version as Parameter
	 * @return
	 * This method will return success or failure response in string format
	 * @throws AcumosException
	 * If in case exception occurs it will throw AcumosException
	 */
	public String readCompositeSolutionGraph(String userId, String solutionID, String version) throws AcumosException;

	/**
	 * This method used for to modify the Node
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionId
	 * This method accepts userId as Parameter
	 * @param version
	 * This method accepts solutionId as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param nodeId
	 * This method accepts nodeId as Parameter
	 * @param nodeName
	 * This method accepts nodeName as Parameter
	 * @param ndata
	 * This method accepts ndata as Parameter
	 * @param field_map
	 * This method accepts field_map as Parameter
	 * @param databrokerMap
	 * This method accepts databrokerMap as Parameter
	 * @param collatorMap
	 * This method accepts collatorMap as Parameter
	 * @param splitterMap
	 * This method accepts splitterMap as Parameter
	 * @return
	 * This method will return success/failure response in string format
	 */
	public String modifyNode(String userId, String solutionId, String version, String cid, String nodeId,
			String nodeName, String ndata, FieldMap field_map, DataBrokerMap databrokerMap, CollatorMap collatorMap, SplitterMap splitterMap);

	/**
	 * This method used for to modify the Link
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param solutionId
	 * This method accepts solutionId as Parameter
	 * @param version
	 * This method accepts solutionId as Parameter
	 * @param linkId
	 * This method accepts linkId as Parameter
	 * @param linkName
	 * This method accepts linkName as Parameter
	 * @return
	 * This method will return success/failure response in string format
	 */
	public String modifyLink(String userId, String cid, String solutionId, String version, String linkId,
			String linkName);

	/**
	 * This method used for to deleteNode
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionId
	 * This method accepts solutionId as Parameter
	 * @param version
	 * This method accepts version as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param nodeId
	 * This method accepts nodeId as Parameter
	 * @return
	 * This method will return success response as true/false
	 * @throws AcumosException
	 * If in case exception it will throw AcumosException
	 */
	public boolean deleteNode(String userId, String solutionId, String version, String cid, String nodeId) throws AcumosException;

	/**
	 * This method used for to deleteLink
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionId
	 * This method accepts solutionId as Parameter
	 * @param version
	 * This method accepts version as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param linkId
	 * This method accepts linkId as Parameter
	 * @return
	 * This method will return success response as true/false
	 */
	public boolean deleteLink(String userId, String solutionId, String version, String cid, String linkId);

	/**
	 * This method used for to addNode
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param solutionId
	 * This method accepts solutionId as Parameter
	 * @param version
	 * This method accepts version as Parameter
	 * @param cid
	 * This method accepts cid as Parameter
	 * @param nodes
	 * This method accepts nodes as Parameter
	 * @return
	 * This method will return success/failure response in string format
	 */
	public String addNode(String userId, String solutionId, String version, String cid, Nodes nodes);

	/**
	 * This method used for to getMatchingModels
	 * @param userId
	 * This method accepts userId as Parameter
	 * @param portType
	 * This method accepts portType as Parameter
	 * @param protobufJsonString
	 * This method accepts protobufJsonString as Parameter
	 * @return
	 * This method will return success/failure response in string format
	 * @throws Exception
	 * If in case exception it will throw AcumosException
	 */
	public String getMatchingModels(String userId, String portType, JSONArray protobufJsonString) throws Exception;
	
	/**
	 * This method will get the updated models like PR,OR,PB 
	 * @throws InterruptedException
	 * 			In case of Thread Execution if exception comes it throws InterruptedException 
	 * @throws ServiceException
	 * 			In case of exception it throws ServiceException 
	 */
	public void getUpdatedModelsbyDate() throws InterruptedException, ServiceException;

}
