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
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.acumos.designstudio.toscagenerator.vo.protobuf.*;
import org.acumos.designstudio.toscagenerator.vo.tgif.Auxiliary;
import org.acumos.designstudio.toscagenerator.vo.tgif.Parameter;
import org.junit.Test;

import com.google.gson.Gson;

public class ProtobufTest {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(TGIFTest.class);

	/**
	 * The test case method generates protoBuf.json. The file is utilized to
	 * create TGIF.json which is used by ds-composition engine to represent a
	 * model.
	 * 
	 */
	@Test
	public void testProtobuf() throws Exception {
		ProtoBufClass protoBufInstance = new ProtoBufClass();
		protoBufInstance.setSyntax("proto3");
		protoBufInstance.getSyntax();
		protoBufInstance.setPackageName("java_package");
		protoBufInstance.getPackageName();
		Option option1 = new Option();
		option1.setKey("java_package");
		option1.getKey();
		option1.setValue("com.google.protobuf");
		option1.getValue();

		Option option2 = new Option();
		option2.setKey("java_outer_classname");
		option2.setValue("DatasetProto");

		List<Option> listOfOption = new ArrayList<Option>();
		listOfOption.add(option1);
		listOfOption.add(option2);

		protoBufInstance.setListOfOption(listOfOption);
		protoBufInstance.getListOfOption();
		
		MessageBody messageBody = new MessageBody();
		messageBody.setMessageName("DataFrame");

		List<MessageargumentList> messageargumentList = getComplexType();

		messageBody.setMessageargumentList(messageargumentList);

		List<MessageBody> listOfMessages = new ArrayList<MessageBody>();
		listOfMessages.add(messageBody);

		protoBufInstance.setListOfMessages(listOfMessages);

		Service service = new Service();
		service.setName("Aggrgator");
		service.getName();
		Operation operation = new Operation();
		operation.setOperationType("rpc");
		operation.getOperationType();
		operation.setOperationName("aggregate");
		operation.getOperationName();

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
		service.getListOfOperations();
		protoBufInstance.setService(service);
		assertNotNull(protoBufInstance);
		Gson gson = new Gson();
		String protoBufToJsonString = gson.toJson(protoBufInstance);
		assertNotNull(protoBufToJsonString);
		logger.info("protoBufToJsonString" + protoBufToJsonString);
		
		ComplexType cType = new ComplexType();
		List<MessageargumentList> msgArgList = getComplexType();
		cType.setMessageargumentList(msgArgList);
		cType.getMessageargumentList();
		cType.setMessageName("MsgName");
		cType.getMessageName();

	}
	
	@Test
	public void ArtifactTest(){
		Artifact arti = new Artifact();
		arti.setContentLength(1);
		arti.getContentLength();
		arti.setExtension(".proto");
		arti.getExtension();
		arti.setName("Artifact");
		arti.getName();
		arti.setNexusURI("uri");
		arti.getNexusURI();
		arti.setPayloadURI("payloadUri");
		arti.getPayloadURI();
		arti.setSolutionID("1");
		arti.getSolutionID();
		arti.setType("Type");
		arti.getType();
		arti.setVersion("1");
		arti.getVersion();
		arti = new Artifact("Name", ".proto", "1", "1", "payloadUri", 1);
	}
	
	@Test
	public void messageSortTest(){
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
		messageargumentInstance2.setComplexType(new ComplexType());
		messageargumentInstance2.getComplexType();
		
		MessageSortByTag tag = new MessageSortByTag();
		tag.compare(messageargumentInstance1, null);
		
		SortFactory factory = new SortFactory();
		factory.getComparator();
	}

	private List<MessageargumentList> getComplexType() {
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
		messageargumentInstance2.setComplexType(new ComplexType());
		messageargumentInstance2.getComplexType();
		
		MessageSortByTag tag = new MessageSortByTag();
		tag.compare(messageargumentInstance1, messageargumentInstance2);
		
		List<MessageargumentList> messageargumentList = new ArrayList<MessageargumentList>();
		messageargumentList.add(messageargumentInstance1);
		messageargumentList.add(messageargumentInstance2);
		return messageargumentList;
	}
}
