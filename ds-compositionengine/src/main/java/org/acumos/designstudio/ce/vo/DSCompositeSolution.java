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

package org.acumos.designstudio.ce.vo;

/**
 * 
 * 
 *
 */
public class DSCompositeSolution extends DSSolution {

	private static final long serialVersionUID = 1L;

	private String cId;
	private boolean ignoreLesserVersionConflictFlag;
	private Artifact cdump;

	public DSCompositeSolution() {
		super();

	}

	/**
	 * 
	 * @param solutionId
	 *            Solution ID
	 * @param solutionRevisionId
	 *            Revision ID
	 * @param solutionName
	 *            Solution name
	 * @param version
	 *            Version string
	 * @param onBoarder
	 *            User ID
	 * @param author
	 *            User ID
	 * @param provider
	 *            Provider
	 * @param toolKit
	 *            Toolkit type code
	 * @param category
	 *            Category code
	 * @param description
	 *            Description
	 * @param visibilityLevel
	 *            Visibility
	 * @param createdDate
	 *            Date
	 * @param modifiedDate
	 *            Date
	 * @param icon
	 *            Image
	 * @param cId
	 *            Composite ID
	 * @param ignoreLesserVersionConflictFlag
	 *            Boolean
	 * @param isDuplicateSolution
	 * 				boolean
	 */
	public DSCompositeSolution(String solutionId, String solutionRevisionId, String solutionName, String version,
			String onBoarder, String author, String provider, String toolKit, String category, String description,
			String visibilityLevel, String createdDate, String modifiedDate, String icon, String cId,
			boolean ignoreLesserVersionConflictFlag, boolean isDuplicateSolution) {
		super(solutionId, solutionRevisionId, solutionName, version, onBoarder, author, provider, toolKit, category,
				description, visibilityLevel, createdDate, modifiedDate, icon, isDuplicateSolution);
		this.cId = cId;
		this.ignoreLesserVersionConflictFlag = ignoreLesserVersionConflictFlag;

	}

	/**
	 * @return the cdump
	 */
	public Artifact getCdump() {
		return cdump;
	}

	/**
	 * @param cdump
	 *            the cdump to set
	 */
	public void setCdump(Artifact cdump) {
		this.cdump = cdump;
	}

	/**
	 * @return the cId
	 */
	public String getcId() {
		return cId;
	}

	/**
	 * @param cId
	 *            the cId to set
	 */
	public void setcId(String cId) {
		this.cId = cId;
	}

	/**
	 * @return the ignoreLesserVersionConflictFlag
	 */
	public boolean getIgnoreLesserVersionConflictFlag() {
		return ignoreLesserVersionConflictFlag;
	}

	/**
	 * @param ignoreLesserVersionConflictFlag
	 *            the ignoreLesserVersionConflictFlag to set
	 */
	public void setIgnoreLesserVersionConflictFlag(boolean ignoreLesserVersionConflictFlag) {
		this.ignoreLesserVersionConflictFlag = ignoreLesserVersionConflictFlag;
	}

}
