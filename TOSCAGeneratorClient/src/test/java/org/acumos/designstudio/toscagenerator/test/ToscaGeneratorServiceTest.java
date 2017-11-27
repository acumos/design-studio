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
import org.acumos.designstudio.toscagenerator.service.TgifGeneratorService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaGeneratorServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TgifGeneratorServiceTest.class);
	
	private String metaDataFile = "D:/ab00343130/Acumos/Acumos/Studio/Sprint4/metadata.json";
	private String protoDataFile = "D:/ab00343130/Acumos/Acumos/Studio/Sprint4/aggregator-proto.proto";
	private String userId = "12345"; 
	private String solutionID = "34ERT3566dashiswf";
	private String version = "1.0.2";
	private String solutionRevisionID = "234567iekslke1";
	private String toscaOutputFolder = "D:/ab00343130/Acumos/Acumos/Studio/Sprint4/";
	private String toscaGeneratorEndPointURL = "http://acumos-dev1-vm01-core:8080/model_create";
	private String nexusEndPointURL = "http://acumos_model_rw:not4you@nexus.acumos.com:8081/repository/repo_acumos_model_maven/";
	private String nexusUserName = "acumos_model_rw";
	private String nexusPassword = "not4you";
	private String nexusGroupId = "com.artifact";
	private String cmnDataSvcEndPoinURL = "http://localhost:8002/ccds";
	private String cmnDataSvcUser = "ccds_client";
	private String cmnDataSvcPwd = "ccds_client";
	private String result = null;
	
	@Test
	public void testCountTransport() {
		ToscaGeneratorClient client = new ToscaGeneratorClient(toscaOutputFolder,  toscaGeneratorEndPointURL,  nexusEndPointURL,
				 nexusUserName,  nexusPassword,  nexusGroupId,  cmnDataSvcEndPoinURL,
				 cmnDataSvcUser,  cmnDataSvcPwd);
		try{
			logger.info("Toscagenerator client start");
			File localMetaDataFile = new File(metaDataFile);
			File localProtoFile = new File(protoDataFile);
			result = client.generateTOSCA(userId, solutionID, version,solutionRevisionID,localProtoFile, localMetaDataFile);
			System.out.println("result : " + result);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" ------------ Exception Occured  generateTOSCA() ----------- " + ex);

		}
		System.out.println(" ------ DONE -------- " + result);
		logger.info("Toscagenerator client end");
	
	}

}
