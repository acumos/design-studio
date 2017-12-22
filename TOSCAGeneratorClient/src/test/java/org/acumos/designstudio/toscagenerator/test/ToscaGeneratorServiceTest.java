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

package org.acumos.designstudio.toscagenerator.test;

import java.io.File;
import java.util.Properties;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaGeneratorServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorServiceTest.class);
	
	private String metaDataFile = "./metadata.json";
	private String protoDataFile = "./aggregator-proto.proto";
	private String userId = "c4e4d366-8ed8-40e1-b61e-1e103de9699b"; 
	private String solutionID = "3842ba62-8a0c-40cf-817a-ae9986e78aaf";
	private String version = "1.0.0";
	private String solutionRevisionID = "be647000-6ccf-4ec7-bf25-af538bc481ef";
	private String toscaOutputFolder = System.getProperty("user.dir");
	private String result = null;
	private String url = "";
	private String user ="";
	private String pass = "";
	private String toscaGeneratorEndPointURL = "";
	private String nexusEndPointURL = "";
	private String nexusUserName = "";
	private String nexusPassword = "";
	private String nexusGroupId="";
	private NexusArtifactClient nexusArtifactClient = null;
	private ICommonDataServiceRestClient cmnDataService;
	public static Properties CONFIG = new Properties();
	
	@Before
	public void createClient() throws Exception {
	    CONFIG.load(ToscaGeneratorServiceTest.class.getResourceAsStream("/TOSCAApplication.properties"));
		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		
		nexusEndPointURL = CONFIG.getProperty("nexus.nexusendpointurlTest");
		nexusUserName = CONFIG.getProperty("nexus.nexususernameTest");
		nexusPassword = CONFIG.getProperty("nexus.nexuspasswordTest");
		nexusGroupId = CONFIG.getProperty("nexus.nexusgroupidTest");
		
		toscaGeneratorEndPointURL = CONFIG.getProperty("toscaGeneratorEndPointURL");
		
		nexusArtifactClient = getNexusClient();
		
		cmnDataService = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);
	}
	private NexusArtifactClient getNexusClient() {
		try {
			RepositoryLocation repositoryLocation = new RepositoryLocation();
			repositoryLocation.setId("1");
			repositoryLocation.setUrl(CONFIG.getProperty("nexus.nexusendpointurlTest"));
			repositoryLocation.setUsername(CONFIG.getProperty("nexus.nexususernameTest"));
			repositoryLocation.setPassword(CONFIG.getProperty("nexus.nexuspasswordTest"));
			nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		} catch (Exception e) {
			logger.error("Exception in getNexusClient()",e);
			throw e;
		}
		return nexusArtifactClient;
	}
	
	@Test
	public void testCountTransport() throws Exception{
		ToscaGeneratorClient client = new ToscaGeneratorClient(toscaOutputFolder,  toscaGeneratorEndPointURL,  nexusEndPointURL,
				 nexusUserName,  nexusPassword,  nexusGroupId,  url,
				 user,  pass);
		try{
			logger.info("Toscagenerator client start");
			File localMetaDataFile = new File(metaDataFile);
			File localProtoFile = new File(protoDataFile);
			result = client.generateTOSCA(userId, solutionID, version,solutionRevisionID,localProtoFile, localMetaDataFile);
			logger.info("result : " + result);
		} catch (Exception ex) {
			logger.error("------------ Exception Occured  generateTOSCA() ----------- ", ex);
			throw ex;
		}
		
		logger.info("Toscagenerator client end");
	}

}
