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

package org.acumos.designstudio.toscagenerator.util;

public class ConfigurationProperties {

	private static ConfigurationProperties configurationProperties;

	private String toscaOutputFolder;
	private String toscaGeneratorEndPointURL;
	private String nexusEndPointURL;
	private String nexusUserName;
	private String nexusPassword;
	private String nexusGroupId;
	private String cmnDataSvcEndPoinURL;
	private String cmnDataSvcUser;
	private String cmnDataSvcPwd;

	/**
	 * 
	 * @param toscaOutputFolder
	 * @param toscaGeneratorEndPointURL
	 * @param nexusEndPointURL
	 * @param nexusUserName
	 * @param nexusPassword
	 * @param nexusGroupId
	 * @param cmnDataSvcEndPoinURL
	 * @param cmnDataSvcUser
	 * @param cmnDataSvcPwd
	 */
	private ConfigurationProperties(String toscaOutputFolder, String toscaGeneratorEndPointURL, String nexusEndPointURL,
			String nexusUserName, String nexusPassword, String nexusGroupId, String cmnDataSvcEndPoinURL,
			String cmnDataSvcUser, String cmnDataSvcPwd) {
		super();
		this.toscaOutputFolder = toscaOutputFolder;
		this.toscaGeneratorEndPointURL = toscaGeneratorEndPointURL;
		this.nexusEndPointURL = nexusEndPointURL;
		this.nexusUserName = nexusUserName;
		this.nexusPassword = nexusPassword;
		this.nexusGroupId = nexusGroupId;
		this.cmnDataSvcEndPoinURL = cmnDataSvcEndPoinURL;
		this.cmnDataSvcUser = cmnDataSvcUser;
		this.cmnDataSvcPwd = cmnDataSvcPwd;
	}

	public ConfigurationProperties() {

	}

	/**
	 * 
	 * @param toscaOutputFolder
	 *            Output folder
	 * @param toscaGeneratorEndPointURL
	 *            Generator URL
	 * @param nexusEndPointURL
	 *            Nexus URL
	 * @param nexusUserName
	 *            Nexus user name
	 * @param nexusPassword
	 *            Nexus password
	 * @param nexusGroupId
	 *            Nexus group ID
	 * @param cmnDataSvcEndPoinURL
	 *            Data service URL
	 * @param cmnDataSvcUser
	 *            Data service user name
	 * @param cmnDataSvcPwd
	 *            Data service password
	 */
	public static void init(String toscaOutputFolder, String toscaGeneratorEndPointURL, String nexusEndPointURL,
			String nexusUserName, String nexusPassword, String nexusGroupId, String cmnDataSvcEndPoinURL,
			String cmnDataSvcUser, String cmnDataSvcPwd) {
		if (configurationProperties == null) {
			configurationProperties = new ConfigurationProperties(toscaOutputFolder, toscaGeneratorEndPointURL,
					nexusEndPointURL, nexusUserName, nexusPassword, nexusGroupId, cmnDataSvcEndPoinURL, cmnDataSvcUser,
					cmnDataSvcPwd);
		}
	}

	/**
	 * 
	 * @return Static instance of ConfigurationProperties
	 */
	public static ConfigurationProperties getConfigurationProperties() {
		return configurationProperties;
	}

	/**
	 * @return the toscaOutputFolder
	 */
	public String getToscaOutputFolder() {
		return toscaOutputFolder;
	}

	/**
	 * @return the toscaGeneratorEndPointURL
	 */
	public String getToscaGeneratorEndPointURL() {
		return toscaGeneratorEndPointURL;
	}

	/**
	 * @return the nexusEndPointURL
	 */
	public String getNexusEndPointURL() {
		return nexusEndPointURL;
	}

	/**
	 * @return the nexusUserName
	 */
	public String getNexusUserName() {
		return nexusUserName;
	}

	/**
	 * @return the nexusPassword
	 */
	public String getNexusPassword() {
		return nexusPassword;
	}

	/**
	 * @return the nexusGroupId
	 */
	public String getNexusGroupId() {
		return nexusGroupId;
	}

	/**
	 * @return the cmnDataSvcEndPoinURL
	 */
	public String getCmnDataSvcEndPoinURL() {
		return cmnDataSvcEndPoinURL;
	}

	/**
	 * @return the cmnDataSvcUser
	 */
	public String getCmnDataSvcUser() {
		return cmnDataSvcUser;
	}

	/**
	 * @return the cmnDataSvcPwd
	 */
	public String getCmnDataSvcPwd() {
		return cmnDataSvcPwd;
	}

}
