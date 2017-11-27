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

package org.acumos.designstudio.ce.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 *
 */
@Component
public class ConfigurationProperties {
	
	@Value("${tosca.outputfolder}")
	private String toscaOutputFolder;
	
	@Value("${nexus.nexusendpointurl}")
	private String nexusendpointurl;

	@Value("${nexus.nexususername}")
	private String nexususername;

	@Value("${nexus.nexuspassword}")
	private String nexuspassword;

	@Value("${nexus.nexusgroupid}")
	private String nexusgroupid;

	@Value("${cmndatasvc.cmndatasvcendpoinurl}")
	private String cmndatasvcendpoinurl;

	@Value("${cmndatasvc.cmndatasvcuser}")
	private String cmndatasvcuser;

	@Value("${cmndatasvc.cmndatasvcpwd}")
	private String cmndatasvcpwd;

	@Value("${dateformat}")
	private String dateFormat;

	/**
	 * @return the toscaOutputFolder
	 */
	public String getToscaOutputFolder() {
		return toscaOutputFolder;
	}

	/**
	 * @param toscaOutputFolder the toscaOutputFolder to set
	 */
	public void setToscaOutputFolder(String toscaOutputFolder) {
		this.toscaOutputFolder = toscaOutputFolder;
	}

	/**
	 * @return the nexusendpointurl
	 */
	public String getNexusendpointurl() {
		return nexusendpointurl;
	}

	/**
	 * @param nexusendpointurl the nexusendpointurl to set
	 */
	public void setNexusendpointurl(String nexusendpointurl) {
		this.nexusendpointurl = nexusendpointurl;
	}

	/**
	 * @return the nexususername
	 */
	public String getNexususername() {
		return nexususername;
	}

	/**
	 * @param nexususername the nexususername to set
	 */
	public void setNexususername(String nexususername) {
		this.nexususername = nexususername;
	}

	/**
	 * @return the nexuspassword
	 */
	public String getNexuspassword() {
		return nexuspassword;
	}

	/**
	 * @param nexuspassword the nexuspassword to set
	 */
	public void setNexuspassword(String nexuspassword) {
		this.nexuspassword = nexuspassword;
	}

	/**
	 * @return the nexusgroupid
	 */
	public String getNexusgroupid() {
		return nexusgroupid;
	}

	/**
	 * @param nexusgroupid the nexusgroupid to set
	 */
	public void setNexusgroupid(String nexusgroupid) {
		this.nexusgroupid = nexusgroupid;
	}

	/**
	 * @return the cmndatasvcendpoinurl
	 */
	public String getCmndatasvcendpoinurl() {
		return cmndatasvcendpoinurl;
	}

	/**
	 * @param cmndatasvcendpoinurl the cmndatasvcendpoinurl to set
	 */
	public void setCmndatasvcendpoinurl(String cmndatasvcendpoinurl) {
		this.cmndatasvcendpoinurl = cmndatasvcendpoinurl;
	}

	/**
	 * @return the cmndatasvcuser
	 */
	public String getCmndatasvcuser() {
		return cmndatasvcuser;
	}

	/**
	 * @param cmndatasvcuser the cmndatasvcuser to set
	 */
	public void setCmndatasvcuser(String cmndatasvcuser) {
		this.cmndatasvcuser = cmndatasvcuser;
	}

	/**
	 * @return the cmndatasvcpwd
	 */
	public String getCmndatasvcpwd() {
		return cmndatasvcpwd;
	}

	/**
	 * @param cmndatasvcpwd the cmndatasvcpwd to set
	 */
	public void setCmndatasvcpwd(String cmndatasvcpwd) {
		this.cmndatasvcpwd = cmndatasvcpwd;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	
	
}
