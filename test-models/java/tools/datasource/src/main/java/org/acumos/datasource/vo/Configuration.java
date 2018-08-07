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

import java.io.Serializable;

public class Configuration implements Serializable, Cloneable  {

	private static final long serialVersionUID = -1157991065733494655L;

	
	private String endpointURL;
	private String protobufFileStr;
	
	
	
	/**
	 * @return the endpointURL
	 */
	public String getEndpointURL() {
		return endpointURL;
	}

	/**
	 * @param endpointURL the endpointURL to set
	 */
	public void setEndpointURL(String endpointURL) {
		this.endpointURL = endpointURL;
	}

	/**
	 * @return the protobufFileStr
	 */
	public String getProtobufFileStr() {
		return protobufFileStr;
	}

	/**
	 * @param protobufFileStr the protobufFileStr to set
	 */
	public void setProtobufFileStr(String protobufFileStr) {
		this.protobufFileStr = protobufFileStr;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
