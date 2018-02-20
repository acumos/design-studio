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

import static org.junit.Assert.*;

import java.util.Properties;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class GetMatchingModelTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GetMatchingModelTest.class);
	private String url = "";
	private String user = "";
	private String pass = "";
	ICommonDataServiceRestClient cmnDataService = null;
	NexusArtifactClient nexusArtifactClient = null;

	public static Properties CONFIG = new Properties();

	@Before
	/**
	 * This method is used to set default values for the instance of
	 * ICommonDataServiceRestClient and NexusArtifactClient by passing common
	 * data service and nexus url, username and password respectively
	 * 
	 * @throws Exception
	 */
	public void createClient() throws Exception {
		CONFIG.load(GetMatchingModelTest.class.getResourceAsStream("/application.properties"));
		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		nexusArtifactClient = getNexusClient();
		cmnDataService = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);
	}

	/**
	 * This method is used to set default values for the instance of
	 * NexusArtifactClient by passing RepositoryLocation object which will
	 * accept nexus url, username and password
	 * 
	 * @return
	 */
	private NexusArtifactClient getNexusClient() {
		try {
			RepositoryLocation repositoryLocation = new RepositoryLocation();
			repositoryLocation.setId("1");
			repositoryLocation.setUrl(CONFIG.getProperty("nexus.nexusendpointurlTest"));
			repositoryLocation.setUsername(CONFIG.getProperty("nexus.nexususernameTest"));
			repositoryLocation.setPassword(CONFIG.getProperty("nexus.nexuspasswordTest"));
			nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "", e);
		}
		return nexusArtifactClient;
	}

	/**
	 * The test case is used to search and enlist the models that are compatible
	 * to connect to the selected port(input / output) of a model. The test case
	 * uses getMatchingModels method which consumes userId, portType,
	 * protobufJsonString and returns the list of matching models in string
	 * format. ds-composition engine utilizes these models to be dragged and
	 * connect to the desired matching port of the selected model.
	 * 
	 * @throws JSONException
	 */
	@Test
	public void getMatchingModels() throws JSONException {

		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		String userId = "";
		String portType = "output";
		String protobufJsonString = "[{\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			solutionServiceImpl.getNexusClient(nexusArtifactClient, null, null);
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,
					protobufJsonString1);
			assertNotNull(getMatchingModelsResult);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		} catch (JSONException je) {
			logger.error(EELFLoggerDelegator.errorLogger, "", je);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,"", ex);
		}

	}
}
