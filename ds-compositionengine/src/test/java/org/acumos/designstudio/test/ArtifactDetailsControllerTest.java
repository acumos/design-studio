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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletResponse;

import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.controller.ArtfactDetailsController;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.service.IAcumosCatalog;
import org.acumos.designstudio.ce.util.Properties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@RunWith(value = BlockJUnit4ClassRunner.class)
public class ArtifactDetailsControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private ArtfactDetailsController artifactDetailsController;
	
	@Mock
	private Properties props;
	
	@Mock
	private IAcumosCatalog acumosCatalog;
	
	@Mock
	private HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	private IAcumosCatalog acumosCatlogServiceImpl;
	
	private Properties properties;
	
	String userId = "123";
	String solutionId = "123";
	String version = "123";
	String toscaJson = "{\"self\":{\"version\":\"1\",\"name\":\"Classifier-R\",\"description\":\"\",\"component_type\":\"Docker\"},\"streams\":{},\"services\":{\"calls\":[{\"config_key\":\"classify\",\"request\":{\"format\":[{\"messageName\":\"ClassifiedData\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"idx\",\"tag\":\"1\",\"type\":\"fixed32\"},{\"role\":\"repeated\",\"name\":\"class\",\"tag\":\"2\",\"type\":\"string\"},{\"role\":\"repeated\",\"name\":\"classifier_idx\",\"tag\":\"3\",\"type\":\"int32\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}},{\"config_key\":\"doClassification\",\"request\":{\"format\":[{\"messageName\":\"Classification\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"idx\",\"tag\":\"1\",\"type\":\"int32\"},{\"role\":\"repeated\",\"name\":\"class\",\"tag\":\"2\",\"type\":\"string\"},{\"role\":\"repeated\",\"name\":\"classifier_idx\",\"tag\":\"3\",\"type\":\"int32\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}],\"provides\":[{\"route\":\"classify\",\"request\":{\"format\":[{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"idx\",\"tag\":\"1\",\"type\":\"int64\"},{\"role\":\"repeated\",\"name\":\"class\",\"tag\":\"2\",\"type\":\"int64\"},{\"role\":\"repeated\",\"name\":\"prediction\",\"tag\":\"3\",\"type\":\"int64\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}},{\"route\":\"doClassification\",\"request\":{\"format\":[{\"messageName\":\"Prediction_mapped\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"idx\",\"tag\":\"1\",\"type\":\"int64\"},{\"role\":\"repeated\",\"name\":\"class\",\"tag\":\"2\",\"type\":\"string\"},{\"role\":\"repeated\",\"name\":\"prediction\",\"tag\":\"3\",\"type\":\"double\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}]},\"parameters\":[],\"auxiliary\":{},\"artifacts\":[]}";
	
	String protobufJson = "{\"protobuf_json\" : {\"syntax\":\"proto3\",\"packageName\":\"\",\"listOfMessages\":[{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"idx\",\"tag\":\"1\"},{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"class\",\"tag\":\"2\"},{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"prediction\",\"tag\":\"3\"}]},{\"messageName\":\"ClassifiedData\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"fixed32\",\"name\":\"idx\",\"tag\":\"1\"},{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"class\",\"tag\":\"2\"},{\"role\":\"repeated\",\"type\":\"int32\",\"name\":\"classifier_idx\",\"tag\":\"3\"}]},{\"messageName\":\"Prediction_mapped\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"idx\",\"tag\":\"1\"},{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"class\",\"tag\":\"2\"},{\"role\":\"repeated\",\"type\":\"double\",\"name\":\"prediction\",\"tag\":\"3\"}]},{\"messageName\":\"Classification\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"int32\",\"name\":\"idx\",\"tag\":\"1\"},{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"class\",\"tag\":\"2\"},{\"role\":\"repeated\",\"type\":\"int32\",\"name\":\"classifier_idx\",\"tag\":\"3\"}]}],\"service\":{\"name\":\"Classifier-R\",\"listOfOperations\":[{\"operationType\":\"rpc\",\"operationName\":\"classify\",\"listOfInputMessages\":[{\"inputMessageName\":\"Prediction\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"ClassifiedData\"}]},{\"operationType\":\"rpc\",\"operationName\":\"doClassification\",\"listOfInputMessages\":[{\"inputMessageName\":\"Prediction_mapped\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"Classification\"}]}]}},\"success\" : \"true\",\"errorMessage\" : \"\"}"; 
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 acumosCatlogServiceImpl = mock(IAcumosCatalog.class);
		 properties = mock(Properties.class);
		 HttpServletResponse response = mock(HttpServletResponse.class);   
	}
	
	@Test
	public void fetchJsonTOSCAExceptionTest() throws AcumosException{
		InterceptorRegistry registry = new InterceptorRegistry();
		HttpServletResponse response = mock(HttpServletResponse.class);  
		when(properties.getArtifactType()).thenReturn("DI");
		Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getArtifactType())).thenReturn(toscaJson);
		artifactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
	}
	
	@Test
	public void fetchJsonTOSCATest() throws AcumosException{
		InterceptorRegistry registry = new InterceptorRegistry();
		HttpServletResponse response = mock(HttpServletResponse.class);  
		Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
		when(props.getArtifactType()).thenReturn("DI");
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getArtifactType())).thenReturn(toscaJson);
		toscaJson = artifactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
		assertNotNull(toscaJson);
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getArtifactType())).thenReturn(null);
		toscaJson = artifactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
		assertEquals(toscaJson,"Failed to fetch the TOSCA details for specified solutionId and version");
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getArtifactType())).thenReturn("");
		toscaJson = artifactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
		assertEquals(toscaJson,"Failed to fetch the TOSCA details for specified solutionId and version");
	}
	
	@Test
	public void fetchProtobufJSONTest() throws AcumosException{
		InterceptorRegistry registry = new InterceptorRegistry();
		Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
		when(props.getProtoArtifactType()).thenReturn("PJ");
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getProtoArtifactType())).thenReturn(protobufJson);
		protobufJson = artifactDetailsController.fetchProtoBufJSON(userId, solutionId, version);
		assertNotNull(protobufJson);
		
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getProtoArtifactType())).thenReturn(null);
		protobufJson = artifactDetailsController.fetchProtoBufJSON(userId, solutionId, version);
		//assertEquals("{\"protobuf_json\" : null, \"success\" : \"false\",\"errorMessage\" : \"Unable to read protoBufFile\"}", protobufJson);
		assertNotNull(protobufJson);
		
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getProtoArtifactType())).thenReturn("");
		protobufJson = artifactDetailsController.fetchProtoBufJSON(userId, solutionId, version);
		//assertEquals("{\"protobuf_json\" : null, \"success\" : \"false\",\"errorMessage\" : \"Unable to read protoBufFile\"}", protobufJson);
		assertNotNull(protobufJson);
	}
	
	//@Test
	public void fetchProtobufJsonExceptionTest() throws AcumosException{
		InterceptorRegistry registry = new InterceptorRegistry();
		Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
		when(properties.getProtoArtifactType()).thenReturn("PJ");
		when(acumosCatalog.readArtifact(userId, solutionId, version, props.getProtoArtifactType())).thenReturn(protobufJson);
		protobufJson = artifactDetailsController.fetchProtoBufJSON(userId, solutionId, version);
	}
	
}
