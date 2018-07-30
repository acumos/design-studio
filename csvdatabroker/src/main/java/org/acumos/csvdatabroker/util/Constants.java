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

package org.acumos.csvdatabroker.util;

public interface Constants {
	public static final String PACKAGEPATH="org/acumos/vo";
	public static final String CLASSNAME="DataVO";
	public static final String PACKAGENAME="org.acumos.vo";
	public static final String PROTOBUF_FILENAME="default";
	public static final String PROTOBUF_FILE_PATH="classpath:default.proto";
	public static final String PROTOBUF_DATA_TYPE="double,float,int32,int64,unit32,unit64,sint32,sint64,fixed32,fixed64,sfixed32,sfixed64,bool,string,bytes";
	public static final String BEGIN_PARENTHESIS="[{(\"";
	public static final String END_PARENTHESIS = "]})\"";
	public static final String FIRST_ROW_CONTAINS_FIELDNAMES = "contains_field_names";
	
}