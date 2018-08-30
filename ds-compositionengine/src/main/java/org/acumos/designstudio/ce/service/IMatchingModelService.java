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

import java.util.List;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;

public interface IMatchingModelService {
	/**
	 * This method will populate PublicModelCache For MatchingModels
	 * @param models
	 * 		models
	 * @throws ServiceException
	 * 		In Exception Case
	 */
	public void populatePublicModelCacheForMatching(List<DSModelVO> models) throws ServiceException;
	/**
	 * This method will remove PublicModelCache For MatchingModels
	 * @param models
	 * 		models
	 * @throws ServiceException
	 * 		In Exception Case
	 */
	public void removePublicModelCacheForMatching(List<DSModelVO> models) throws ServiceException;
	/**
	 * This method will get the Public DSModels 
	 * @return
	 * 		list of DSModelVo
	 * @throws ServiceException
	 * 		In Exception Case
	 */
	public List<DSModelVO> getPublicDSModels() throws ServiceException;
	/**
	 * This method will get the Private DSModels 
	 * @param userId
	 * 		UserId
	 * @return
	 * 		list of DSModelVo
	 * @throws ServiceException
	 * 		In Exception Case
	 */
	public List<DSModelVO> getPrivateDSModels(String userId) throws ServiceException;
	/**
	 * This method will populate PrivateModelCache For MatchingModels 
	 * @param userId
	 * 		UserId
	 * @param models
	 * 		models
	 * @throws ServiceException
	 * 		In Exception Case	
	 */
	public void populatePrivateModelCacheForMatching(String userId, List<DSModelVO> models) throws ServiceException;
}
