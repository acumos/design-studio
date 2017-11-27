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

import java.util.ArrayList;
import java.util.List;

import org.acumos.designstudio.cdump.FieldMap;
import org.acumos.designstudio.cdump.Nodes;
import org.acumos.designstudio.cdump.Property;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service("solutionServiceDummy")
public class SolutionServiceDummy implements SolutionService {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionServiceDummy.class);

	@Override
	public String getSolutions(String userId) {
		logger.debug(EELFLoggerDelegator.debugLogger, "----getSOlutions() started-------");
		DSSolution ds1 = new DSSolution("", "", "Solution1", "v1", "vaibhav", "vaibhav", "vaibhav", "Unknown",
				"private", "Testing", "Category1", "", "", "");
		DSSolution ds2 = new DSSolution("", "", "Solution1", "v1.2", "vaibhav", "vaibhav", "vaibhav", "Unknown",
				"private", "Testing", "Category2", "", "", "");

		List<DSSolution> list = new ArrayList<>();
		list.add(ds1);
		list.add(ds2);

		Gson gson = new Gson();
		String json = gson.toJson(list, new TypeToken<List<DSSolution>>() {
			private static final long serialVersionUID = 5801214825449355590L;
		}.getType());
		logger.debug(EELFLoggerDelegator.debugLogger, "JSON Array : " + json);
		logger.debug(EELFLoggerDelegator.debugLogger, "----getSOlutions() Ends-------");
		return json;

	}

	@Override
	public String createNewCompositeSolution(String userId) {
		return null;
	}

	@Override
	public boolean addLink(String userId, String solutionId, String version, String linkName, String linkId,
			String sourceNodeName, String sourceNodeId, String targetNodeName, String targetNodeId,
			String sourceNodeRequirement, String targetNodeCapabilityName, String cid, Property propertieValues) {
		return false;
	}

	@Override
	public String readCompositeSolutionGraph(String userId, String solutionID, String version) {
		return null;
	}

	@Override
	public String modifyNode(String userId, String solutionId, String version, String cid, String nodeId,
			String nodeName, String ndata, FieldMap fieldmap) {
		return null;
	}

	@Override
	public String modifyLink(String userId, String cid, String solutionId, String version, String linkId,
			String linkName) {
		return null;
	}

	@Override
	public boolean deleteNode(String userId, String solutionId, String version, String cid, String nodeId) {
		return false;
	}

	@Override
	public boolean deleteLink(String userId, String solutionId, String version, String cid, String linkId) {
		return false;
	}

	@Override
	public String addNode(String userId, String solutionId, String version, String cid, Nodes node) {
		return null;
	}

	@Override
	public String getMatchingModels(String userId, String portType, JSONArray protobufJsonString) throws Exception {
		return null;
	}

}
