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

package org.acumos.sqldatabroker.service;

import static org.junit.Assert.assertNotNull;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.util.ProtobufUtil;
import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.Protobuf;
import org.acumos.sqldatabroker.vo.ProtobufMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;



@RunWith(MockitoJUnitRunner.class)
public class ProtobufServiceTest {

	@InjectMocks
	ProtobufServiceImpl protobufServiceImpl;
	
	/*@Mock
	Protobuf protobuf1;*/
	
	/*@Mock
	ProtobufMessage protobufMessage;*/
	
	@Mock
	DynamicSchema protobufSchema;
	
	/*@Mock
	ProtobufService services;*/
	
	@Mock
	ConfigurationService confService;
	
	/*@Mock
	ProtobufUtil protobufUtil;*/
	
	Configuration conf = null;
	
	
	@Before
	public void setUp() throws Exception {
		String protobufStr = "message DataFrame { repeated string mime_type = 1;\n} \n\" service Predictor { \"\n\"  rpc predict (AggregateData) returns (Prediction); rpc transform (DataFrame) returns (Prediction);}";		//protobufStr = String.format(protobufStr, "ok", true, "");
		
		String configJson = "{	\"script\": \"test script\",	\"target_system_url\": \"jdbc:mysql://10.20.126.175:3306/cds?useSSL=false\",	\"local_system_data_file_path\": \"data1.csv\",	\"first_row\": \"contains_field_names\",	\"csv_file_field_separator\": \",\",	\"data_broker_type\": \"csv\",	\"map_inputs\": [	  {		\"input_field\": {		  \"name\": \"Column1\",		  \"type\": \"int\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"1\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column2\",		  \"type\": \"int\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"2\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column3\",		  \"type\": \"int\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"3\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column4\",		  \"type\": \"string\",		  \"checked\": \"NO\",		  \"mapped_to_field\": \"null\"		}	  }	],	\"map_outputs\": [	  {		\"output_field\": {		  \"tag\": \"1\",		  \"name\": \"f1\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"double\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"2\",		  \"name\": \"f2\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"double\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"3\",		  \"name\": \"s\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"string\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  }	],  \"protobufFile\": \"syntax = \\\"proto3\\\";\\n\\nmessage ComputeInput {\\n  double f1 = 1;\\n  double f2 = 2;\\n  string s = 3;\\n}\\n\\nmessage ComputeResult {\\n  double f = 1;\\n  string s = 2;\\n}\\n\\nservice Model {\\n  rpc multiply (ComputeInput) returns (ComputeResult);\\n}\\n\",\"user_id\": \"CDS_USER\",  \"password\": \"CDS_PASS\",\"database_name\": \"cds\",\"table_name\": \"c_user\",\"jdbc_driver_data_source_class_name\":\"com.mysql.jdbc.Driver\"}";
		configJson = "{	\"script\": \"\",	\"target_system_url\": \"jdbc:mysql://10.20.126.175:3306/cds?useSSL=false\",	\"local_system_data_file_path\": \"\",	\"first_row\": \"\",	\"csv_file_field_separator\": \",\",	\"data_broker_type\": \"SQL\",	\"map_inputs\": [	  {		\"input_field\": {		  \"name\": \"Column1\",		  \"type\": \"string\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"1\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column2\",		  \"type\": \"string\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"2\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column3\",		  \"type\": \"string\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"3\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column4\",		  \"type\": \"string\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"4\"		}	  },	  {		\"input_field\": {		  \"name\": \" Column5\",		  \"type\": \"string\",		  \"checked\": \"YES\",		  \"mapped_to_field\": \"5\"		}	  }	],	\"map_outputs\": [	  {		\"output_field\": {		  \"tag\": \"1\",		  \"name\": \"f1\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"double\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"2\",		  \"name\": \"f2\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"double\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"3\",		  \"name\": \"f3\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"string\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"4\",		  \"name\": \"f4\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"string\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  },	  {		\"output_field\": {		  \"tag\": \"5\",		  \"name\": \"f5\",		  \"type_and_role_hierarchy_list\": [			{			  \"name\": \"string\",			  \"role\": \"\"			},			{			  \"name\": \"ComputeInput\",			  \"role\": \"null\"			}		  ]		}	  }	],  \"protobufFile\": \"syntax = \\\"proto3\\\";\\n\\nmessage ComputeInput {\\n  string f1 = 1;\\n  string f2 = 2;\\n  string f3 = 3;\\n  string f4 = 4;\\n  string f5 = 5;\\n}\\n\\nmessage ComputeResult {\\n  double f = 1;\\n  string s = 2;\\n}\\n\\nservice Model {\\n  rpc multiply (ComputeInput) returns (ComputeResult);\\n}\\n\",\"user_id\": \"CDS_USER\",  \"password\": \"CDS_PASS\",\"database_name\": \"cds\",\"table_name\": \"c_role\",\"jdbc_driver_data_source_class_name\":\"com.mysql.jdbc.Driver\"}";
		ObjectMapper mapper = new ObjectMapper();
		conf = new Configuration();
		conf = mapper.readValue(configJson, Configuration.class);
		
		Mockito.when(confService.getConf()).thenReturn(conf);
	}
	
	@Test
	public void processProtobuf() throws Exception {
		
		protobufServiceImpl.processProtobuf(conf);
		assertNotNull(conf);
	}
	
	@Test(expected = ServiceException.class)
	public void processProtobufException() throws Exception {
		
		protobufServiceImpl.processProtobuf(null);
		assertNotNull(conf);
	}
	
	@Test
	public void getProtobuf() throws Exception {
		protobufServiceImpl.processProtobuf(conf);
		Protobuf result = protobufServiceImpl.getProtobuf();
		assertNotNull(result);
	}
	
	@Test(expected = ServiceException.class)
	public void getProtobufException() throws Exception {
		Protobuf result = protobufServiceImpl.getProtobuf();
		assertNotNull(result);
	}
	
	@Test
	public void convertToProtobufFormat() throws CloneNotSupportedException, ServiceException{
		String line = "test1,test2,test3,test4,test5";
		protobufServiceImpl.processProtobuf(conf);
		byte[] result = protobufServiceImpl.convertToProtobufFormat(line);
		assertNotNull(result);
	}
	
	@Test
	public void convertToProtobufFormatWithMessageName() throws CloneNotSupportedException, ServiceException, InvalidProtocolBufferException{
		String line = "test1,test2,test3,test4,test5";
		String messageName = "ComputeInput";
		protobufServiceImpl.processProtobuf(conf);
		byte[] result = protobufServiceImpl.convertToProtobufFormat(messageName, line);
		assertNotNull(result);
	}
	
		
	@Test
	public void readProtobufFormat() throws Exception {
		
		DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
		schemaBuilder.setName("PersonSchemaDynamic.proto");
		
		MessageDefinition msgDef = MessageDefinition.newBuilder("Prediction") // message Person
				.addField("required", "int32", "demo1", 1)		// required int32 id = 1
				.addField("required", "string", "name", 2)	// required string name = 2
				.addField("optional", "string", "email", 3)	// optional string email = 3
				.build();
		
		schemaBuilder.addMessageDefinition(msgDef);
		DynamicSchema schema = schemaBuilder.build();
		DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Prediction"); 
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		DynamicMessage msg = msgBuilder
				.setField(msgDesc.findFieldByName("demo1"), 1)
				.setField(msgDesc.findFieldByName("name"), "surya")
				.setField(msgDesc.findFieldByName("email"), "at@sis.gov.uk")
				.build();
		
		byte[] line = msg.toByteArray();
		
		Mockito.when(protobufSchema.newMessageBuilder("Prediction")).thenReturn(msgBuilder);
		
		
		String result = protobufServiceImpl.readProtobufFormat("Prediction",line);
		assertNotNull(result);
	}

}
	