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
	 * @return the lib
	 */
	public String getLib() {
		return lib;
	}

	@Value("${lib}")
	private String lib;
	
	

	/**
	 * @return the toscaOutputFolder
	 */
	public String getToscaOutputFolder() {
		return toscaOutputFolder;
	}

	/**
	 * @return the nexusendpointurl
	 */
	public String getNexusendpointurl() {
		return nexusendpointurl;
	}

	/**
	 * @return the nexususername
	 */
	public String getNexususername() {
		return nexususername;
	}

	/**
	 * @return the nexuspassword
	 */
	public String getNexuspassword() {
		return nexuspassword;
	}

	/**
	 * @return the nexusgroupid
	 */
	public String getNexusgroupid() {
		return nexusgroupid;
	}

	/**
	 * @return the cmndatasvcendpoinurl
	 */
	public String getCmndatasvcendpoinurl() {
		return cmndatasvcendpoinurl;
	}

	/**
	 * @return the cmndatasvcuser
	 */
	public String getCmndatasvcuser() {
		return cmndatasvcuser;
	}

	/**
	 * @return the cmndatasvcpwd
	 */
	public String getCmndatasvcpwd() {
		return cmndatasvcpwd;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	
	

	
	
	
}
