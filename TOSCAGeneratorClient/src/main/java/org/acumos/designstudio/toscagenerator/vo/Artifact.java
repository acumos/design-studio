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

package org.acumos.designstudio.toscagenerator.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artifact {

	private String name;
	private String type;
	private String solutionID;
	private String version;
	private String nexusURI;
	private String payloadURI;
	private String extension;
	private int contentLength;

	public Artifact() {

	}

	/**
	 * 
	 * @param name
	 *            Artfact name
	 * @param extension
	 *            File extension
	 * @param solutionID
	 *            Solution ID
	 * @param version
	 *            Version string
	 * @param payloadURI
	 *            Payload URI
	 * @param length
	 *            Content length
	 */
	public Artifact(String name, String extension, String solutionID, String version, String payloadURI, int length) {
		this.name = name;
		this.type = "TOSCA" + name.toUpperCase();
		this.solutionID = solutionID;
		this.version = version;
		this.payloadURI = payloadURI + name + "." + extension;
		this.contentLength = length;
		this.extension = extension;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getPayloadURI() {
		return payloadURI;
	}

	public void setPayloadURI(String payloadURI) {
		this.payloadURI = payloadURI;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSolutionID() {
		return solutionID;
	}

	public void setSolutionID(String solutionID) {
		this.solutionID = solutionID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNexusURI() {
		return nexusURI;
	}

	public void setNexusURI(String nexusURI) {
		this.nexusURI = nexusURI;
	}

}
