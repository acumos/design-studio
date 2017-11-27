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

import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaGeneratorServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TgifGeneratorServiceTest.class);
	
	private String metaDataFile = "./metadata.json";
	private String protoDataFile = "./aggregator-proto.proto";
	private String userId = "c4e4d366-8ed8-40e1-b61e-1e103de9699b"; 
	private String solutionID = "3842ba62-8a0c-40cf-817a-ae9986e78aaf";
	private String version = "1.0.0";
	private String solutionRevisionID = "be647000-6ccf-4ec7-bf25-af538bc481ef";
	
	private String toscaOutputFolder = System.getProperty("user.dir");
	
	private String toscaGeneratorEndPointURL = "http://cognita-dev1-vm01-core:8080/model_create";
	private String nexusEndPointURL = "http://cognita_model_rw:not4you@cognita-nexus01.eastus.cloudapp.azure.com:8081/repository/repo_cognita_model_maven/";
	private String nexusUserName = "cognita_model_rw";
	private String nexusPassword = "not4you";
	private String nexusGroupId = "com.artifact";
	private String cmnDataSvcEndPoinURL = "http://cognita-dev1-vm01-core.eastus.cloudapp.azure.com:8002/ccds";
	private String cmnDataSvcUser = "ccds_client";
	private String cmnDataSvcPwd = "ccds_client";
	private String result = null;
	
	@Test
	public void testCountTransport() throws Exception{
		ToscaGeneratorClient client = new ToscaGeneratorClient(toscaOutputFolder,  toscaGeneratorEndPointURL,  nexusEndPointURL,
				 nexusUserName,  nexusPassword,  nexusGroupId,  cmnDataSvcEndPoinURL,
				 cmnDataSvcUser,  cmnDataSvcPwd);
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
