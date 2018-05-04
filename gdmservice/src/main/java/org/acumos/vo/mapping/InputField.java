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

package org.acumos.vo.mapping;

import java.io.Serializable;

public class InputField implements Serializable{
	
	private static final long serialVersionUID = -7735575155080257503L;
	private String tag;
	private String role;
	private String name;
	private String type;
	private String mapped_to_message;
	private String mapped_to_field;
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the mapped_to_message
	 */
	public String getMapped_to_message() {
		return mapped_to_message;
	}
	/**
	 * @param mapped_to_message the mapped_to_message to set
	 */
	public void setMapped_to_message(String mapped_to_message) {
		this.mapped_to_message = mapped_to_message;
	}
	/**
	 * @return the mapped_to_field
	 */
	public String getMapped_to_field() {
		return mapped_to_field;
	}
	/**
	 * @param mapped_to_field the mapped_to_field to set
	 */
	public void setMapped_to_field(String mapped_to_field) {
		this.mapped_to_field = mapped_to_field;
	}
	
	
}
