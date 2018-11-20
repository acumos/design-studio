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

package org.acumos.designstudio.ce.controller;

import javax.servlet.http.HttpServletResponse;

import org.acumos.designstudio.ce.Application;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/dsce/admin/")
public class AdminController {
	
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(AdminController.class);
	
	@ApiOperation(value = "Gets ds-compositionengine version")
	@RequestMapping(value = "/getVersion", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String getVersion(HttpServletResponse response) {
		String version; 
		try {
			String className = this.getClass().getSimpleName() + ".class";
	        String classPath = this.getClass().getResource(className).toString();
	        version = classPath.startsWith("jar") ? Application.class.getPackage().getImplementationVersion().split("-b")[0]:"1.40.3";
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getVersion() ", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			version = "1.40.3";
		}
		
		return version;
	}
	
}
