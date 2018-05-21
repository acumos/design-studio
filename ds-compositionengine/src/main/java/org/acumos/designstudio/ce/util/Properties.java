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

	private static final long serialVersionUID = -1551281465390705794L;

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

	@Value("${solutionErrorDescription}")
	private String solutionErrorDescription;

	@Value("${artifactType}")
	private String artifactType;

	@Value("${compositSolutionToolKitTypeCode}")
	private String compositSolutiontoolKitTypeCode;
	
	@Value("${protoArtifactType}")
	private String protoArtifactType;

	@Value("${protobufFileName}")
	private String protobufFileName;
	
	
	@Value("${packagepath}")
	private String packagepath;

	@Value("${className}")
	private String className;
	
	@Value("${protobufjar}")
	private String protobufjar;
	
	
	@Value("${fieldMapping}")
	private String fieldMapping;
	
	@Value("${target}")
	private String target;
	
	@Value("${gdmJarName}")
	private String gdmJarName;
	
	
	@Value("${solutionResultsetSize}")
	private int solutionResultsetSize;
	
	@Value("${blueprintArtifactType}")
	private String blueprintArtifactType;
	
	@Value("${gdmType}")
	private String gdmType;
	
	@Value("${databrokerType}")
	private String databrokerType;
	
	@Value("${modelImageArtifactType}")
	private String modelImageArtifactType;

	@Value("${protobuffFileExtention}")	
	private String protobuffFileExtention;
	
	
	/**
	 * @return the protobuffFileExtention
	 */
	public String getProtobuffFileExtention() {
		return protobuffFileExtention;
	}

	/**
	 * @return the modelImageArtifactType
	 */
	public String getModelImageArtifactType() {
		return modelImageArtifactType;
	}
	
	/**
	 * @return the gdmType
	 */
	public String getGdmType() {
		return gdmType;
	}

	/**
	 * @return the databrokerType
	 */
	public String getDatabrokerType() {
		return databrokerType;
	}

	/**
	 * @return the blueprintArtifactType
	 */
	public String getBlueprintArtifactType() {
		return blueprintArtifactType;
	}

	/**
	 * @return the solutionResultsetSize
	 */
	public int getSolutionResultsetSize() {
		return solutionResultsetSize;
	}

	/**
	 * @return the gdmJarName
	 */
	public String getGdmJarName() {
		return gdmJarName;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the fieldMapping
	 */
	public String getFieldMapping() {
		return fieldMapping;
	}

	/**
	 * @return the protobufjar
	 */
	public String getProtobufjar() {
		return protobufjar;
	}

	/**
	 * @return the packagepath
	 */
	public String getPackagepath() {
		return packagepath;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the protobufFileName
	 */
	public String getProtobufFileName() {
		return protobufFileName;
	}

	/**
	 * @return the solutionErrorCode
	 */
	public String getSolutionErrorCode() {
		return solutionErrorCode;
	}

	/**
	 * @return the solutionErrorDesc
	 */
	public String getSolutionErrorDesc() {
		return solutionErrorDesc;
	}

	/**
	 * @return the compositionErrorCode
	 */
	public String getCompositionErrorCode() {
		return compositionErrorCode;
	}

	/**
	 * @return the compositionErrorDescription
	 */
	public String getCompositionErrorDescription() {
		return compositionErrorDescription;
	}

	/**
	 * @return the successCode
	 */
	public String getSuccessCode() {
		return successCode;
	}

	/**
	 * @return the compositionSolutionErrorCode
	 */
	public String getCompositionSolutionErrorCode() {
		return compositionSolutionErrorCode;
	}

	/**
	 * @return the compositionSolutionErrorDesc
	 */
	public String getCompositionSolutionErrorDesc() {
		return compositionSolutionErrorDesc;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @return the toolKit
	 */
	public String getToolKit() {
		return toolKit;
	}

	/**
	 * @return the visibilityLevel
	 */
	public String getVisibilityLevel() {
		return visibilityLevel;
	}

	/**
	 * @return the artifactTypeCode
	 */
	public String getArtifactTypeCode() {
		return artifactTypeCode;
	}

	/**
	 * @return the askToUpdateExistingCompSolnMsg
	 */
	public String getAskToUpdateExistingCompSolnMsg() {
		return askToUpdateExistingCompSolnMsg;
	}

	/**
	 * @return the privateAccessTypeCode
	 */
	public String getPrivateAccessTypeCode() {
		return privateAccessTypeCode;
	}

	/**
	 * @return the publicAccessTypeCode
	 */
	public String getPublicAccessTypeCode() {
		return publicAccessTypeCode;
	}

	/**
	 * @return the organizationAccessTypeCode
	 */
	public String getOrganizationAccessTypeCode() {
		return organizationAccessTypeCode;
	}

	/**
	 * @return the getSolutionErrorDescription
	 */
	public String getSolutionErrorDescription() {
		return solutionErrorDescription;
	}

	/**
	 * @return the artifactType
	 */
	public String getArtifactType() {
		return artifactType;
	}

	/**
	 * @return the compositSolutiontoolKitTypeCode
	 */
	public String getCompositSolutiontoolKitTypeCode() {
		return compositSolutiontoolKitTypeCode;
	}

	/**
	 * @return the protoArtifactType
	 */
	public String getProtoArtifactType() {
		return protoArtifactType;
	}
	
	
}
