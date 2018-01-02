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
import org.acumos.designstudio.toscagenerator.vo.tgif.Call;
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

/**
 * 
 * @author NB00350480
 *
 */
public class TGIFTest {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(TGIFTest.class);

	@Test
	public void testTGIF() throws Exception {

		Tgif tgif = new Tgif();

		Self selfObj = new Self();
		
		selfObj.setVersion("1.1.1");
		selfObj.setName("Gen");
		selfObj.setDescription("Tgif file test");
		selfObj.setComponent_type("Docker");
		
		tgif.setSelf(selfObj);

		Stream stream = new Stream();
		String[] subscribes = new String[1];
		String[] publishes = new String[1];
		
		stream.setSubscribes(subscribes);
		stream.setPublishes(publishes);
		
		tgif.setStreams(stream);
		
		Service service = new Service();
		Call[] callList = getCallDetails();
		Provide[] provideList = getProvide();
		service.setCalls(callList);
		service.setProvides(provideList);
		
		tgif.setServices(service);

		assertNotNull(tgif);		
		Gson gson = new Gson();
		String tgifoJsonString = gson.toJson(tgif);
		logger.debug(EELFLoggerDelegator.debugLogger, "result " +  tgifoJsonString);

	}
	private Call[] getCallDetails() throws ParseException{
		Call call = new Call();
		call.setConfig_key("config_key");
		
		Request request = new Request();
		String jsonString = "[{\"messageName\": \"Prediction\",\"messageargumentList\": [{\"name\": \"myRow\",\"rule\": \"repeated\",\"tag\": \"1\",\"type\": \"int64\"}]}]";
		
		Object object=null;
		JSONArray arrayObj=null;
		JSONParser jsonParser=new JSONParser();
		object=jsonParser.parse(jsonString);
		arrayObj=(JSONArray) object;
		
		request.setFormat(arrayObj);
		request.setVersion("");
		
		call.setRequest(request);
		
		Response response = new Response();
		response.setFormat(new org.json.simple.JSONArray());
		response.setVersion("");
		
		call.setResponse(response);
		Call[] callList = {call};
		return callList;
	}
	private Provide[] getProvide() throws ParseException{
		Provide provide = new Provide();
		provide.setRoute("transform");
		
		Request request = new Request();
		String jsonString = "[{\"messageName\": \"Prediction\",\"messageargumentList\": [{\"name\": \"myRow\",\"rule\": \"repeated\",\"tag\": \"1\",\"type\": \"int64\"}]}]";
		
		Object object=null;
		JSONArray arrayObj=null;
		JSONParser jsonParser=new JSONParser();
		object=jsonParser.parse(jsonString);
		arrayObj=(JSONArray) object;
		
		request.setFormat(arrayObj);
		request.setVersion("");
		
		provide.setRequest(request);
		
		Response response = new Response();
		response.setFormat(new org.json.simple.JSONArray());
		response.setVersion("");
		
		provide.setResponse(response);
		Provide[] provideList = {provide};
		return provideList;
	}

}
