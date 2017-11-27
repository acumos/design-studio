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

/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;
import java.io.Serializable;
import java.util.List;

/**
 * 
 *
 */
public class BluePrint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String version;
	private List<InputOperationSignatures> input_operation_signatures;
	private List<Node> nodes;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return the input_operation_signatures
	 */
	public List<InputOperationSignatures> getInput_operation_signatures() {
		return input_operation_signatures;
	}
	/**
	 * @param input_operation_signatures the input_operation_signatures to set
	 */
	public void setInput_operation_signatures(List<InputOperationSignatures> input_operation_signatures) {
		this.input_operation_signatures = input_operation_signatures;
	}
	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}
	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

}
