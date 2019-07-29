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

package org.acumos.designstudio.ce.vo.compositeproto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Protobuf implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3685522023286927954L;
	
	private String syntax; 
	private List<ProtobufOption> options;
	private ProtobufService service;
	private List<ProtobufMessage> messages;
	@JsonProperty(value="package")
	private String packageName;
	
	
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Protobuf(){
		options = new ArrayList<ProtobufOption>();
		messages = new ArrayList<ProtobufMessage>();
	}
	
	/**
	 * @return the syntax
	 */
	public String getSyntax() {
		return syntax;
	}
	/**
	 * @param syntax the syntax to set
	 */
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	/**
	 * @return the options
	 */
	public List<ProtobufOption> getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(List<ProtobufOption> options) {
		this.options = options;
	}
	/**
	 * @return the service
	 */
	public ProtobufService getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(ProtobufService service) {
		this.service = service;
	}
	/**
	 * @return the messages
	 */
	public List<ProtobufMessage> getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<ProtobufMessage> messages) {
		this.messages = messages;
	}
	
	/**
        * This method returns ProtobufMessage for given messageName
        * @param messageName
        *	The name of the Protobuf message
        */
	public ProtobufMessage getMessage(String messageName){
		//check if messageName is for nested message 
		if(messageName.indexOf(".") != -1){
			messageName = messageName.substring(messageName.lastIndexOf(".")+1);
		}
		ProtobufMessage message = null;
		for(ProtobufMessage m : messages){
			if(m.getName().equals(messageName)){
				message = m; 
				break;
			}
		}
		return message;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("syntax = " + syntax + ";\n");
		sb.append("package " + packageName + ";\n");
		sb.append("\n");
		for(ProtobufOption o : options){
			sb.append(o.toString());
		}
		//sb.append("\n");
		for(ProtobufMessage m : messages){
			sb.append(m.toString());
			sb.append("\n");
		}
		//sb.append("\n");
		sb.append(service.toString());
		sb.append("\n");
		return sb.toString();
	}
	
}