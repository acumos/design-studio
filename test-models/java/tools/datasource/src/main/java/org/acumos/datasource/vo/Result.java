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

package org.acumos.datasource.vo;

public class Result {
	private int status;
	private Object message;
	
	public Result(){
		
	}
	
	
	public Result(int status, Object message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	/**
	 * @return status
	 * 			This method returns status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status 
	 * 			This method accepts status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return message
	 * 		This method returns message
	 */
	public Object getMessage() {
		return message;
	}
	/**
	 * @param message 
	 * 		This method accepts message
	 */
	public void setMessage(Object message) {
		this.message = message;
	}
	
	
}
