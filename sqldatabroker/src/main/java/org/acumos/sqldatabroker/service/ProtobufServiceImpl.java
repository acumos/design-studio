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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.util.DatabrokerConstants;
import org.acumos.sqldatabroker.util.EELFLoggerDelegator;
import org.acumos.sqldatabroker.util.ProtobufUtil;
import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.DBInputField;
import org.acumos.sqldatabroker.vo.DBMapInput;
import org.acumos.sqldatabroker.vo.DBMapOutput;
import org.acumos.sqldatabroker.vo.DBOTypeAndRoleHierarchy;
import org.acumos.sqldatabroker.vo.DBOutputField;
import org.acumos.sqldatabroker.vo.Protobuf;
import org.acumos.sqldatabroker.vo.ProtobufMessage;
import org.acumos.sqldatabroker.vo.ProtobufMessageField;
import org.acumos.sqldatabroker.vo.ProtobufServiceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;

@Component("ProtobufServiceImpl")
public class ProtobufServiceImpl implements ProtobufService {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ProtobufServiceImpl.class);
	
	private Protobuf protobuf; 
	private DynamicSchema protobufSchema;
	
	@Autowired
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationService confService;
	
	@Override
	public void processProtobuf(Configuration conf) throws ServiceException {
		try{
			String protobufStr = conf.getProtobufFile();
			protobuf = ProtobufUtil.parseProtobuf(protobufStr);
			setProbufSchem(protobuf);
			
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in processProtobuf()", e);
			throw new ServiceException("Invalid Protobuf or Protobuf Definition not found","401", "Invalid Protobuf or Protobuf Definition not found", e);
		}
	}
	
	@Override
	public Protobuf getProtobuf() throws ServiceException {
		if(null == protobuf){
			throw new ServiceException("Invalid Protobuf or Protobuf Definition not found","401", "Invalid Protobuf or Protobuf Definition not found");
		} 
		return protobuf;
	}

	@Override
	public byte[] convertToProtobufFormat(String line) throws CloneNotSupportedException{
		byte[] result = null;
		List<ProtobufServiceOperation> operations = protobuf.getService().getOperations();
		List<String> inputMessageNames = getConnectedPortInputMsgNames(operations);
		DynamicMessage msg = null;
		for(String msgName : inputMessageNames){
			//TODO : Logic may need to modify if the rpc call have multiple input parameters.
			msg = constructProtobufMessageData(msgName, line);
			result = msg.toByteArray();
		}
		return result;
	}

	@Override
	public byte[] convertToProtobufFormat(String messageName, String line) throws CloneNotSupportedException, InvalidProtocolBufferException {
		byte[] result = null;
		DynamicMessage msg = null;
		msg = constructProtobufMessageData(messageName, line);
		result =  msg.toByteArray();
		return result;
	}
	
	@Override
	public String readProtobufFormat(String messageName, byte[] line) throws InvalidProtocolBufferException{
		String result = null;
		DynamicMessage msg = null;
		// Create dynamic message from schema
		DynamicMessage.Builder msgBuilder = protobufSchema.newMessageBuilder(messageName);
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		msg = DynamicMessage.parseFrom(msgDesc, line);
		result = msg.toString();
		return result;
	}
	
	private void setProbufSchem(Protobuf protobuf) throws DescriptorValidationException {
		// Create dynamic schema
		DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
		schemaBuilder.setName("DatabrokerSchemaDynamic.proto");
		Map<String, MessageDefinition> msgDefinitions = new HashMap<String,MessageDefinition>();
		
		
		MessageDefinition msgDef = null;
		MessageDefinition.Builder builder = null;
		List<ProtobufMessageField> fields = null;
		
		List<ProtobufMessage> messages = protobuf.getMessages();
		for(ProtobufMessage msg : messages){ //add MessageDefinition to msgDefinitions
			builder = MessageDefinition.newBuilder(msg.getName());
			fields = msg.getFields();
			for(ProtobufMessageField f : fields){
				if(DatabrokerConstants.PROTOBUF_DATA_TYPE.contains(f.getType())){
					builder.addField(f.getRole(), f.getType(), f.getName(), f.getTag());
				} else if(f.getType().contains("enum")){
					//TODO : Include Enum 
				} else { //field is nested message, get it from msgDefinitions 
					builder.addMessageDefinition(getNestedMsgDefinitionFrom(msgDefinitions, f.getType()));
					builder.addField(f.getRole(), f.getType(), f.getName(), f.getTag());
				}
			}
			msgDef = builder.build();
			msgDefinitions.put(msg.getName(), msgDef);
			//schemaBuilder.addMessageDefinition(msgDef);
		}
		for(String key : msgDefinitions.keySet()){
			schemaBuilder.addMessageDefinition(msgDefinitions.get(key));
		}
		protobufSchema = schemaBuilder.build();
	}
	
	private MessageDefinition getNestedMsgDefinitionFrom(Map<String, MessageDefinition> msgDefinitions, String type) {
		MessageDefinition m = msgDefinitions.get(type);
		msgDefinitions.remove(type);
		return m;
	}


	private DynamicMessage constructProtobufMessageData(String msgName, String line) throws CloneNotSupportedException {
		DynamicMessage msg = null;
		ProtobufMessage message = protobuf.getMessage(msgName);
		List<ProtobufMessageField> fields = message.getFields();
		// Create dynamic message from schema
		DynamicMessage.Builder msgBuilder = protobufSchema.newMessageBuilder(msgName);
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		for(ProtobufMessageField f : fields){
			//check if its repeated or not 
			if(f.getRole().equalsIgnoreCase("repeated")){
				List<Object> values = getRepeatedValues(message.getName(),f,line);
				for(Object value : values){
					msgBuilder.addRepeatedField(msgDesc.findFieldByName(f.getName()), value);
				}
			} else {
				msgBuilder.setField(msgDesc.findFieldByName(f.getName()), getValuefor(message.getName(), f, line));
			}
			
		}
		msg = msgBuilder.build();
		return msg;
	}


	/**
	 * This method process the data structure  collection, collection, ....
	 * e.g. [1,2,3],[4,5,6],1,2,3 and first two fields might be part of nested structure and might be repeated. 
	 * 
	 * @param messageName
	 * 		This method accepts messageName
	 * @param field
	 * 		This method accepts field
	 * @param line
	 * 		This method accepts line
	 * @return
	 * 		This method returns list of Object
	 * @throws CloneNotSupportedException
	 * 		In case of any exception, this method throws the CloneNotSupportedException
	 */
	private List<Object> getRepeatedValues(String messageName, ProtobufMessageField field, String line) throws CloneNotSupportedException {
		String fieldType = field.getType();
		List<Object> values = new ArrayList<Object>();
		Object value = null;
		
		if(DatabrokerConstants.PROTOBUF_DATA_TYPE.contains(fieldType)){
			int mappedColumn = getMappedColumn(messageName, field);
			Object input = getValue(mappedColumn, line); //get the corresponding input value from the line
			//Convert the input value in to list of objects
			List<Object> inputs = getObjectList(input,fieldType);
			 for(Object o : inputs){
				 value = convertTo(o, fieldType);
				 values.add(value);
			 }
		} else {
			 //[ ],[],1,2,3
			 //for(Object o : inputs){
				 value = constructProtobufMessageData(messageName + "." + field.getType(), line);
				 values.add(value);
			 //}
		}
		return values;
	}

	
	private Object getValuefor(String messageName, ProtobufMessageField field, String line) throws CloneNotSupportedException {
		Object value = null;
		if(DatabrokerConstants.PROTOBUF_DATA_TYPE.contains(field.getType())){
			int mappedColumn = getMappedColumn(messageName, field);
			if (mappedColumn >= 0) {
				value = getValue(mappedColumn, line);
				value = convertTo(value, field.getType());
			}
		} else {
			value = constructProtobufMessageData(messageName + "." + field.getType(), line);
		}
		return value;
	}


	private List<Object> getObjectList(Object value, String type) {
		List<Object> result = null;
		//System.out.println("input value class : " + value.getClass().getSimpleName());
		//Strip {} || [] || () || ""
		if(value.getClass().getSimpleName().equals("String")){
			String val = (String) value;
			if(checkBeginEnd(val,"\"","\"")){
				val = val.substring(1, val.length()-1);
			}
			
			if(checkBeginEnd(val,"{","}")){
				val = val.substring(1, val.length()-1);
			} else if(checkBeginEnd(val,"[","]")){
				val = val.substring(1, val.length()-1);
			} else if(checkBeginEnd(val,"(",")")){
				val = val.substring(1, val.length()-1);
			} 
			
			if(DatabrokerConstants.PROTOBUF_DATA_TYPE.contains(type)) {
				Object[] inputs = val.split(",");
				result = Arrays.asList(inputs);
			} else {
				//[],[],[]
				Stack<String> parenthesis = new Stack<String>();
				result = new ArrayList<Object>();
				char[] chars = val.toCharArray();
				int start = 0;
				int end = 0;
				for(int i = 0; i < chars.length ; i ++){
					if(DatabrokerConstants.BEGIN_PARENTHESIS.indexOf(chars[i])  >= 0){ //begin paranthesis, push to stack
						if(parenthesis.isEmpty()){
							start = i;
						}
						parenthesis.push(String.valueOf(chars[i]));
					} else if(DatabrokerConstants.END_PARENTHESIS.indexOf(chars[i])  >= 0){
						parenthesis.pop();
						//if parenthesis stack is empty then get the input string
						if(parenthesis.isEmpty()){
							end = i;
							String v = val.substring(start+1,end);
							result.add(v);
						}
					}
				}
			}
			
		} else if(value.getClass().getSimpleName().equals("ArrayList")){ //ArrayList
			//TODO : Need to be implemented 
		} else if(value.getClass().getSimpleName().contains("[")) { //Array
			//TODO : Need to be implemented 
		}
		
		return result;
	}
	
	private boolean checkBeginEnd(String input, String beginChar, String endChar) {
		boolean resultFlag = false;
		String startwith = String.valueOf(input.charAt(0));
		String endwith = String.valueOf(input.charAt(input.length()-1));
		if(startwith.equals(beginChar) && endwith.equals(endChar)){
			resultFlag = true;
		}
		
		return resultFlag;
	}


	private Object convertTo(Object value, String type) {
		Object result = null;
		String input = (String) value;
		if ("double".equals(type)) {
			result = Double.parseDouble(input);
		} else if ("float".equals(type)) {
			result = Float.parseFloat(input);
		} else if ("int32".equals(type)) {
			result = Integer.parseInt(input);
		} else if ("int64".equals(type)) {
			result = Long.parseLong(input);
		} else if ("unit32".equals(type)) {
			result = Integer.parseInt(input);
		} else if ("unit64".equals(type)) {
			result = Long.parseLong(input);
		} else if ("sint32".equals(type)) {
			result = Integer.parseInt(input);
		} else if ("sint64".equals(type)) {
			result = Long.parseLong(input);
		} else if ("fixed32".equals(type)) {
			result = Integer.parseInt(input);
		} else if ("fixed64".equals(type)) {
			result = Long.parseLong(input);
		} else if ("sfixed32".equals(type)) {
			result = Integer.parseInt(input);
		} else if ("sfixed64".equals(type)) {
			result = Long.parseLong(input);
		} else if ("bool".equals(type)) {
			result = Boolean.parseBoolean(input);
		} else if ("string".equals(type)) {
			result = input;
		} else if ("bytes".equals(type)) {
			result = input.getBytes(StandardCharsets.UTF_8);
		}
		return result;
	}


	private Object getValue(int mappedColumn, String line) throws CloneNotSupportedException {
		Configuration conf = confService.getConf();
		char splitby = conf.getCsv_file_field_separator().charAt(0);
		Object result = null;
		char[] chars = line.toCharArray();
		Stack<String> parenthesis = new Stack<String>();
		int start = 0;
		int end; 
		String v = "";
		List<String> columns = new ArrayList<String>();
		for(int i =0 ; i < chars.length; i ++){
			if(DatabrokerConstants.BEGIN_PARENTHESIS.indexOf(chars[i])  >= 0){ //begin parenthesis, push to stack
				if(parenthesis.isEmpty()){
					start = i;
				}
				parenthesis.push(String.valueOf(chars[i]));
			} else if(DatabrokerConstants.END_PARENTHESIS.indexOf(chars[i])  >= 0){
				parenthesis.pop();
				//if parenthesis stack is empty then get the input string
				if(parenthesis.isEmpty()){
					end = i;
					v = null;
					v = line.substring(start,end+1);
					//result.add(v);
				}
			} else if(chars[i] == splitby) {
				if(parenthesis.isEmpty()){
					columns.add(v.trim());
					v = "";
				}
			} else {
				if(parenthesis.isEmpty()){
					v = v+chars[i];
				}
			}
		}
		columns.add(v);
		if(null != columns && columns.size() > 0 ){
			result = columns.get(mappedColumn);
		}
		return result;
	}


	private int getMappedColumn(String messageName, ProtobufMessageField field) throws CloneNotSupportedException {
		int mappedColumn = -1;
		Configuration conf = confService.getConf();
		DBMapOutput[] outputs =    conf.getMap_outputs();
		
		
		int outputLength = outputs.length;
		DBOutputField outputField = null;
		
		DBOTypeAndRoleHierarchy typeAndRole = null;
		String outputFieldTag = null;
		
		for(int i = 0 ; i < outputLength ; i ++){
			outputField = outputs[i].getOutput_field();
			//check the field name 
			if(outputField.getName().equals(field.getName())){
				typeAndRole = outputField.getType_and_role_hierarchy_list()[1];
				if(typeAndRole.getName().equals(messageName)){
					outputFieldTag = outputField.getTag();
				}
			}
		}
		
		if(null != outputFieldTag){
			DBMapInput[] inputs = conf.getMap_inputs();
			DBInputField inputField = null;
			
			int inputLength = inputs.length;
			for(int i = 0 ; i < inputLength ; i ++ ) { 
				inputField = inputs[i].getInput_field();
				if(inputField.getMapped_to_field().equals(outputFieldTag)){
					mappedColumn = i;
					break;
				}
			}
		}
		return mappedColumn;
	}


	private List<String> getConnectedPortInputMsgNames(List<ProtobufServiceOperation> operations) {
		List<String> inputMessageNames = null;
		for(ProtobufServiceOperation o : operations){
			//TODO : Current logic returns the first operation's input msg name, but need to update the logic to return the connected port input message name 
			inputMessageNames = o.getInputMessageNames();
		}
		return inputMessageNames;
	}


	/**
	 * This method is to support JUnit
	 * @param confService
	 *        Instance of ConfigurationService
	 */
	public void setConfService(ConfigurationService confService){
		this.confService = confService;
	}
}
