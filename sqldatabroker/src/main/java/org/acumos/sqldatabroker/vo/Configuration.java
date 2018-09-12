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

package org.acumos.sqldatabroker.vo;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Configuration implements Serializable, Cloneable {

	private static final long serialVersionUID = -1221791434230094251L;
	
	
	private String script;
	private String target_system_url;
	private String local_system_data_file_path;
	private String first_row;
	private String csv_file_field_separator;
	private String data_broker_type;
	private DBMapInput[] map_inputs;
	private DBMapOutput[] map_outputs;
	
	private String userId; //user_id 
	private String password; //password
	private String protobufFile; //protobufFile
	
	//private String map_action;
	private String databaseName; //database_name
	private String tableName; //table_name
	private String dataSourceClassName; //jdbc_driver_data_source_class_name;
	
		
	/**
	 * @return the userId
	 */
	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	@JsonSetter("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
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

	/**
	 * @return the databaseName
	 */
	@JsonProperty("database_name")
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * @param databaseName the databaseName to set
	 */
	@JsonSetter("database_name")
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	/**
	 * @return the tableName
	 */
	@JsonProperty("table_name")
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * @param tableName the tableName to set
	 */
	@JsonSetter("table_name")
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * @return the dataSourceClassName
	 */
	@JsonProperty("jdbc_driver_data_source_class_name")
	public String getDataSourceClassName() {
		return dataSourceClassName;
	}
	
	/**
	 * @param dataSourceClassName the dataSourceClassName to set
	 */
	@JsonSetter("jdbc_driver_data_source_class_name")
	public void setDataSourceClassName(String dataSourceClassName) {
		this.dataSourceClassName = dataSourceClassName;
	}
	
	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}
	
	/**
	 * @return the target_system_url
	 */
	public String getTarget_system_url() {
		return target_system_url;
	}
	
	/**
	 * @param target_system_url the target_system_url to set
	 */
	public void setTarget_system_url(String target_system_url) {
		this.target_system_url = target_system_url;
	}
	
	/**
	 * @return the local_system_data_file_path
	 */
	public String getLocal_system_data_file_path() {
		return local_system_data_file_path;
	}
	
	/**
	 * @param local_system_data_file_path the local_system_data_file_path to set
	 */
	public void setLocal_system_data_file_path(String local_system_data_file_path) {
		this.local_system_data_file_path = local_system_data_file_path;
	}
	
	/**
	 * @return the first_row
	 */
	public String getFirst_row() {
		return first_row;
	}
	
	/**
	 * @param first_row the first_row to set
	 */
	public void setFirst_row(String first_row) {
		this.first_row = first_row;
	}
	
	/**
	 * @return the csv_file_field_separator
	 */
	public String getCsv_file_field_separator() {
		return csv_file_field_separator;
	}
	
	/**
	 * @param csv_file_field_separator the csv_file_field_separator to set
	 */
	public void setCsv_file_field_separator(String csv_file_field_separator) {
		this.csv_file_field_separator = csv_file_field_separator;
	}
	
	/**
	 * @return the data_broker_type
	 */
	public String getData_broker_type() {
		return data_broker_type;
	}
	
	/**
	 * @param data_broker_type the data_broker_type to set
	 */
	public void setData_broker_type(String data_broker_type) {
		this.data_broker_type = data_broker_type;
	}
	
	/**
	 * @return the map_inputs
	 */
	public DBMapInput[] getMap_inputs() {
		return map_inputs;
	}
	
	/**
	 * @param map_inputs the map_inputs to set
	 */
	public void setMap_inputs(DBMapInput[] map_inputs) {
		this.map_inputs = map_inputs;
	}
	
	/**
	 * @return the map_outputs
	 */
	public DBMapOutput[] getMap_outputs() {
		return map_outputs;
	}
	
	/**
	 * @param map_outputs the map_outputs to set
	 */
	public void setMap_outputs(DBMapOutput[] map_outputs) {
		this.map_outputs = map_outputs;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	public String getRemoteDir() throws ServiceException {
		String remoteDir = null;
		String remotePath = null;
		try {
			URI uri = new URI(target_system_url);
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
	
	public String getRemoteFilePath() throws ServiceException {
		String remotePath = null;
		try {
			URI uri = new URI(target_system_url);
			remotePath = uri.getPath();
		} catch (URISyntaxException e) {
			throw new ServiceException("  Exception in getRemoteDir() ","401", "Invalid Target system URL ");
		}
		return remotePath;
	}
	
	public String getLocalFilePath() throws ServiceException { 
		String localPath = null;
		try {
			localPath = target_system_url;
			
			//Path may contain "\" so replace it with "/"
			if(localPath.contains("\\")){
				localPath = localPath.replace("\\", "/");
			}
			if(localPath.startsWith("file")){
				localPath = localPath.replace("file://", "");
			}
			
		} catch(Exception e){
			throw new ServiceException(" Exceptio in getLocalPath() ", "401", "Invalid local Path");
		}
		return localPath; 
	}
	
	public String getLocalPath() throws ServiceException { 
		String localPath = null;
		try {
			localPath = target_system_url;
			
			//Path may contain "\" so replace it with "/"
			if(localPath.contains("\\")){
				localPath = localPath.replace("\\", "/");
			}
			if(localPath.indexOf(".") > 0){
				localPath = localPath.substring(0, localPath.lastIndexOf("/")+1);
			}
			if(!localPath.endsWith("/")){
				localPath = localPath+ "/";
			}
			if(localPath.startsWith("file")){
				localPath = localPath.replace("file://", "");
			}
			
		} catch(Exception e){
			throw new ServiceException(" Exceptio in getLocalPath() ", "401", "Invalid local Path");
		}
		return localPath; 
	}
	
	public boolean isRemoteFile() { 
		boolean result = false; 
		
		/*if((null != host && null != port) && ( !host.isEmpty() && port != 0)){
			result = true;
		}*/
		return result;
	}
}
