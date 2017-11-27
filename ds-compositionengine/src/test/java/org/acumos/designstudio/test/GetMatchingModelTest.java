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

package org.acumos.designstudio.test;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class GetMatchingModelTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionServiceImpl.class);
	private final String url = "http://localhost:8000/ccds";
	private final String user = "ccds_client";
	private final String pass = "ccds_client";
	ICommonDataServiceRestClient cmnDataService = null;
	NexusArtifactClient nexusArtifactClient = null;
	
	/**
	 * 
	 * @return
	 */
	private ICommonDataServiceRestClient getClient() {
		cmnDataService = new CommonDataServiceRestClientImpl(url, user, pass);
		return cmnDataService;
	}
	/**
	 * 
	 * @return
	 */
	private  NexusArtifactClient getNexusClient(){
		 RepositoryLocation repositoryLocation = new RepositoryLocation();
         repositoryLocation.setId("1");
         repositoryLocation.setUrl("http://acumos_model_rw:not4you@nexus.acumos.com:8081/repository/repo_acumos_model_maven");
         repositoryLocation.setUsername("acumos_model_rw");
         repositoryLocation.setPassword("not4you");
         nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
         return nexusArtifactClient;
    }
	//@Test
	public void getMatchingModels() throws JSONException{
		RepositoryLocation repositoryLocation = new RepositoryLocation();
        repositoryLocation.setId("1");
        repositoryLocation.setUrl("http://acumos_model_rw:not4you@nexus.acumos.com:8081/repository/repo_acumos_model_maven");
        repositoryLocation.setUsername("acumos_model_rw");
        repositoryLocation.setPassword("not4you");
		nexusArtifactClient = getNexusClient();
		SolutionServiceImpl solutionServiceImpl= new SolutionServiceImpl();
		String userId = "";
		String portType = "output";
		String protobufJsonString = "[{\"rule\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"rule\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try{
			cmnDataService  = getClient();
			solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType, protobufJsonString1);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		}catch(JSONException je){
			logger.error(EELFLoggerDelegator.errorLogger, je.getMessage());
		}catch(Exception ex){
			logger.error(EELFLoggerDelegator.errorLogger, ex.getMessage());
		}
		
		
	}
}
