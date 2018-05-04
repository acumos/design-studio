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

public enum ConverterEnum {
	
	tostring(new ToString()),
	
	inttofloat(new IntToFloat()),
	inttoboolean(new IntToBoolean()),
	inttolong(new IntToLong()),
	inttoint(new IntToInt()),
	
	floattoboolean(new FloatToBoolean()),
	floattoint(new FloatToInt()),
	floattolong(new FloatToLong()),
	floattofloat(new FloatToFloat()),
	
	stringtofloat(new StringToFloat()),
	stringtoint(new StringToInt()),
	stringtolong(new StringToLong()),
	
	
	booleantoint(new BooleanToInt()),
	booleantoboolean(new BooleanToBoolean()),
	booleantofloat(new BooleanToFloat());
	
	
	
	
	private final String converterName;
	private final Converter converter;
	
		
	private ConverterEnum(Converter converter){
		this.converter = converter;
		this.converterName = converter.getClass().getName();
	}
	
	public boolean equalsName(String name) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return converterName.equals(name);
    }

    public String toString() {
       return this.converterName;
    }
    
    public Converter getConverter(){
    	return this.converter;
    }
}
