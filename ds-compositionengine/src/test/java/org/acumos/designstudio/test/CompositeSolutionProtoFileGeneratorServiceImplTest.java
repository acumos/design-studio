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

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.CompositeSolutionProtoFileGeneratorServiceImpl;
import org.acumos.nexus.client.NexusArtifactClient;
import org.apache.maven.wagon.ConnectionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.junit4.SpringRunner;

import javassist.bytecode.ByteArray;

@RunWith(SpringRunner.class)
public class CompositeSolutionProtoFileGeneratorServiceImplTest {
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private CompositeSolutionProtoFileGeneratorServiceImpl cspfgsImpl;
	
	@Mock
    private CommonDataServiceRestClientImpl cmnDataService;
	
	@Mock
    private NexusArtifactClient nexusClient;
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void payloadTest() throws ConnectionException{
		String solutionId = "111";
		String version = "1.0.0";
		String artifactType = "BP";
		String fileExtension = ".proto";
		//String str = "{\"syntax\":\"proto3\",\"packageName\":\"WSLRyaKHDkLpVPFBkIAXxUDscwqHgmlY\",\"listOfMessages\":[{\"messageName\":\"GreetOut\",\"messageargumentList\":[{\"role\":\"\",\"type\":\"string\",\"name\":\"value\",\"tag\":\"1\"}]},{\"messageName\":\"GreetIn\",\"messageargumentList\":[{\"role\":\"\",\"type\":\"string\",\"name\":\"s\",\"tag\":\"1\"}]}],\"service\":{\"name\":\"Model\",\"listOfOperations\":[{\"operationType\":\"rpc\",\"operationName\":\"greet\",\"listOfInputMessages\":[{\"inputMessageName\":\"GreetIn\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"GreetOut\"}]}]}}";
		String str = "syntax = \"proto3\"; package UbENTnRTKDmvMUwJokNTcYjStUylzrBO; service Model { rpc ingest (ComputeInput) returns (ComputeInput);}message ComputeInput { double f1 = 1; double f2 = 2; string s = 3;}";
		MLPSolutionRevision mlpSolutionRevision = getMLPSolutionRevision(solutionId, version);
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		
		MLPArtifact mlpArtifact = getMLPArtifact(mlpSolutionRevision);
		List<MLPArtifact> mlpArtifactList = new ArrayList<MLPArtifact>();
		mlpArtifactList.add(mlpArtifact);
		
		byte[] bytes = str.getBytes();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(bytes.length);
		stream.write(bytes, 0, bytes.length);
		try {
			when(cmnDataService.getSolutionRevisions("111")).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.getSolutionRevisionArtifacts("111", mlpSolutionRevision.getRevisionId())).thenReturn(mlpArtifactList);
			when(nexusClient.getArtifact(mlpArtifact.getUri())).thenReturn(stream);
			String result = cspfgsImpl.getPayload(solutionId, version, artifactType, fileExtension);
			Assert.assertNotNull(result);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void parseProtobufTest(){
		
	}

	/**
	 * @param mlpSolutionRevision
	 * @return
	 */
	private MLPArtifact getMLPArtifact(MLPSolutionRevision mlpSolutionRevision) {
		MLPArtifact mlpArtifact = new MLPArtifact();
		mlpArtifact.setArtifactTypeCode("BP");
		mlpArtifact.setDescription("Proto File for : Test for SolutionID : "
				+ mlpSolutionRevision.getSolutionId() + " with version : " + mlpSolutionRevision.getVersion());
		mlpArtifact.setUri("org/acumos/111/c0012/1.0.0/c0012-1.0.0.proto");
		mlpArtifact.setName("testPubVer");
		mlpArtifact.setUserId("111"); 	
		mlpArtifact.setVersion("1.0.0");
		mlpArtifact.setSize(1);
		mlpArtifact.setArtifactId("111");
		return mlpArtifact;
	}

	/**
	 * @param solutionId
	 * @param version
	 * @return
	 */
	private MLPSolutionRevision getMLPSolutionRevision(String solutionId, String version) {
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(solutionId);
		mlpSolutionRevision.setUserId("111");
		mlpSolutionRevision.setVersion(version);
		mlpSolutionRevision.setCreated(Instant.now());
		mlpSolutionRevision.setModified(Instant.now());
		mlpSolutionRevision.setMetadata("This is metadata");
		mlpSolutionRevision.setOnboarded(Instant.now());
		mlpSolutionRevision.setOrigin("Origin");
		mlpSolutionRevision.setSourceId("source");
		mlpSolutionRevision.setVerifiedLicense("verified");
		mlpSolutionRevision.setVerifiedVulnerability("verified");
		mlpSolutionRevision.setPublisher("techmdev");
		mlpSolutionRevision.setRevisionId("111");
		return mlpSolutionRevision;
	}
	

}
