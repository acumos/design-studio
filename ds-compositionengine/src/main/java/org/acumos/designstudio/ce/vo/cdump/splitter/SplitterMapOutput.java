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


package org.acumos.designstudio.ce.vo.cdump.splitter;

import java.io.Serializable;

public class SplitterMapOutput implements Serializable{

	private static final long serialVersionUID = -142963644521061377L;
	
	private SplitterOutputField output_field;

	public SplitterOutputField getOutput_field() {
		return output_field;
	}

	public void setOutput_field(SplitterOutputField output_field) {
		this.output_field = output_field;
	}
	

}
