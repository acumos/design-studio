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

import static org.junit.Assert.*;

import java.util.*;

import org.acumos.designstudio.toscagenerator.util.EELFLoggerDelegator;
import org.acumos.designstudio.toscagenerator.vo.tgif.Artifact;
import org.acumos.designstudio.toscagenerator.vo.tgif.Auxiliary;
import org.acumos.designstudio.toscagenerator.vo.tgif.Call;
import org.acumos.designstudio.toscagenerator.vo.tgif.Parameter;
import org.acumos.designstudio.toscagenerator.vo.tgif.Provide;
import org.acumos.designstudio.toscagenerator.vo.tgif.Request;
import org.acumos.designstudio.toscagenerator.vo.tgif.Response;
import org.acumos.designstudio.toscagenerator.vo.tgif.Self;
import org.acumos.designstudio.toscagenerator.vo.tgif.Service;
import org.acumos.designstudio.toscagenerator.vo.tgif.Stream;
import org.acumos.designstudio.toscagenerator.vo.tgif.Tgif;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.google.gson.Gson;

public class TGIFTest {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(TGIFTest.class);

	/**
	 * The test case is used to test the representation of TGIF.json in the
	 * desired format.TGIF.json consist of name, version, description,
	 * component_type under self, streams,parameters,auxiliary,artifacts. It
	 * defines the services, called and provided by a model. The json file is
	 * used by ds-composition engine to represent a model.
	 * 
	 */

	@Test
	public void testTGIF() throws Exception {

		Tgif tgif = new Tgif();

		Self selfObj = new Self();

		selfObj.setVersion("1.1.1");
		selfObj.getVersion();
		
		selfObj.setName("Gen");
		selfObj.getName();
		selfObj.setDescription("Tgif file test");
		selfObj.getDescription();
		selfObj.setComponent_type("Docker");
		selfObj.getComponent_type();
		
		selfObj = new Self("1", "Name", "Description", "ComponentType");

		tgif.setSelf(selfObj);
		tgif.getSelf();

		Stream stream = new Stream();
		String[] subscribes = new String[1];
		String[] publishes = new String[1];

		stream.setSubscribes(subscribes);
		stream.getSubscribes();
		stream.setPublishes(publishes);
		stream.getPublishes();
		
		stream = new Stream(subscribes, publishes);

		tgif.setStreams(stream);
		tgif.getStreams();

		Service service = new Service();
		Call[] callList = getCallDetails();
		Provide[] provideList = getProvide();
		service.setCalls(callList);
		service.getCalls();
		service.setProvides(provideList);
		service.getProvides();
		service = new Service(callList, provideList);

		tgif.setServices(service);
		tgif.getServices();
		
		Parameter param1 = new Parameter();
		param1.setDescription("Description");
		param1.getDescription();
		param1.setName("Name");
		param1.getName();
		param1.setValue("Value");
		param1.getValue();
		Parameter param2 = new Parameter("Name", "Value", "Description");
		
		Auxiliary aux = new Auxiliary();
		aux.setTemp("Temp");
		aux.getTemp();
		
		
		tgif.setParameters(new Parameter[0]);
		tgif.getParameters();
		
		tgif.setArtifacts(new Artifact[0]);
		tgif.getArtifacts();
		
		tgif.setAuxiliary(aux);
		tgif.getAuxiliary();
		
		Artifact arti = new Artifact();
		arti.setType("Type");
		arti.getType();
		arti.setUri("uri");
		arti.getUri();
		arti =  new Artifact("uri", "type");
		
		
		
		Tgif tgif1 = new Tgif(selfObj, stream, service, new Parameter[0], aux, new Artifact[0]);
		

		assertNotNull(tgif);
		Gson gson = new Gson();
		String tgifoJsonString = gson.toJson(tgif);
		logger.debug(EELFLoggerDelegator.debugLogger, "result " + tgifoJsonString);

	}

	/**
	 * The method is used while creating TGIF.json so as to show to what all
	 * models a given model can connect to input port based on the matching
	 * messageargument . These are later on shown as input messages on the input
	 * port of the model.The file is used by ds-composition engine to represent
	 * a model.
	 * 
	 */
	private Call[] getCallDetails() throws ParseException {
		Call call = new Call();
		call.setConfig_key("config_key");
		call.getConfig_key();
		
		Request request = new Request();
		
		String jsonString = "[{\"messageName\": \"Prediction\",\"messageargumentList\": [{\"name\": \"myRow\",\"role\": \"repeated\",\"tag\": \"1\",\"type\": \"int64\"}]}]";

		Object object = null;
		JSONArray arrayObj = null;
		JSONParser jsonParser = new JSONParser();
		object = jsonParser.parse(jsonString);
		arrayObj = (JSONArray) object;

		request.setFormat(arrayObj);
		request.getFormat();
		request.setVersion("1");
		request.getVersion();
		
		Request request1 = new Request(arrayObj, "1");

		call.setRequest(request);
		call.getRequest();

		Response response = new Response();
		response.setFormat(new org.json.simple.JSONArray());
		response.getFormat();
		response.setVersion("");
		response.getVersion();
		
		Response response1 = new Response(arrayObj, "1");

		call.setResponse(response);
		call.getResponse();
		Call[] callList = { call };
		
		Call call1 = new Call("ConfigKey", request, response);
		return callList;
	}

	/**
	 * The method is used while creating TGIF.json so as to show to what all
	 * models a given model can connect to its output port based on the matching
	 * messageargument. These are later on shown as output messages on the
	 * output port of the model.The file is used by ds-composition engine to
	 * represent a model.
	 * 
	 */
	private Provide[] getProvide() throws ParseException {
		Provide provide = new Provide();
		provide.setRoute("transform");
		provide.getRoute();

		Request request = new Request();
		String jsonString = "[{\"messageName\": \"Prediction\",\"messageargumentList\": [{\"name\": \"myRow\",\"role\": \"repeated\",\"tag\": \"1\",\"type\": \"int64\"}]}]";

		Object object = null;
		JSONArray arrayObj = null;
		JSONParser jsonParser = new JSONParser();
		object = jsonParser.parse(jsonString);
		arrayObj = (JSONArray) object;

		request.setFormat(arrayObj);
		request.getFormat();
		request.setVersion("1");
		request.getVersion();
		provide.setRequest(request);
		provide.getRequest();

		Response response = new Response();
		response.setFormat(new org.json.simple.JSONArray());
		response.setVersion("1");

		provide.setResponse(response);
		provide.getResponse();
		
		provide = new Provide("route", request, response);
		Provide[] provideList = { provide };
		return provideList;
	}

}
