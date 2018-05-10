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

package org.acumos.converter;

public enum ConversionEnum {

	BOOLTOINT32("booleantoint"),
	BOOLTOBOOL("booleantoboolean"),
	BOOLTOSTRING("tostring"),
	BOOLTOFLOAT("booleantofloat"),
	
	FLOATTOBOOL("floattoboolean"),
	FLOATTOINT32("floattoint"),
	FLOATTOINT64("floattolong"),
	FLOATTOFLOAT("floattofloat"),
	FOATTOSTRING("tostring"),
	
	INT32TOBOOL("inttoboolean"),
	INT32TOFLOAT("inttofloat"),
	INT32TOINT64("inttolong"),
	INT32TOINT32("inttoint"),
	INT32TOSTRING("tostring"),
	
	STRINGTOFLOAT("stringtofloat"),
	STRINGTOINT32("stringtoint"),
	STRINGTOINT64("stringtolong"),
	STRINGTOSTRING("tostring"),
	
	
	
	TOSTRING("tostring");
	
	private final String type;
	private ConversionEnum(String type){
		this.type = type;
	}
	
	public boolean equalsName(String type) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return type.equals(type);
    }

    public String toString() {
       return this.type;
    }
    
}
