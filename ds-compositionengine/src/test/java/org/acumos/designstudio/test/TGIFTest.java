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
import static org.junit.Assert.assertTrue;

import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.vo.tgif.Artifact;
import org.acumos.designstudio.ce.vo.tgif.Auxiliary;
import org.acumos.designstudio.ce.vo.tgif.Call;
import org.acumos.designstudio.ce.vo.tgif.Parameter;
import org.acumos.designstudio.ce.vo.tgif.Provide;
import org.acumos.designstudio.ce.vo.tgif.Request;
import org.acumos.designstudio.ce.vo.tgif.Response;
import org.acumos.designstudio.ce.vo.tgif.Self;
import org.acumos.designstudio.ce.vo.tgif.Service;
import org.acumos.designstudio.ce.vo.tgif.Stream;
import org.acumos.designstudio.ce.vo.tgif.Tgif;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 *
 */
public class TGIFTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	/**
	 * The test case is used to test the representation of TGIF.json in the
	 * desired format.TGIF.json consist of name, version, description,
	 * component_type under self, streams,parameters,auxiliary,artifacts. It
	 * defines the services, called and provided by a model. The json file is
	 * used by ds-composition engine to represent a model.
	 * 
	 */
	public void testTGIF() throws Exception {

		Tgif tgif = new Tgif();

		Self selfObj = new Self();
		selfObj.setComponent_type("component_type");
		selfObj.setDescription("description");
		selfObj.setName("name");
		selfObj.setVersion("version");
		Self selfObj1 = new Self("version", "name", "description", "component_type");
		logger.debug("result  {} ",  selfObj1);
		tgif.setSelf(selfObj);
		selfObj.getComponent_type();
		selfObj.getDescription();
		selfObj.getName();
		selfObj.getVersion();

		Stream stream = new Stream();
		String[] subscribes = new String[1];
		String[] publishes = new String[1];
		stream.setSubscribes(subscribes);
		stream.setPublishes(publishes);
		Stream stream1 = new Stream(subscribes, publishes);
		logger.debug("result  {} ",  stream1);
		tgif.setStreams(stream);
		stream.getPublishes();
		stream.getSubscribes();

		Request request1 = new Request(new org.json.simple.JSONArray(), "version");
		logger.debug("result  {} ",  request1);
		Request request = new Request();
		request.setFormat(new org.json.simple.JSONArray());
		request.setVersion("version");
		request.getFormat();
		request.getFormat();

		Response response1 = new Response(new org.json.simple.JSONArray(), "version");
		logger.debug("result  {} ",  response1);
		Response response = new Response();
		response.setFormat(new org.json.simple.JSONArray());
		response.setVersion("version");
		response.getFormat();
		response.getVersion();

		Call call = new Call();
		call.setConfig_key("config_key");
		call.setRequest(request);
		call.setResponse(response);
		Call call1 = new Call("config_key", request, response);
		logger.debug("result  {} ",  call1);
		call.getConfig_key();
		call.getRequest();
		call.getResponse();

		Provide provider = new Provide();
		provider.setRequest(request);
		provider.setResponse(response);
		provider.setRoute("route");
		Provide provider1 = new Provide("route", request, response);
		logger.debug("result  {} ",  provider1);
		provider.getRequest();
		provider.getResponse();
		provider.getRoute();

		Call[] callArray = new Call[1];
		callArray[0] = call;
		Provide[] provideArray = new Provide[1];
		provideArray[0] = provider;
		Service service = new Service();
		service.setCalls(callArray);
		service.setProvides(provideArray);
		Service service1 = new Service(callArray, provideArray);
		logger.debug("result  {} ",  service1);
		service.getCalls();
		service.getProvides();
		tgif.setServices(service);

		Parameter[] parameterArray = new Parameter[1];
		Parameter parameter = new Parameter();
		parameter.setDescription("description");
		parameter.setName("name");
		parameter.setValue("value");
		Parameter parameter1 = new Parameter("name", "value", "description");
		logger.debug("result  {} ",  parameter1);
		parameterArray[0] = parameter;
		parameter.getDescription();
		parameter.getName();
		parameter.getValue();
		tgif.setParameters(parameterArray);

		Auxiliary auxiliary = new Auxiliary();
		auxiliary.setTemp("temp");
		auxiliary.getTemp();
		tgif.setAuxiliary(auxiliary);

		Artifact[] artifactArray = new Artifact[1];
		Artifact artifact1 = new Artifact("type", "uri");
		logger.debug("result  {} ",  artifact1);
		Artifact artifact = new Artifact();
		artifact.setType("type");
		artifact.setUri("uri");
		artifact.getType();
		artifact.getUri();
		artifactArray[0] = artifact;
		tgif.setArtifacts(artifactArray);

		Tgif tgif1 = new Tgif(selfObj, stream, service, parameterArray, auxiliary, artifactArray);
		logger.debug("result  {} ",  tgif1);
		tgif.getArtifacts();
		tgif.getAuxiliary();
		tgif.getParameters();
		tgif.getSelf();
		tgif.getServices();
		tgif.getStreams();
		
		assertNotNull(selfObj);
		assertNotNull(selfObj1);
		assertEquals("name", selfObj.getName());
		
		assertNotNull(stream);
		assertNotNull(stream1);
		assertTrue(stream.getPublishes().length == 1);
		
		assertNotNull(request1);
		assertNotNull(request);
		assertEquals("version",request.getVersion());
		
		assertNotNull(response);
		assertNotNull(response1);
		assertTrue("version".equals(response.getVersion()));
		
		assertNotNull(call);
		assertNotNull(call1);
		assertEquals("config_key",call.getConfig_key());
		
		assertNotNull(service);
		assertNotNull(service1);
		assertTrue(service.getCalls().length == 1);
		
		
		assertNotNull(parameter);
		assertNotNull(parameter1);
		assertEquals("description",parameter.getDescription());
		
		assertNotNull(auxiliary);
		assertEquals("temp",auxiliary.getTemp());
		
		assertNotNull(artifact1);
		assertNotNull(artifact);
		assertEquals("uri",artifact.getUri());
		
		assertNotNull(tgif);
		assertNotNull(tgif1);

	}

}
