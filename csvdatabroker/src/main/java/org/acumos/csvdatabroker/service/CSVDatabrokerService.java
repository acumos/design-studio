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

package org.acumos.csvdatabroker.service;

import java.io.IOException;
import java.io.OutputStream;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;

public interface CSVDatabrokerService {
	
	/**
	 * This methods writes line by line data to the OutputStream passed as the input parameter
	 * @param out
	 *         OutputStream parameter to write output to.
	 * @throws ServiceException
	 * 			In case of any exception, this method throws the ServiceException
	 * @throws IOException
	 * 			Throws IOException while writing data.
	 */
	public void writeDataTo(OutputStream out) throws ServiceException, IOException;
	
	/**
	 * This method returns one line of output as array of byte
	 * @return byte[]
	 * 			Return the date in the from of byte[]
	 * @throws ServiceException
	 * 			In case of any exception, this method throws the ServiceException
	 */
	public byte[] getOneRecord() throws ServiceException;
}
