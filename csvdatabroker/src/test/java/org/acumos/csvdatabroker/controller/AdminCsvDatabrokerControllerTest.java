/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.csvdatabroker.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.acumos.csvdatabroker.service.CSVDatabrokerService;
import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.service.ProtobufService;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.Protobuf;
import org.acumos.csvdatabroker.vo.ProtobufMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@RunWith(SpringRunner.class)
@WebMvcTest(value = AdminCsvDatabrokerController.class, secure = false)
public class AdminCsvDatabrokerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	@Qualifier("CSVDatabrokerServiceImpl")
	private CSVDatabrokerService service;
	
	@MockBean
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@MockBean
	@Qualifier("ProtobufServiceImpl")
	private ProtobufService protoService;
	
	@Test
	public void setResultsetSize() throws Exception { 
		
		MvcResult mvcresult = mockMvc.perform(MockMvcRequestBuilders.put("/admin/setResultsetSize", 1)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		
		
		mvcresult = mockMvc.perform(MockMvcRequestBuilders.put("/admin/setResultsetSize", 0)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)).andReturn();
		 response = mvcresult.getResponse();
		 assertEquals(200, response.getStatus());
	}
	
	@Test
	public void resetOffset() throws Exception { 
		
		Configuration conf  = null;
		Mockito.when(confService.getConf()).thenReturn(conf);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/admin/resetOffset");
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		
		conf = new Configuration();
		conf.setScript("this is the script");
		conf.setFirst_row("contains_field_names");
		Mockito.when(confService.getConf()).thenReturn(conf);
		requestBuilder = MockMvcRequestBuilders.put("/admin/resetOffset");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		response = mvcresult.getResponse();
		assertEquals(200, response.getStatus());
		
	}
	
	
	@Test
	public void getOffset() throws Exception { 
		
		Mockito.when(confService.getStart()).thenReturn(1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/getOffset");
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		response = mvcresult.getResponse();
		
		Mockito.when(confService.getStart()).thenThrow(new NullPointerException());
		requestBuilder = MockMvcRequestBuilders.get("/admin/getOffset");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		response = mvcresult.getResponse();
		assertNotNull(response);		
	}
	
	@Test
	public void getEnvironmentConfiguration() throws Exception { 
		
		Configuration conf = new Configuration();
		conf.setPassword("test");
		Mockito.when(confService.getConf()).thenReturn(conf);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/getconfigDB");
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		
		Mockito.when(confService.getConf()).thenThrow(new NullPointerException());
		requestBuilder = MockMvcRequestBuilders.get("/admin/getconfigDB");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		response = mvcresult.getResponse();
		assertNotNull(response);		
	} 
	
	@Test
	public void getProtobuf() throws Exception { 
		
		Protobuf protobuf  = new Protobuf();
		List<ProtobufMessage> messages = new ArrayList<ProtobufMessage>(); 
		ProtobufMessage protobufMessage = new ProtobufMessage();
		protobufMessage.setName("test");
		messages.add(protobufMessage);
		protobuf.setMessages(messages);
		protobuf.setSyntax("test");
	
		protobuf.setService(new org.acumos.csvdatabroker.vo.ProtobufService());
		Mockito.when(protoService.getProtobuf()).thenReturn(protobuf);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/getProtobuf");
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		
		Mockito.when(protoService.getProtobuf()).thenThrow(new NullPointerException());
		requestBuilder = MockMvcRequestBuilders.get("/admin/getProtobuf");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		response = mvcresult.getResponse();
		assertNotNull(response);		
	}
}
