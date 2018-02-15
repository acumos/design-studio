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


/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;

public class NodeOperationSignature extends BaseOperationSignature {

	private static final long serialVersionUID = 7867606266539774888L;

	private String input_message_name;
	private String output_message_name;
	
	/**
	 * @return the input_message_name
	 */
	public String getInput_message_name() {
		return input_message_name;
	}
	/**
	 * @param input_message_name the input_message_name to set
	 */
	public void setInput_message_name(String input_message_name) {
		this.input_message_name = input_message_name;
	}
	/**
	 * @return the output_message_name
	 */
	public String getOutput_message_name() {
		return output_message_name;
	}
	/**
	 * @param output_message_name the output_message_name to set
	 */
	public void setOutput_message_name(String output_message_name) {
		this.output_message_name = output_message_name;
	}
	
	
	
}
