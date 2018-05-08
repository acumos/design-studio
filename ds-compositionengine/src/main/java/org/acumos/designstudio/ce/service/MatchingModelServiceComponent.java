package org.acumos.designstudio.ce.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

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
