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

import java.util.LinkedHashMap;
import java.util.List;

import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * This class is responsible for holding the solutions list.
 * as well revisions list and Artifact list details temporary
 * in CACHE or temporary variable to improve the performance.
 * of Matching Model API call
 */
@SessionScope
@Component
public class MatchingModelServiceComponent {
	private List<MLPSolution> matchingModelsolutionList  = null;
	private LinkedHashMap<String, List<MLPSolutionRevision>> matchingModelRevisionList = null;
	private LinkedHashMap<String, List<MLPArtifact>> matchingModelArtifactTigifFileList = null;
	/**
	 * @return the matchingModelsolutionList
	 */
	public List<MLPSolution> getMatchingModelsolutionList() {
		return matchingModelsolutionList;
	}
	/**
	 * @param matchingModelsolutionList the matchingModelsolutionList to set
	 */
	public void setMatchingModelsolutionList(List<MLPSolution> matchingModelsolutionList) {
		this.matchingModelsolutionList = matchingModelsolutionList;
	}
	/**
	 * @return the matchingModelRevisionList
	 */
	public LinkedHashMap<String, List<MLPSolutionRevision>> getMatchingModelRevisionList() {
		return matchingModelRevisionList;
	}
	/**
	 * @param matchingModelRevisionList the matchingModelRevisionList to set
	 */
	public void setMatchingModelRevisionList(LinkedHashMap<String, List<MLPSolutionRevision>> matchingModelRevisionList) {
		this.matchingModelRevisionList = matchingModelRevisionList;
	}
	/**
	 * @return the matchingModelArtifactTigifFileList
	 */
	public LinkedHashMap<String, List<MLPArtifact>> getMatchingModelArtifactTigifFileList() {
		return matchingModelArtifactTigifFileList;
	}
	/**
	 * @param matchingModelArtifactTigifFileList the matchingModelArtifactTigifFileList to set
	 */
	public void setMatchingModelArtifactTigifFileList(
			LinkedHashMap<String, List<MLPArtifact>> matchingModelArtifactTigifFileList) {
		this.matchingModelArtifactTigifFileList = matchingModelArtifactTigifFileList;
	}
	
}
