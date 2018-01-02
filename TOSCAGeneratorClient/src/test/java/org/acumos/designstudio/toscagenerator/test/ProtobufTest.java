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

import static org.junit.Assert.assertNotNull;

import java.util.*;

import org.acumos.designstudio.toscagenerator.util.EELFLoggerDelegator;
import org.acumos.designstudio.toscagenerator.vo.protobuf.*;
import org.junit.Test;

import com.google.gson.Gson;

public class ProtobufTest {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(TGIFTest.class);
	@Test
	public void testProtobuf() throws Exception {
		ProtoBufClass  protoBufInstance = new ProtoBufClass();
		protoBufInstance.setSyntax("proto3");
		protoBufInstance.setPackageName("java_package");
		
		Option option1 = new Option();
		option1.setKey("java_package");
		option1.setValue("com.google.protobuf");
		
		Option option2 = new Option();
		option2.setKey("java_outer_classname");
		option2.setValue("DatasetProto");
		
		List<Option> listOfOption = new ArrayList<Option>();
		listOfOption.add(option1);
		listOfOption.add(option2);
		
		protoBufInstance.setListOfOption(listOfOption);
		
		MessageBody messageBody = new MessageBody();
		messageBody.setMessageName("DataFrame");
		
		MessageargumentList messageargumentInstance1 = new MessageargumentList();
		messageargumentInstance1.setRole("repeated");
		messageargumentInstance1.setType("string");
		messageargumentInstance1.setName("mime_type");
		messageargumentInstance1.setTag("1");
		
		MessageargumentList messageargumentInstance2 = new MessageargumentList();
		messageargumentInstance2.setRole("repeated");
		messageargumentInstance2.setType("int32");
		messageargumentInstance2.setName("binary_stream");
		messageargumentInstance2.setTag("2");
		
		List<MessageargumentList> messageargumentList = new ArrayList<MessageargumentList>();
		messageargumentList.add(messageargumentInstance1);
		messageargumentList.add(messageargumentInstance2);
		
		messageBody.setMessageargumentList(messageargumentList);
		
		List<MessageBody> listOfMessages = new ArrayList<MessageBody>();
		listOfMessages.add(messageBody);
		
		protoBufInstance.setListOfMessages(listOfMessages);
		
		Service service  = new Service();
		service.setName("Aggrgator");
		
		Operation operation = new Operation();
		operation.setOperationType("rpc");
		operation.setOperationName("aggregate");
		
		InputMessage inputMessage = new InputMessage();
		inputMessage.setInputMessageName("DataFrame");
		
		List<InputMessage> listofInputMessages = new ArrayList<InputMessage>();
		listofInputMessages.add(inputMessage);
		
		OutputMessage outputMessage = new OutputMessage();
		outputMessage.setOutPutMessageName("AggregateData");
		
		List<OutputMessage> listofOutputMessages = new ArrayList<OutputMessage>();
		listofOutputMessages.add(outputMessage);
		
		operation.setListOfInputMessages(listofInputMessages);
		operation.setListOfOutputMessages(listofOutputMessages);
		
		List<Operation> listOfOperations = new ArrayList<Operation>();
		listOfOperations.add(operation);
		
		service.setListOfOperations(listOfOperations);
		
		protoBufInstance.setService(service);
		assertNotNull(protoBufInstance);
		Gson gson = new Gson();
		String protoBufToJsonString = gson.toJson(protoBufInstance);
		assertNotNull(protoBufToJsonString);
		logger.info("protoBufToJsonString"+protoBufToJsonString);
		
		
	}
}
