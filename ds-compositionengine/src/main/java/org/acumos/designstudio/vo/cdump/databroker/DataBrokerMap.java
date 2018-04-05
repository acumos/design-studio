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

package org.acumos.designstudio.vo.cdump.databroker;

import java.io.Serializable;

public class DataBrokerMap implements Serializable{

	private static final long serialVersionUID = -4853419224669616315L;
	
	private String script;
	private String target_system_url;
	private String local_system_data_file_path;
	private String first_row;
	private String csv_file_field_separator;
	private String data_broker_type;
	private DBMapInput[] map_inputs;
	private DBMapOutput[] map_outputs;
	private String map_action;
	
	
	
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
	/**
	 * @return the map_action
	 */
	public String getMap_action() {
		return map_action;
	}
	/**
	 * @param map_action the map_action to set
	 */
	public void setMap_action(String map_action) {
		this.map_action = map_action;
	}
	
	
}
