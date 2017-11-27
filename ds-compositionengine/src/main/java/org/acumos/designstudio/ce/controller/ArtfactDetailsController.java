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

import org.acumos.designstudio.ce.service.IAcumosCatalog;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/dsce/artifact/")
public class ArtfactDetailsController {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ArtfactDetailsController.class);
	@Autowired
	private IAcumosCatalog iacumosCatalog;
	@Autowired
	Properties props;

	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @return
	 */

	@ApiOperation(value = "Gets TOSCA details for specified solutionId and version")
	@RequestMapping(value = "/fetchJsonTOSCA", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String fetchJsonTOSCA(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "solutionId", required = true) String solutionId,
			@RequestParam(value = "version", required = true) String version) {
		logger.debug(EELFLoggerDelegator.debugLogger, "------- fetchJsonTOSCA() ------- : Begin");
		String result = "";
		try {
			result = iacumosCatalog.readArtifact(userId, solutionId, version, props.getArtifactType().trim());

			if (result == null || result.isEmpty()) {
				result = "Failed to fetch the TOSCA details for specified solutionId and version";
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception in fetchJsonTOSCA() -------",e);
			result = e.getMessage();
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------- fetchJsonTOSCA() ------- : End");
		return result;
	}
	

	/**
	 * 
	 * @param userId
	 * @param solutionId
	 * @param version
	 * @return
	 */
	@ApiOperation(value = "Get the profobuf file details for specified solutionID and version")
	@RequestMapping(value = "/fetchProtoBufJSON", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String fetchProtoBufJSON(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "solutionId", required = true) String solutionId,
			@RequestParam(value = "version", required = true) String version) {
		logger.debug(EELFLoggerDelegator.debugLogger,
				"--------ArtfactDetailsController : fetchProtoBufJSON()-------- : Begin");

		String resultTemplate = "{\"protobuf_json\" : %s,\n \"success\" : \"%s\",\n \"errorMessage\" : \"%s\"}";
		String result = "";
		try {
			result = iacumosCatalog.readArtifact(userId, solutionId, version, props.getProtoArtifactType().trim());

			if (result != null && !result.isEmpty()) {
				resultTemplate = String.format(resultTemplate, result, true, "");
			} else {
				resultTemplate = String.format(resultTemplate, result, false, "Unable to read protoBufFile");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception in fetchProtoBufJSON() -------",e);
			resultTemplate = String.format(resultTemplate, null, false, e.getMessage());
		}
		logger.debug(EELFLoggerDelegator.debugLogger,
				"--------ArtfactDetailsController : fetchProtoBufJSON()-------- : End");
		return resultTemplate;
	}

}
