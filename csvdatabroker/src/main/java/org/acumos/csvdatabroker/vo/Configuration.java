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

package org.acumos.csvdatabroker.vo;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;

public class Configuration implements Serializable, Cloneable {

	private static final long serialVersionUID = -1221791434230094251L;
	private String userName;
	private String password;
	private String host;
	private Integer port;
	private DataBrokerMap data_broker_map;
	private String protobufFile;
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	
	/**
	 * @return the data_broker_map
	 */
	public DataBrokerMap getData_broker_map() {
		return data_broker_map;
	}
	/**
	 * @param data_broker_map the data_broker_map to set
	 */
	public void setData_broker_map(DataBrokerMap data_broker_map) {
		this.data_broker_map = data_broker_map;
	}
	/**
	 * @return the protobufFile
	 */
	public String getProtobufFile() {
		return protobufFile;
	}
	/**
	 * @param protobufFile the protobufFile to set
	 */
	public void setProtobufFile(String protobufFile) {
		this.protobufFile = protobufFile;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	public String getRemoteDir() throws ServiceException {
		String remoteDir = null;
		String remotePath = null;
		try {
			URI uri = new URI(data_broker_map.getTarget_system_url());
			remotePath = uri.getPath();
			
			if(remotePath.endsWith("/")){ //Target System URL may end with dir/
				remoteDir = remotePath;
			} else if(remotePath.indexOf(".") > 0) { //Target System remote path can end with file.ext
				remoteDir = remotePath.substring(0, remotePath.lastIndexOf("/")+1);
			} else { //Target System URL may end with dir but with out end /
				remoteDir = remotePath + "/";
			}
		} catch (URISyntaxException e) {
			throw new ServiceException("  Exception in getRemoteDir() ","401", "Invalid Target system URL ");
		}
		return remoteDir;
	}
	
}
