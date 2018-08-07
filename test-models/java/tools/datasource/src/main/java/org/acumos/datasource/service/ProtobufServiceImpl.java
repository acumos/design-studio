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

package org.acumos.datasource.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.acumos.datasource.util.Constants;
import org.acumos.datasource.util.ProtobufUtil;
import org.acumos.datasource.vo.Configuration;
import org.acumos.datasource.vo.Protobuf;
import org.acumos.datasource.vo.ProtobufMessage;
import org.acumos.datasource.vo.ProtobufMessageField;
import org.springframework.stereotype.Component;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ServiceException;

@Component("ProtobufServiceImpl")
public class ProtobufServiceImpl implements ProtobufService {
	
	private Configuration conf;
	private Protobuf protobuf; 
	private DynamicSchema protobufSchema;
	
	@Override
	public Configuration getConf() throws CloneNotSupportedException {
		return (null != conf)? (Configuration)conf.clone() : null;
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	
	/**
	 * This method process the protobuf and sets the details.
	 * @param conf
	 * 		This method accepts conf
	 * @throws NullPointerException
	 * 		In case of any exception, this method throws the ServiceException
	 */
	public void processProtobuf() {
		try{
			String protobufStr = conf.getProtobufFileStr();
			protobuf = ProtobufUtil.parseProtobuf(protobufStr);
			setProbufSchem(protobuf);
			
		} catch (Exception e){
			e.printStackTrace();
			
		}
	}
	
	/**
	 * This method return the Protobuf Class set from the configuration details using API /configDB.
	 * @return Protobuf
	 * 			This method returns Protobuf
	 * @throws ServiceException
	 * 			In case of any exception, this method throws the ServiceException
	 */
	public Protobuf getProtobuf() throws NullPointerException {
		if(null == protobuf){
			throw new NullPointerException();
		} 
		return protobuf;
	}

	
	/**
	 * This method converters the ASCII data (input line) into protobuf binary format for the specified message name.
	 * @param messageName
	 * 		This method accepts messageName
	 * @param line
	 * 		This method accepts line
	 * @return
	 * 		This method returns byte[]
	 * @throws CloneNotSupportedException
	 * 		In case of any exception, this method throws the CloneNotSupportedException
	 * @throws InvalidProtocolBufferException
	 * 		In case of any exception, this method throws the InvalidProtocolBufferException
	 */
	public byte[] convertToProtobufFormat(String messageName, String line) throws CloneNotSupportedException, InvalidProtocolBufferException {
		byte[] result = null;
		DynamicMessage msg = null;
		msg = constructProtobufMessageData(messageName, line);
		result =  msg.toByteArray();
		return result;
	}
	
	/**
	 * This method read the protobuf binary formatted data (i.e., line) into specified message.
	 * @param messageName
	 * 		This method accepts messageName
	 * @param line
	 * 		This method accepts This method accepts messageName
	 * @return
	 * 		This method returns result as String
	 * @throws InvalidProtocolBufferException
	 * 		In case of any exception, this method throws the InvalidProtocolBufferException
	 */
	public String readProtobufFormat(String messageName, byte[] line) throws InvalidProtocolBufferException{
		String result = null;
		DynamicMessage msg = null;
		DynamicMessage.Builder msgBuilder = protobufSchema.newMessageBuilder(messageName);
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		msg = DynamicMessage.parseFrom(msgDesc, line);
		result = msg.toString();
		return result;
	}
	
	private void setProbufSchem(Protobuf protobuf) throws DescriptorValidationException {
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
				if(Constants.PROTOBUF_DATA_TYPE.contains(f.getType())){
					builder.addField(f.getRole(), f.getType(), f.getName(), f.getTag());
				} else if(f.getType().contains("enum")){
					//TODO : Include Enum 
				} else { 
					builder.addMessageDefinition(getNestedMsgDefinitionFrom(msgDefinitions, f.getType()));
					builder.addField(f.getRole(), f.getType(), f.getName(), f.getTag());
				}
			}
			msgDef = builder.build();
			msgDefinitions.put(msg.getName(), msgDef);
		}
		for(String key : msgDefinitions.keySet()){
			schemaBuilder.addMessageDefinition(msgDefinitions.get(key));
		}
		protobufSchema = schemaBuilder.build();
	}
	
	private MessageDefinition getNestedMsgDefinitionFrom(Map<String, MessageDefinition> msgDefinitions, String type) {
		MessageDefinition m = msgDefinitions.get(type);
		return m;
	}


	private DynamicMessage constructProtobufMessageData(String msgName, String line) throws CloneNotSupportedException {
		DynamicMessage msg = null;
		ProtobufMessage message = protobuf.getMessage(msgName);
		List<ProtobufMessageField> fields = message.getFields();
		DynamicMessage.Builder msgBuilder = protobufSchema.newMessageBuilder(msgName);
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		for(ProtobufMessageField f : fields){
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
		
		if(Constants.PROTOBUF_DATA_TYPE.contains(fieldType)){
			int mappedColumn = getMappedColumn(messageName, field);
			Object input = getValue(mappedColumn, line); 
			List<Object> inputs = getObjectList(input,fieldType);
			 for(Object o : inputs){
				 value = convertTo(o, fieldType);
				 values.add(value);
			 }
		} else {
			 value = constructProtobufMessageData(messageName + "." + field.getType(), line);
			 values.add(value);
		}
		return values;
	}

	private Object getValuefor(String messageName, ProtobufMessageField field, String line) throws CloneNotSupportedException {
		Object value = null;
		if(Constants.PROTOBUF_DATA_TYPE.contains(field.getType())){
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
			
			if(Constants.PROTOBUF_DATA_TYPE.contains(type)) {
				Object[] inputs = val.split(",");
				result = Arrays.asList(inputs);
			} else {
				Stack<String> parenthesis = new Stack<String>();
				result = new ArrayList<Object>();
				char[] chars = val.toCharArray();
				int start = 0;
				int end = 0;
				for(int i = 0; i < chars.length ; i ++){
					if(Constants.BEGIN_PARENTHESIS.indexOf(chars[i])  >= 0){ //begin paranthesis, push to stack
						if(parenthesis.isEmpty()){
							start = i;
						}
						parenthesis.push(String.valueOf(chars[i]));
					} else if(Constants.END_PARENTHESIS.indexOf(chars[i])  >= 0){
						parenthesis.pop();
						//if paranthesis stack is empty then get the input string
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
		char splitby = ',';
		Object result = null;
		char[] chars = line.toCharArray();
		Stack<String> parenthesis = new Stack<String>();
		int start = 0;
		int end; 
		String v = "";
		List<String> columns = new ArrayList<String>();
		for(int i =0 ; i < chars.length; i ++){
			if(Constants.BEGIN_PARENTHESIS.indexOf(chars[i])  >= 0){ //begin parenthesis, push to stack
				if(parenthesis.isEmpty()){
					start = i;
				}
				parenthesis.push(String.valueOf(chars[i]));
			} else if(Constants.END_PARENTHESIS.indexOf(chars[i])  >= 0){
				parenthesis.pop();
				if(parenthesis.isEmpty()){
					end = i;
					v = null;
					v = line.substring(start,end+1);
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
		mappedColumn = field.getTag() - 1;
		return mappedColumn;
	}

}
