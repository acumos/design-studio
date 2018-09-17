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

package org.acumos.sqldatabroker.service;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.Protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public interface ProtobufService {
	
	/**
	 * This method process the protobuf and sets the details.
	 * @param conf
	 * 		This method accepts Configuration as an parameter
	 * @throws ServiceException
	 * 		This method throws ServiceException in case of any Exception.
	 */
	public void processProtobuf(Configuration conf) throws ServiceException;
	
	/**
	 * This method returns the Protobuf Class set from the configuration details using API /configDB.
	 * @return Protobuf
	 * 		This method return the instance of Protobuf for the configured protobuf file. 
	 * @throws ServiceException
	 * 		This method throws ServiceException in case of any Exception.
	 */
	public Protobuf getProtobuf() throws ServiceException;

	/**
	 * This method converts the ASCII data (input line) into protobuf binary format.
	 * @param line
	 * 		This method accepts the data as String.
	 * @return byte[]
	 * 		This method returns byte[] as an output
	 * @throws CloneNotSupportedException
	 * 		This method throws CloneNotSupportedException 
	 */
	public byte[] convertToProtobufFormat(String line) throws CloneNotSupportedException;

	/**
	 * This method converts the ASCII data (input line) into protobuf binary format for the specified message name.
	 * @param messageName
	 * 		This method accepts message name
	 * @param line
	 * 		This method accepts data as String
	 * @return
	 * 		This method return protobuf formatted binary data. 
	 * 
	 * @throws CloneNotSupportedException
	 * 		This method throws CloneNotSupportedException 
	 * @throws InvalidProtocolBufferException
	 * 		This method throws InvalidProtocolBufferException 
	 */
	public byte[] convertToProtobufFormat(String messageName, String line) throws CloneNotSupportedException, InvalidProtocolBufferException;
	
	/**
	 * This method reads the protobuf binary formatted data (i.e., line) into specified message.
	 * @param messageName
	 * 		This method accepts message Name
	 * @param line
	 * 		This method accepts data as byte array
	 * @return
	 * 		This method return the ASCII formatted output 
	 * 
	 * @throws InvalidProtocolBufferException
	 * 		This method throws InvalidProtocolBufferException 
	 */
	public String readProtobufFormat(String messageName, byte[] line) throws InvalidProtocolBufferException;
	
	

}
