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

package org.acumos.designstudio.ce.vo.cdump;

import java.io.Serializable;

import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMap;
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.FieldMap;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMap;

public class DataConnector implements Serializable {
	
	private static final long serialVersionUID = 3121933663617437481L;
	
	private FieldMap fieldMap;
	private DataBrokerMap databrokerMap;
	private CollatorMap collatorMap;
	private SplitterMap splitterMap;
	
	
	/**
	 * @return fieldMap
	 */
	public FieldMap getFieldMap() {
		return fieldMap;
	}
	
	public void setFieldMap(FieldMap fieldMap) {
		this.fieldMap = fieldMap;
	}
	
	/**
	 * @return databrokerMap
	 */
	public DataBrokerMap getDatabrokerMap() {
		return databrokerMap;
	}
	public void setDatabrokerMap(DataBrokerMap databrokerMap) {
		this.databrokerMap = databrokerMap;
	}

	public CollatorMap getCollatorMap() {
		return collatorMap;
	}

	public void setCollatorMap(CollatorMap collatorMap) {
		this.collatorMap = collatorMap;
	}

	public SplitterMap getSplitterMap() {
		return splitterMap;
	}

	public void setSplitterMap(SplitterMap splitterMap) {
		this.splitterMap = splitterMap;
	}


}
