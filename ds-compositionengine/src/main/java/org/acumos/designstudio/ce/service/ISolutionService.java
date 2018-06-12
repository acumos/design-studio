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

	public String getSolutions(String userId) throws ServiceException;

	public String createNewCompositeSolution(String userId) throws AcumosException;

	public boolean addLink(String userId, String solutionId, String version, String linkName, String linkId,
			String sourceNodeName, String sourceNodeId, String targetNodeName, String targetNodeId,
			String sourceNodeRequirement, String targetNodeCapabilityName, String cid, Property propertieValues);

	public String readCompositeSolutionGraph(String userId, String solutionID, String version) throws AcumosException;

	public String modifyNode(String userId, String solutionId, String version, String cid, String nodeId,
			String nodeName, String ndata, FieldMap field_map, DataBrokerMap databrokerMap, CollatorMap collatorMap, SplitterMap splitterMap);

	public String modifyLink(String userId, String cid, String solutionId, String version, String linkId,
			String linkName);

	public boolean deleteNode(String userId, String solutionId, String version, String cid, String nodeId) throws AcumosException;

	public boolean deleteLink(String userId, String solutionId, String version, String cid, String linkId);

	public String addNode(String userId, String solutionId, String version, String cid, Nodes nodes);

	public String getMatchingModels(String userId, String portType, JSONArray protobufJsonString) throws Exception;

}
