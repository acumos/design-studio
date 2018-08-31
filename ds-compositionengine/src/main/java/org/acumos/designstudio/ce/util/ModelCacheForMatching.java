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


package org.acumos.designstudio.ce.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acumos.designstudio.ce.vo.matchingmodel.KeyVO;
import org.acumos.designstudio.ce.vo.matchingmodel.ModelDetailVO;
import org.springframework.stereotype.Component;

@Component
public class ModelCacheForMatching implements Serializable {

	private static final long serialVersionUID = -5325762093658637128L;
	
	private Map<KeyVO, List<ModelDetailVO>> publicModelCache;
	private Map<String, Map<KeyVO, List<ModelDetailVO>>> userPrivateModelCache;
	private Map<String, Date> userPrivateModelUpdateTime;
	
	public Map<KeyVO, List<ModelDetailVO>> getPublicModelCache() {
		return publicModelCache;
	}
	public void setPublicModelCache(Map<KeyVO, List<ModelDetailVO>> publicModelCache) {
		this.publicModelCache = publicModelCache;
	}
	
	/**
	 * This method will get PrivateModelCache 
	 * @param userId
	 * 		This method accepts userId as parameter
	 * @return
	 * 		This method returns userPrivateModelCache, if not returns null 
	 */
	public Map<KeyVO, List<ModelDetailVO>> getPrivateModelCache(String userId) {
		if (null != userPrivateModelCache) {
			if (userPrivateModelCache.containsKey(userId)) {
				return userPrivateModelCache.get(userId);
			} else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	/**
	 * This method removes the user private model cache from the userPrivateModelCache. 
	 * @param userId
	 * 		This method accepts userId as parameter
	 */
	public void removeUserPrivateModelCache(String userId){
		if(null != userPrivateModelCache){
			if(userPrivateModelCache.containsKey(userId)){
				userPrivateModelCache.remove(userId);
			}
		}
	}
	
	/**
	 * This method will set UserPrivateModelCache
	 * @param userId
	 * 		This method accepts userId as parameter
	 * @param privateModelCache
	 * 		This method accepts privateModelCache as parameter
	 */
	public void setUserPrivateModelCache(String userId, Map<KeyVO, List<ModelDetailVO>> privateModelCache) {
		if(null == userPrivateModelCache){
			this.userPrivateModelCache = new HashMap<String, Map<KeyVO, List<ModelDetailVO>>>();
		}
		userPrivateModelCache.put(userId, privateModelCache);
	}
	
	/**
	 * This method will get UserPrivateModelUpdateTime
	 * @param userId
	 * 		This method accepts userId as parameter
	 * @return
	 * 		This method returns userPrivateModelUpdateTime
	 */
	public Date getUserPrivateModelUpdateTime(String userId) {
		if (null != userPrivateModelUpdateTime) {
			if (userPrivateModelUpdateTime.containsKey(userId)) {
				return userPrivateModelUpdateTime.get(userId);
			} else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	/**
	 * This method will set UserPrivateModelUpdateTime
	 * @param userId
	 * 		This method accepts userId as parameter
	 * @param executionTime
	 * 		This method accepts executionTime as parameter to set the userPrivateModelUpdateTime
	 */
	public void setUserPrivateModelUpdateTime(String userId, Date executionTime) {
		if(null == userPrivateModelUpdateTime){
			this.userPrivateModelUpdateTime = new HashMap<String, Date>();
		}
		userPrivateModelUpdateTime.put(userId, executionTime);
	}
	
	
}
