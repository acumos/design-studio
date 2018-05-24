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

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.service.CSVDatabrokerService;
import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.service.ProtobufService;
import org.acumos.csvdatabroker.service.ProtobufServiceImpl;
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
@WebMvcTest(value = VerifyCsvDatabrokerController.class, secure = false)
public class VerifyCsvDatabrokerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	String configJson = "{  \"userName\": \"techmahindra\",  \"password\": \"Tech@1234\",  \"host\": \"10.20.44.24\",  \"port\": 22,  \"data_broker_map\": {        \"script\": \"test sample script\",		\"target_system_url\": \"file://10.20.44.24:22/home/techmahindra/vaibhav/predict.csv\",        \"local_system_data_file_path\": \"SampleCSVFile_11kb.csv\",        \"first_row\": \"contains_field_names\",        \"csv_file_field_separator\": \",\",        \"data_broker_type\": \"csv\",        \"map_inputs\":[                 {                    \"input_field\":{                       \"name\":\"date\",                     \"type\":\"string\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"cpu\",                     \"type\":\"float\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.2\"                  }               },               {                    \"input_field\":{                       \"name\":\"Memory\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.1\"                  }               },               {                    \"input_field\":{                       \"name\":\"Network\",                     \"type\":\"String\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"id\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               }        ],        \"map_outputs\":[                 {                    \"output_field\":{                       \"tag\":\"1.2\",                     \"name\":\"binary_stream\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               },               {                    \"output_field\":{                       \"tag\":\"1.1\",                     \"name\":\"mime_type\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               }            ]         },  \"protobufFile\": \"syntax = \\\"proto3\\\";\\n\\nmessage DataFrameRow {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\nmessage DataFrame { \\n  required DataFrameRow rows = 1;\\n }\\n\\nmessage Prediction {\\n  repeated int64 idx = 1;\\n  repeated int64 class = 2;\\n  repeated int64 prediction = 3;\\n}\\n\\nmessage AggregateData {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\n\\nservice Predictor-R {\\n  rpc transform (DataFrame) returns (Prediction);\\n}\"}";
	
	
	@MockBean
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@MockBean
	@Qualifier("ProtobufServiceImpl")
	private ProtobufServiceImpl protoService;
	
	@Test
	public void getProtobufFormattedData() throws Exception{
		
		MvcResult mvcresult = mockMvc.perform(MockMvcRequestBuilders.get("/verify/getProtobufMessage")
				.param("messageName","test")
				.param("InputData","demo")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void getDataForProtobufFormat1() throws Exception{
		
		byte[] protobufMessage = null;
		String messageName1 = null;
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/verify/getData1");
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		
		Mockito.when(protoService.readProtobufFormat(messageName1,protobufMessage)).thenThrow(new NullPointerException());
		
		requestBuilder = MockMvcRequestBuilders.get("/verify/getData1");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(0, mvcresult.getResponse().getContentLength());
	}
}
