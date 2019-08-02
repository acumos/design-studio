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
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.ServiceException;
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaGeneratorClientTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	//@Test(expected = ServiceException.class)
	public void ToscaClientTest(){
		ToscaGeneratorClient client = new ToscaGeneratorClient(" {\"toscaOutputFolder\":\"./src/test/resources\",\"toscaGeneratorEndPointURL\":\"cognita-nexus01:8002/toscapythonserver:2.0.0\",\"nexusEndPointURL\":\"http://acumos-devuser:d3vu3er@acumos-nexus01.eastus.cloudapp.azure.com:8081/repository/acumos-dev-maven\",\"nexusUserName\":\"acumos-devuser\",\"nexusPassword\":\"d3vu3er\",\"nexusGroupId\":\"com.artifact\",\"cmnDataSvcEndPoinURL\":\"http://cognita-dev1-vm01-core:8000/ccds\",\"cmnDataSvcUser\":\"ccds_client\",\"cmnDataSvcPwd\":\"ccds_client\"}");
		client = new ToscaGeneratorClient("str");
		client = new ToscaGeneratorClient("./src/test/resources", "cognita-nexus01:8002/toscapythonserver:2.0.0", "http://acumos-devuser:d3vu3er@acumos-nexus01.eastus.cloudapp.azure.com:8081/repository/acumos-dev-maven", "acumos-devuser", "d3vu3er", "com.artifact", "http://cognita-dev1-vm01-core:8000/ccds", "ccds_client", "ccds_client");
		
		File localDir = new File("./src/test/resources");
		String localProtobufFile = "testmodel.proto";
		File protoFile = new File(localDir,localProtobufFile);
		if (!protoFile.exists()) {
			try {
				protoFile.createNewFile();
			} catch (IOException e) {
				logger.error("Exception occured while creating the new file");
			}
		}
		
		String localMetadataFile = "PROTOBUF.json";
		File tagFile = new File(localDir,localMetadataFile);
		if (!tagFile.exists()) {
			try {
				tagFile.createNewFile();
			} catch (IOException e) {
				logger.error("Exception occured while creating the new file");
			}
		}
		Artifact artifact = new Artifact();
		artifact.setSolutionID("123");
		artifact.setVersion("1");
		artifact.setContentLength(2000);
		artifact.setExtension(".TOSCA");
		artifact.setName("TOSCA");
		artifact.setPayloadURI("URI");
		artifact.setNexusURI("nexus URI");
		artifact.setType("Type");
		List<Artifact> artList = new ArrayList<Artifact>();
		artList.add(artifact);
		
		MLPSolutionRevision mlpRev = new MLPSolutionRevision();
		mlpRev.setCreated(Instant.now());
		mlpRev.setMetadata("MetaData");
		mlpRev.setModified(Instant.now());
		mlpRev.setOnboarded(Instant.now());
		mlpRev.setPublisher("techmdev");
		mlpRev.setRevisionId("123");
		mlpRev.setSolutionId("123");
		mlpRev.setUserId("123");
		mlpRev.setVerifiedLicense("Yes");
		mlpRev.setVerifiedVulnerability("Yes");
		mlpRev.setVersion("1");
		List<MLPSolutionRevision> listMLPSolRev = new ArrayList<>();
		listMLPSolRev.add(mlpRev);
		
		try {
			//when(protoService.createProtoJson("123", "1", tagFile)).thenReturn("str");
			//when(toscaGeneratorService.uploadFilesToRepository("123", "1", artList)).thenReturn(artList);
			//when(cdmsClient.getSolutionRevisions("123")).thenReturn(listMLPSolRev);
			client.generateTOSCA("123", "123", "1", "123", protoFile, tagFile);
		} catch (AcumosException e) {
			logger.error("AcumosException occured while generating the TOSCA File");
		}
		
	}


}
