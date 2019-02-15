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
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.controller.ArtfactDetailsController;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.AcumosCatalogServiceImpl;
import org.acumos.designstudio.ce.service.ICompositeSolutionService;
import org.acumos.nexus.client.RepositoryLocation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


public class ArtfactDetailsControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static Properties CONFIG = new Properties();
	RepositoryLocation repositoryLocation = null;
	String solutionId = "010646b2-0298-4d25-9571-b775c3737bb6";
	String version = "1";
	String userId = "e57490ed-8462-4a77-ab39-157138dfbda8";
	String artifactType = "TG";
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@InjectMocks
	ArtfactDetailsController artfactDetailsController;
	@Mock
	AcumosCatalogServiceImpl acumosCatalogServiceImpl;
	@Mock
	ICompositeSolutionService compositeServiceImpl;
	@Mock
	org.acumos.designstudio.ce.util.Properties props;
	
	@Mock
	HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	/**
	 * The test case is used to fetch the TGIF.json for a model by using a
	 * method readArtifact which consumes userId, solutionId, version,
	 * artifactType as parameters and fetches TGIF.json in a string
	 * format.TGIF.json consist of name, version, description, component_type
	 * under self, streams,parameters,auxiliary,artifacts. It also defines the
	 * services, called and provided by a model at the input and output port
	 * respectively. The file is used by ds-composition engine to represent a
	 * model.
	 * 
	 */
	public void fetchJsonTOSCA() {
		String resultStatment = "{\"self\":{\"version\":\"1\",\"name\":\"CPM1\",\"description\":\"\",\"component_type\":\"Docker\"},\"streams\":{},\"services\":{\"calls\":[{\"config_key\":\"transform\",\"request\":{\"format\":[{\"messageName\":\"DataFrame\",\"messageargumentList\":[{\"role\":\"repeated\",\"complexType\":{\"messageName\":\"DataFrameRow\",\"messageargumentList\":[{\"role\":\"\",\"name\":\"sepal_len\",\"tag\":\"1.1\",\"type\":\"string\"},{\"role\":\"\",\"name\":\"sepal_wid\",\"tag\":\"1.2\",\"type\":\"int32\"},{\"role\":\"\",\"complexType\":{\"messageName\":\"SubFrameRow\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"row_1\",\"tag\":\"1.3.1\",\"type\":\"string\"},{\"role\":\"repeated\",\"name\":\"row_2\",\"tag\":\"1.3.2\",\"type\":\"string\"}]},\"name\":\"petal_len\",\"tag\":\"1.3\",\"type\":\"SubFrameRow\"},{\"role\":\"\",\"name\":\"petal_wid\",\"tag\":\"1.4\",\"type\":\"string\"}]},\"name\":\"rows\",\"tag\":\"1\",\"type\":\"DataFrameRow\"},{\"role\":\"repeated\",\"complexType\":{\"messageName\":\"MyFrameRow\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"row_1\",\"tag\":\"2.1\",\"type\":\"int64\"},{\"role\":\"repeated\",\"name\":\"row_2\",\"tag\":\"2.2\",\"type\":\"string\"}]},\"name\":\"myRow\",\"tag\":\"2\",\"type\":\"MyFrameRow\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}],\"provides\":[{\"route\":\"transform\",\"request\":{\"format\":[{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"role\":\"repeated\",\"name\":\"myRow\",\"tag\":\"1\",\"type\":\"int64\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}]},\"parameters\":[],\"auxiliary\":{},\"artifacts\":[]}";
		try {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(acumosCatalogServiceImpl.readArtifact(userId, solutionId, version, artifactType))
					.thenReturn(resultStatment);
			String result = acumosCatalogServiceImpl.readArtifact(userId, solutionId, version, artifactType);
			assertNotNull(result);
		} catch (AcumosException e) {
			logger.error("Exception Occured in fetchJsonTOSCA() testcase", e);
		}
	}

	
	@Test
	/**
	 * The test case is used to fetch the TGIF.json for a model by using a
	 * method readArtifact which consumes userId, solutionId, version,
	 * artifactType as parameters and fetches TGIF.json in a string
	 * format.TGIF.json consist of name, version, description, component_type
	 * under self, streams,parameters,auxiliary,artifacts. It also defines the
	 * services, called and provided by a model at the input and output port
	 * respectively. The file is used by ds-composition engine to represent a
	 * model.
	 * 
	 */
	public void fetchJsonTOSCA2() {
		try {
			HttpServletResponse response = new MockHttpServletResponse();
			when(props.getArtifactType()).thenReturn("TG");	
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(acumosCatalogServiceImpl.readArtifact(userId, solutionId, version, "TG")).thenReturn("123");
			Object result = artfactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
			assertNotNull((String)result);
		} catch (AcumosException e) {
			logger.error("Exception Occured in fetchJsonTOSCA2() testcase", e);
		}
	}
	
	@Test
	/**
	 * The test case is used to fetch the TGIF.json for a model by using a
	 * method readArtifact which consumes userId, solutionId, version,
	 * artifactType as parameters and fetches TGIF.json in a string
	 * format.TGIF.json consist of name, version, description, component_type
	 * under self, streams,parameters,auxiliary,artifacts. It also defines the
	 * services, called and provided by a model at the input and output port
	 * respectively. The file is used by ds-composition engine to represent a
	 * model.
	 * 
	 */
	public void fetchJsonTOSCA3() {
		
		try {
			HttpServletResponse response = new MockHttpServletResponse();
			when(props.getArtifactType()).thenReturn("TG");	
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(acumosCatalogServiceImpl.readArtifact(userId, solutionId, version, "TG")).thenReturn(null);
			Object result = artfactDetailsController.fetchJsonTOSCA(userId, solutionId, version, response);
			assertEquals("Failed to fetch the TOSCA details for specified solutionId and version", (String) result);
			assertNotNull((String)result);
		} catch (AcumosException e) {
			logger.error("Exception Occured in fetchJsonTOSCA2() testcase", e);
		}
	}
}
