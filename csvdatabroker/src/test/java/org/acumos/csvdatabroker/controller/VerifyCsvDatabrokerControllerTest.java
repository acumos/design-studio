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

import static org.junit.Assert.*;

import org.acumos.csvdatabroker.service.CSVDatabrokerService;
import org.junit.Test;
import org.junit.runner.RunWith;
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
//value = VerifyCsvDatabrokerController.class
@WebMvcTest(controllers = { VerifyCsvDatabrokerController.class, AdminCsvDatabrokerController.class }, secure = false)
public class VerifyCsvDatabrokerControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	String configJson = "{  \"userName\": \"techmahindra\",  \"password\": \"Tech@1234\",  \"host\": \"10.20.44.24\",  \"port\": 22,  \"data_broker_map\": {        \"script\": \"test sample script\",		\"target_system_url\": \"file://10.20.44.24:22/home/techmahindra/vaibhav/predict.csv\",        \"local_system_data_file_path\": \"SampleCSVFile_11kb.csv\",        \"first_row\": \"contains_field_names\",        \"csv_file_field_separator\": \",\",        \"data_broker_type\": \"csv\",        \"map_inputs\":[                 {                    \"input_field\":{                       \"name\":\"date\",                     \"type\":\"string\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"cpu\",                     \"type\":\"float\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.2\"                  }               },               {                    \"input_field\":{                       \"name\":\"Memory\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"1.1\"                  }               },               {                    \"input_field\":{                       \"name\":\"Network\",                     \"type\":\"String\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               },               {                    \"input_field\":{                       \"name\":\"id\",                     \"type\":\"int\",                     \"checked\":\"YES\",                     \"mapped_to_field\":\"\"                  }               }        ],        \"map_outputs\":[                 {                    \"output_field\":{                       \"tag\":\"1.2\",                     \"name\":\"binary_stream\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               },               {                    \"output_field\":{                       \"tag\":\"1.1\",                     \"name\":\"mime_type\",                     \"type_and_role_hierarchy_list\":[                          {                             \"name\":\"string\",                           \"role\":\"repeated\"                        },                        {                             \"name\":\"DataFrameRow\",                           \"role\":\"\"                        },                        {                             \"name\":\"DataFrame\",                           \"role\":\"null\"                        }                     ]                  }               }            ]         },  \"protobufFile\": \"syntax = \\\"proto3\\\";\\n\\nmessage DataFrameRow {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\nmessage DataFrame { \\n  required DataFrameRow rows = 1;\\n }\\n\\nmessage Prediction {\\n  repeated int64 idx = 1;\\n  repeated int64 class = 2;\\n  repeated int64 prediction = 3;\\n}\\n\\nmessage AggregateData {\\n  repeated string mime_type = 1;\\n  repeated string binary_stream = 2;\\n}\\n\\nservice Predictor-R {\\n  rpc transform (DataFrame) returns (Prediction);\\n}\"}";
	
	@MockBean
	@Qualifier("CSVDatabrokerServiceImpl")
	CSVDatabrokerService service;
	
	@Test
	public void getProtobufFormattedData() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/configDB")
				.accept(MediaType.APPLICATION_JSON).content(configJson)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcresult = mockMvc.perform(requestBuilder).andReturn();
		
		
		//to cover the exception 
		/*requestBuilder = MockMvcRequestBuilders.get("/verify/getProtobufMessage")
				.param("messageName", "DataFrame")
				.param("InputData", "1,2,3,4");
				.accept(MediaType.APPLICATION_JSON).content(configJson)
				.contentType(MediaType.APPLICATION_JSON);
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals("{\"status\":417,\"message\":\"Failed to convert to protobuf format data !!!\"}",response.getContentAsString());
		*/
		requestBuilder = MockMvcRequestBuilders.get("/admin/getProtobuf");
		mvcresult = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = mvcresult.getResponse();
		System.out.println("*****************");
		System.out.println(response.getContentAsString());
	}
}
