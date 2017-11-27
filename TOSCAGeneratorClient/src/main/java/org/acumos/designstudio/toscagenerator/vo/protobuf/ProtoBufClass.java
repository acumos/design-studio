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

package org.acumos.designstudio.toscagenerator.vo.protobuf;

import java.io.Serializable;
import java.util.List;

public class ProtoBufClass implements Serializable{
	public ProtoBufClass() {
		// TODO Auto-generated constructor stub
	}
	
	private String syntax = "";
	private String packageName = "";
	private List<Option> listOfOption = null;
	private List<MessageBody> listOfMessages = null;
	private Service service = null;
	
	
	public String getSyntax() {
		return syntax;
	}
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<MessageBody> getListOfMessages() {
		return listOfMessages;
	}
	public void setListOfMessages(List<MessageBody> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	/**
	 * @return the listOfOption
	 */
	public List<Option> getListOfOption() {
		return listOfOption;
	}
	/**
	 * @param listOfOption the listOfOption to set
	 */
	public void setListOfOption(List<Option> listOfOption) {
		this.listOfOption = listOfOption;
	}
	
		

}
