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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 *
 */
@Component
@PropertySource("classpath:application.properties")
public class Properties implements Serializable {
	private static final long serialVersionUID = 1L;

	@Value("${solutionErrorCode}")
	private String solutionErrorCode;

	@Value("${solutionErrorDesc}")
	private String solutionErrorDesc;
	@Value("${compositionErrorCode}")
	private String compositionErrorCode;

	@Value("${compositionErrorDescription}")
	private String compositionErrorDescription;

	@Value("${successCode}")
	private String successCode;

	@Value("${compositionSolutionErrorCode}")
	private String compositionSolutionErrorCode;

	@Value("${compositionSolutionErrorDesc}")
	private String compositionSolutionErrorDesc;

	@Value("${provider}")
	private String provider;

	@Value("${toolKit}")
	private String toolKit;

	@Value("${visibilityLevel}")
	private String visibilityLevel;

	@Value("${artifactTypeCode}")
	private String artifactTypeCode;

	@Value("${askToUpdateExistingCompSolnMsg}")
	private String askToUpdateExistingCompSolnMsg;

	@Value("${privateAccessTypeCode}")
	private String privateAccessTypeCode;

	@Value("${publicAccessTypeCode}")
	private String publicAccessTypeCode;

	@Value("${organizationAccessTypeCode}")
	private String organizationAccessTypeCode;

	@Value("${getSolutionErrorDescription}")
	private String getSolutionErrorDescription;

	@Value("${artifactType}")
	private String artifactType;

	@Value("${compositSolutionToolKitTypeCode}")
	private String compositSolutiontoolKitTypeCode;
	
	@Value("${protoArtifactType}")
	private String protoArtifactType;

	/**
	 * @return the solutionErrorCode
	 */
	public String getSolutionErrorCode() {
		return solutionErrorCode;
	}

	/**
	 * @param solutionErrorCode the solutionErrorCode to set
	 */
	public void setSolutionErrorCode(String solutionErrorCode) {
		this.solutionErrorCode = solutionErrorCode;
	}

	/**
	 * @return the solutionErrorDesc
	 */
	public String getSolutionErrorDesc() {
		return solutionErrorDesc;
	}

	/**
	 * @param solutionErrorDesc the solutionErrorDesc to set
	 */
	public void setSolutionErrorDesc(String solutionErrorDesc) {
		this.solutionErrorDesc = solutionErrorDesc;
	}

	/**
	 * @return the compositionErrorCode
	 */
	public String getCompositionErrorCode() {
		return compositionErrorCode;
	}

	/**
	 * @param compositionErrorCode the compositionErrorCode to set
	 */
	public void setCompositionErrorCode(String compositionErrorCode) {
		this.compositionErrorCode = compositionErrorCode;
	}

	/**
	 * @return the compositionErrorDescription
	 */
	public String getCompositionErrorDescription() {
		return compositionErrorDescription;
	}

	/**
	 * @param compositionErrorDescription the compositionErrorDescription to set
	 */
	public void setCompositionErrorDescription(String compositionErrorDescription) {
		this.compositionErrorDescription = compositionErrorDescription;
	}

	/**
	 * @return the successCode
	 */
	public String getSuccessCode() {
		return successCode;
	}

	/**
	 * @param successCode the successCode to set
	 */
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	/**
	 * @return the compositionSolutionErrorCode
	 */
	public String getCompositionSolutionErrorCode() {
		return compositionSolutionErrorCode;
	}

	/**
	 * @param compositionSolutionErrorCode the compositionSolutionErrorCode to set
	 */
	public void setCompositionSolutionErrorCode(String compositionSolutionErrorCode) {
		this.compositionSolutionErrorCode = compositionSolutionErrorCode;
	}

	/**
	 * @return the compositionSolutionErrorDesc
	 */
	public String getCompositionSolutionErrorDesc() {
		return compositionSolutionErrorDesc;
	}

	/**
	 * @param compositionSolutionErrorDesc the compositionSolutionErrorDesc to set
	 */
	public void setCompositionSolutionErrorDesc(String compositionSolutionErrorDesc) {
		this.compositionSolutionErrorDesc = compositionSolutionErrorDesc;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the toolKit
	 */
	public String getToolKit() {
		return toolKit;
	}

	/**
	 * @param toolKit the toolKit to set
	 */
	public void setToolKit(String toolKit) {
		this.toolKit = toolKit;
	}

	/**
	 * @return the visibilityLevel
	 */
	public String getVisibilityLevel() {
		return visibilityLevel;
	}

	/**
	 * @param visibilityLevel the visibilityLevel to set
	 */
	public void setVisibilityLevel(String visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

	/**
	 * @return the artifactTypeCode
	 */
	public String getArtifactTypeCode() {
		return artifactTypeCode;
	}

	/**
	 * @param artifactTypeCode the artifactTypeCode to set
	 */
	public void setArtifactTypeCode(String artifactTypeCode) {
		this.artifactTypeCode = artifactTypeCode;
	}

	/**
	 * @return the askToUpdateExistingCompSolnMsg
	 */
	public String getAskToUpdateExistingCompSolnMsg() {
		return askToUpdateExistingCompSolnMsg;
	}

	/**
	 * @param askToUpdateExistingCompSolnMsg the askToUpdateExistingCompSolnMsg to set
	 */
	public void setAskToUpdateExistingCompSolnMsg(String askToUpdateExistingCompSolnMsg) {
		this.askToUpdateExistingCompSolnMsg = askToUpdateExistingCompSolnMsg;
	}

	/**
	 * @return the privateAccessTypeCode
	 */
	public String getPrivateAccessTypeCode() {
		return privateAccessTypeCode;
	}

	/**
	 * @param privateAccessTypeCode the privateAccessTypeCode to set
	 */
	public void setPrivateAccessTypeCode(String privateAccessTypeCode) {
		this.privateAccessTypeCode = privateAccessTypeCode;
	}

	/**
	 * @return the publicAccessTypeCode
	 */
	public String getPublicAccessTypeCode() {
		return publicAccessTypeCode;
	}

	/**
	 * @param publicAccessTypeCode the publicAccessTypeCode to set
	 */
	public void setPublicAccessTypeCode(String publicAccessTypeCode) {
		this.publicAccessTypeCode = publicAccessTypeCode;
	}

	/**
	 * @return the organizationAccessTypeCode
	 */
	public String getOrganizationAccessTypeCode() {
		return organizationAccessTypeCode;
	}

	/**
	 * @param organizationAccessTypeCode the organizationAccessTypeCode to set
	 */
	public void setOrganizationAccessTypeCode(String organizationAccessTypeCode) {
		this.organizationAccessTypeCode = organizationAccessTypeCode;
	}

	/**
	 * @return the getSolutionErrorDescription
	 */
	public String getGetSolutionErrorDescription() {
		return getSolutionErrorDescription;
	}

	/**
	 * @param getSolutionErrorDescription the getSolutionErrorDescription to set
	 */
	public void setGetSolutionErrorDescription(String getSolutionErrorDescription) {
		this.getSolutionErrorDescription = getSolutionErrorDescription;
	}

	/**
	 * @return the artifactType
	 */
	public String getArtifactType() {
		return artifactType;
	}

	/**
	 * @param artifactType the artifactType to set
	 */
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * @return the compositSolutiontoolKitTypeCode
	 */
	public String getCompositSolutiontoolKitTypeCode() {
		return compositSolutiontoolKitTypeCode;
	}

	/**
	 * @param compositSolutiontoolKitTypeCode the compositSolutiontoolKitTypeCode to set
	 */
	public void setCompositSolutiontoolKitTypeCode(String compositSolutiontoolKitTypeCode) {
		this.compositSolutiontoolKitTypeCode = compositSolutiontoolKitTypeCode;
	}

	/**
	 * @return the protoArtifactType
	 */
	public String getProtoArtifactType() {
		return protoArtifactType;
	}

	/**
	 * @param protoArtifactType the protoArtifactType to set
	 */
	public void setProtoArtifactType(String protoArtifactType) {
		this.protoArtifactType = protoArtifactType;
	}
	
	
	



}
