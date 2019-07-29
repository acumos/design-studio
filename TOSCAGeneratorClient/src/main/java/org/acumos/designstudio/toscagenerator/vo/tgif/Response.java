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

package org.acumos.designstudio.toscagenerator.vo.tgif;

import java.io.Serializable;

import org.json.simple.JSONArray;

public class Response implements Serializable {

	private static final long serialVersionUID = 7215402687596230670L;

	private JSONArray format;
	private String version;

	public Response() {

	}

	/**
	 * 
	 * @param format
	 *            JSONArray
	 * @param version
	 *            Version string
	 */
	public Response(JSONArray format, String version) {
		super();
		this.format = format;
		this.version = version;
	}

	/**
	 * @return the format
	 */
	public JSONArray getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(JSONArray format) {
		this.format = format;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}
