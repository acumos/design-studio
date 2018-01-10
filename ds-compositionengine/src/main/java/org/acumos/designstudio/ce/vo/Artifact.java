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

package org.acumos.designstudio.ce.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artifact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 *            Artifact name
	 * @param extension
	 *            Artifact file extension
	 * @param solutionID
	 *            Solution ID
	 * @param version
	 *            Version
	 * @param payloadURI
	 *            Payload URL
	 * @param length
	 *            Content length
	 */
	public Artifact(String name, String extension, String solutionID, String version, String payloadURI, int length) {
		this.name = name;
		this.type = name.toUpperCase();
		this.solutionID = solutionID;
		this.version = version;
		this.payloadURI = payloadURI + name + "." + extension;
		this.contentLength = length;
		this.extension = extension;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the solutionID
	 */
	public String getSolutionID() {
		return solutionID;
	}

	/**
	 * @param solutionID
	 *            the solutionID to set
	 */
	public void setSolutionID(String solutionID) {
		this.solutionID = solutionID;
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

	/**
	 * @return the nexusURI
	 */
	public String getNexusURI() {
		return nexusURI;
	}

	/**
	 * @param nexusURI
	 *            the nexusURI to set
	 */
	public void setNexusURI(String nexusURI) {
		this.nexusURI = nexusURI;
	}

	/**
	 * @return the payloadURI
	 */
	public String getPayloadURI() {
		return payloadURI;
	}

	/**
	 * @param payloadURI
	 *            the payloadURI to set
	 */
	public void setPayloadURI(String payloadURI) {
		this.payloadURI = payloadURI;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the contentLength
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength
	 *            the contentLength to set
	 */
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

}
