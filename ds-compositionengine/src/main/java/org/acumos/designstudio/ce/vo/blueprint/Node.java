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

package org.acumos.designstudio.ce.vo.blueprint;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {

	private static final long serialVersionUID = -6799798022353553155L;
	
	private String container_name;
	private String node_type;
	private String image;
	private String proto_uri;
	private List<OperationSignatureList> operation_signature_list;
	private BPDataBrokerMap data_broker_map;
	private BPSplitterMap splitter_map;
	private BPCollatorMap collator_map;
	private List<DataSource> data_sources;

	public String getContainer_name() {
		return container_name;
	}
	public void setContainer_name(String container_name) {
		this.container_name = container_name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getNode_type() {
		return node_type;
	}
	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
	public String getProto_uri() {
		return proto_uri;
	}
	public void setProto_uri(String proto_uri) {
		this.proto_uri = proto_uri;
	}
	public List<OperationSignatureList> getOperation_signature_list() {
		return operation_signature_list;
	}
	public void setOperation_signature_list(List<OperationSignatureList> operation_signature_list) {
		this.operation_signature_list = operation_signature_list;
	}
	public List<DataSource> getData_sources() {
		return data_sources;
	}
	public void setData_sources(List<DataSource> data_sources) {
		this.data_sources = data_sources;
	}
	public BPDataBrokerMap getData_broker_map() {
		return data_broker_map;
	}
	public void setData_broker_map(BPDataBrokerMap data_broker_map) {
		this.data_broker_map = data_broker_map;
	}
	public BPSplitterMap getSplitter_map() {
		return splitter_map;
	}
	public void setSplitter_map(BPSplitterMap splitter_map) {
		this.splitter_map = splitter_map;
	}
	public BPCollatorMap getCollator_map() {
		return collator_map;
	}
	public void setCollator_map(BPCollatorMap collator_map) {
		this.collator_map = collator_map;
	}
	

}
