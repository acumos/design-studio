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

package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.ce.exceptionhandler.CustomException;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.json.JSONException;

/**
 * 
 * 
 *
 */
public interface ICompositeSolutionService {

	/**
	 * 
	 * @param dscs
	 * @return
	 * @throws CustomException
	 * @throws JSONException
	 */
	public String saveCompositeSolution(DSCompositeSolution dscs) throws CustomException, JSONException;
	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @return
	 * @throws CustomException
	 * @throws JSONException
	 */
	public boolean deleteCompositeSolution(String userId, String solutionId, String version) throws CustomException, JSONException;
	/**
	 * @param userId
	 * @param solutionId
	 * @param solutionVersion
	 * @param cid
	 * @return
	 */
	public String closeCompositeSolution(String userId, String solutionId, String solutionVersion, String cid);
	/**
	 * @param userId
	 * @param solutionId
	 * @param solutionVersion
	 * @param cid
	 * @return
	 */
	public String clearCompositeSolution(String userId, String solutionId, String solutionVersion, String cid);
	/**
	 * @param userId
	 * @param visibilityLevel 
	 * @return List of active Public Composite Solutions
	 */
	public String getCompositeSolutions(String userId, String visibilityLevel ) throws CustomException;
	
	/**
	 * @param userId
	 * @param version
	 * @param solutionId
	 * @param cid
	 * @return
	 */
	public String validateCompositeSolution(String userId, String solutionName, String solutionId, String version);
}
