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
public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String container_name;
	private String image;
	private List<Depends_On> depends_on;

	
	/**
	 * @return the container_name
	 */
	public String getContainer_name() {
		return container_name;
	}

	/**
	 * @param container_name the container_name to set
	 */
	public void setContainer_name(String container_name) {
		this.container_name = container_name;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the depends_on
	 */
	public List<Depends_On> getDepends_on() {
		return depends_on;
	}

	/**
	 * @param depends_on the depends_on to set
	 */
	public void setDepends_on(List<Depends_On> depends_on) {
		this.depends_on = depends_on;
	}
	


}
