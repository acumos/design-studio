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


package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.compositeproto.Protobuf;


public interface ICompositeSolutionProtoFileGeneratorService {
	
	
	/**
	 * This method get the file from Nexus, return the content for the specified artifact.
	 * 
	 * @param solutionId
	 * 	solutionId of the artifact
	 * @param version
	 * 	solution version
	 * @param artifactType
	 * 	solution artifact type for which UIR need to be retrieved
	 * @param fileExtension
	 * 	The artifact type Nexus file extension
	 * @return string
     *	returns the Nexus proto URI
	 * @throws ServiceException
	 * 	In case of any error throws ServiceException
	 */
	public String getPayload(String solutionId, String version, String artifactType, String fileExtension)throws ServiceException;

	
	/**
	 * This method parse the data and returns the Protobuf
	 * 
	 * @param protoData
	 *    .proto file content
	 *
	 * @return Protobuf 
	 *    Converts the input and returns the instance of Protobuf class
	 *
	 */
	public Protobuf parseProtobuf(String protoData);
}
