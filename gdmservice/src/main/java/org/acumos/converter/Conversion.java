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

public class Conversion {
	
	
	private static Conversion conversion = null;
	
	private Conversion(){
		super();
	}
	
	public static Conversion getInstance(){
		if(null == conversion){
			conversion = new Conversion();
		}
		return conversion;
	}
	
	public Object executeConversion(Object in, ConversionEnum conversion) throws ConversionException{
		Object result = null;
		Converter converter = null;
		try{
			converter = ConverterEnum.valueOf(conversion.toString()).getConverter();
			result = converter.convert(in);
		} 
		catch(ConversionException e ){
			throw e;
		} 
		
		return result;
	}
}
