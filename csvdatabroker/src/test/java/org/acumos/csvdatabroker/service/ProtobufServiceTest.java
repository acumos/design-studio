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

package org.acumos.csvdatabroker.service;

import static org.junit.Assert.assertNotNull;

import org.acumos.csvdatabroker.util.ProtobufUtil;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.Protobuf;
import org.acumos.csvdatabroker.vo.ProtobufMessage;
import org.acumos.csvdatabroker.vo.ProtobufService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;


@RunWith(MockitoJUnitRunner.class)
public class ProtobufServiceTest {

	@InjectMocks
	private CSVDatabrokerServiceImpl service;
	
	@InjectMocks
	ProtobufServiceImpl protobufServiceImpl;
	
	@Mock
	RemoteScriptExecutor executor; 
	
	@Mock
	Protobuf protobuf1;
	
	@Mock
	ProtobufMessage protobufMessage;
	
	@Mock
	DynamicSchema protobufSchema;
	
	@Mock
	ProtobufService services;
	
	@Mock
	ConfigurationService confService;
	
	@Mock
	ProtobufUtil protobufUtil;
	
	Configuration conf = null;
	
	
	@Test
	public void processProtobuf() throws Exception {
		String protobufStr = "message DataFrame { repeated string mime_type = 1;\n} \n\" service Predictor { \"\n\"  rpc predict (AggregateData) returns (Prediction); rpc transform (DataFrame) returns (Prediction);}";		//protobufStr = String.format(protobufStr, "ok", true, "");
		
		Configuration conf = new Configuration();
		
		conf.setProtobufFile(protobufStr);
		protobufServiceImpl.processProtobuf(conf);
		assertNotNull(conf);
	}
	
	@Test
	public void getProtobuf() throws Exception {
		Protobuf result = protobufServiceImpl.getProtobuf();
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
		Mockito.when(confService.getConf()).thenReturn(conf);
		
		String result = protobufServiceImpl.readProtobufFormat("Prediction",line);
		assertNotNull(result);
	}

}
	