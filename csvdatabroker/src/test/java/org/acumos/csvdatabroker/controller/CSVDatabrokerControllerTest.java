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

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.service.CSVDatabrokerService;
import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.service.ProtobufService;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.DataBrokerMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CSVDatabrokerController.class, secure = false)
public class CSVDatabrokerControllerTest {
	
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
	
	String configJson = "{  \"userName\": \"techmahindra\",  \"password\": \"Tech@1234\",  \"host\": \"10.20.44.24\",  \"port\": 22,  \"data_broker_map\": {        \"script\": \"test sample script\",		\"target_system_url\": \"file://10.20.44.24:22/home/techmahindra/vaibhav/predict.csv\",        \"local_system_data_file_path\": \"SampleCSVFile_11kb.csv\",        \"first_row\": \"contains_field_names\",        \"csv_file_field_separator\": \",\",        \"data_broker_type\": \"csv\",        \"map_inputs\":[                 {                    \"input_field\":{                       \"name\":\"date\",                     \"type\":\"string\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"cpu\",                     \"type\":\"float\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.2\"                  }               },               {                    \"input_field\":{                       \"name\":\"Memory\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.1\"                  }               },               {                    \"input_field\":{                       \"name\":\"Network\",                     \"type\":\"String\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"id\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               }        ],        \"map_outputs\":[                 {                    \"output_field\":{                       \"tag\":\"1.2\",                     \"name\":\"binary_stream\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               },               {                    \"output_field\":{                       \"tag\":\"1.1\",                     \"name\":\"mime_type\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               }            ]         },  \"protobufFile\": \"syntax = \\\"proto3\\\";\\n\\nmessage DataFrameRow {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\nmessage DataFrame { \\n  required DataFrameRow rows = 1;\\n }\\n\\nmessage Prediction {\\n  repeated int64 idx = 1;\\n  repeated int64 class = 2;\\n  repeated int64 prediction = 3;\\n}\\n\\nmessage AggregateData {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\n\\nservice Predictor-R {\\n  rpc transform (DataFrame) returns (Prediction);\\n}\"}";
	
	@Test
	public void configureEnvironment() throws Exception {
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/configDB")
				.accept(MediaType.APPLICATION_JSON).content(configJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(200, response.getStatus());				
	}
	
	@Test //(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
	public void configureEnvironmentThrowsException() throws Exception {
		String configJson1 = "{  \"userName\": \"techmahindra\",  \"password\": \"Tech@1234\",  \"host\": \"10.20.44.24\",  \"port\": 22,  \"data_broker_map\": {        \"script\": \"test sample script\",		\"target_system_url\": \"file://10.20.44.24:22/home/techmahindra/vaibhav/predict.csv\",        \"local_system_data_file_path\": \"SampleCSVFile_11kb.csv\",        \"first_row\": \"contains_field_names\",        \"csv_file_field_separator\": \",\",        \"data_broker_type\": \"csv\",        \"map_inputs\":[                 {                    \"input_field\":{                       \"name\":\"date\",                     \"type\":\"string\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"cpu\",                     \"type\":\"float\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.2\"                  }               },               {                    \"input_field\":{                       \"name\":\"Memory\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.1\"                  }               },               {                    \"input_field\":{                       \"name\":\"Network\",                     \"type\":\"String\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"id\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               }        ],        \"map_outputs\":[                 {                    \"output_field\":{                       \"tag\":\"1.2\",                     \"name\":\"binary_stream\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               },               {                    \"output_field\":{                       \"tag\":\"1.1\",                     \"name\":\"mime_type\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               }            ]         },  \"protobufFile\": \"syntax = \\\"proto3\\\";\n\nmessage DataFrameRow {\n  repeated string mime_type = 1;\n  repeated string binary_stream = 2;\n}\nmessage DataFrame { \n  required DataFrameRow rows = 1;\n }\n\nmessage Prediction {\n  repeated int64 idx = 1;\n  repeated int64 class = 2;\n  repeated int64 prediction = 3;\n}\n\nmessage AggregateData {\n  repeated string mime_type = 1;\n  repeated string binary_stream = 2;\n}\n\nservice Predictor-R {\n  rpc transform (DataFrame) returns (Prediction);\n}\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/configDB")
				.accept(MediaType.APPLICATION_JSON).content(configJson1)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(400, response.getStatus());				
	}
	
	@Test
	public void pullData() throws Exception { 
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/configDB")
				.accept(MediaType.APPLICATION_JSON).content(configJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		String result = "this is test";
		Mockito.when(service.getOneRecord()).thenReturn(result.getBytes());
		Mockito.when(confService.getResultsetSize()).thenReturn(1);
		requestBuilder = MockMvcRequestBuilders.get("/pullData");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		assertEquals(result, response.getContentAsString());
		  
		//Exception 
		Mockito.when(service.getOneRecord()).thenThrow(new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getData()"));
		
		 requestBuilder = MockMvcRequestBuilders.get("/pullData");
		 mvcresult = mockMvc.perform(requestBuilder).andReturn();
		 
		 response = mvcresult.getResponse();
		 assertEquals("{\"status\":500,\"message\":\"No Data Found !!!\"}", response.getContentAsString());
		
		confService.setResultsetSize(-1);
		OutputStream out = new FileOutputStream("test.txt");
		Mockito.when(confService.getResultsetSize()).thenReturn(-1);
		Mockito.doNothing().when(service).writeDataTo(out);
		requestBuilder = MockMvcRequestBuilders.get("/pullData");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(0, mvcresult.getResponse().getContentLength());
		
		//TODO : Need to check, current logic is not covering the exception.
		confService.setResultsetSize(-1);
		Mockito.when(confService.getResultsetSize()).thenReturn(1);
		Mockito.doThrow(ServiceException.class).when(service).writeDataTo(out);
		//Mockito.doNothing().when(service).writeDataTo(out);
		requestBuilder = MockMvcRequestBuilders.get("/pullData");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		response = mvcresult.getResponse();
		assertEquals("{\"status\":500,\"message\":\"No Data Found !!!\"}", response.getContentAsString());
		
			
		confService.setResultsetSize(2);
		Mockito.when(confService.getResultsetSize()).thenReturn(2);
		requestBuilder = MockMvcRequestBuilders.get("/pullData");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(0, mvcresult.getResponse().getContentLength());
		
	}
	
}
