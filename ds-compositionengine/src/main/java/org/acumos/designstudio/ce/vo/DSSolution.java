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

import java.io.Serializable;

/**
 * 
 *
 *
 */
public class DSSolution implements Serializable {

	private static final long serialVersionUID = -8356434223773490779L;

	private String solutionId;
	private String solutionRevisionId;

	private String solutionName;
	private String version;
	private String onBoarder;
	private String author;
	private String provider;
	private String toolKit;
	private String category;
	private String description;
	private String visibilityLevel;
	private String createdDate;
	private String modifiedDate;
	private String icon; // the icon path.
	private boolean isDuplicateSolution;

	public DSSolution() {
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
	 * @param isDuplicateSolution 
	 * 			boolean
	 */
	public DSSolution(String solutionId, String solutionRevisionId, String solutionName, String version,
			String onBoarder, String author, String provider, String toolKit, String category, String description,
			String visibilityLevel, String createdDate, String modifiedDate, String icon, boolean isDuplicateSolution) {
		super();

		this.solutionId = solutionId;
		this.solutionRevisionId = solutionRevisionId;

		this.solutionName = solutionName;
		this.version = version;
		this.onBoarder = onBoarder;
		this.author = author;
		this.provider = provider;
		this.toolKit = toolKit;
		this.category = category;
		this.description = description;
		this.visibilityLevel = visibilityLevel;
		this.isDuplicateSolution = isDuplicateSolution;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the solutionId
	 */
	public String getSolutionId() {
		return solutionId;
	}

	/**
	 * @param solutionId
	 *            the solutionId to set
	 */
	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	/**
	 * @return the solutionRevisionId
	 */
	public String getSolutionRevisionId() {
		return solutionRevisionId;
	}

	/**
	 * @param solutionRevisionId
	 *            the solutionRevisionId to set
	 */
	public void setSolutionRevisionId(String solutionRevisionId) {
		this.solutionRevisionId = solutionRevisionId;
	}

	/**
	 * @return the name
	 */
	public String getSolutionName() {
		return this.solutionName;
	}

	/**
	 * @param solutionName
	 *            the name to set
	 */
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
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
	 * @param toolKit
	 *            the toolKit to set
	 */
	public void setToolKit(String toolKit) {
		this.toolKit = toolKit;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the visibilityLevel
	 */
	public String getVisibilityLevel() {
		return visibilityLevel;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param visibilityLevel
	 *            the visibilityLevel to set
	 */
	public void setVisibilityLevel(String visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

	/**
	 * @return the version
	 */

	/**
	 * @return the onBoarder
	 */
	public String getOnBoarder() {
		return onBoarder;
	}

	/**
	 * @param onBoarder
	 *            the onBoarder to set
	 */
	public void setOnBoarder(String onBoarder) {
		this.onBoarder = onBoarder;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	

	/**
	 * @return the isDuplicateSolution
	 */
	public boolean isDuplicateSolution() {
		return isDuplicateSolution;
	}

	/**
	 * @param isDuplicateSolution the isDuplicateSolution to set
	 */
	public void setDuplicateSolution(boolean isDuplicateSolution) {
		this.isDuplicateSolution = isDuplicateSolution;
	}

	@Override
	public String toString() {
		return "DSSolution [solutionId=" + solutionId + "solutionRevisionId=" + solutionRevisionId + "name="
				+ solutionName + ", version=" + version + ", onBoarder=" + onBoarder + ", author=" + author
				+ ", provider=" + provider + ", toolKit=" + toolKit + ", category=" + category + ", description="
				+ description + ", visibilityLevel=" + visibilityLevel + ", isDuplicateSolution=" + isDuplicateSolution + "]";
	}

	/**
	 * 
	 * @return Content as JSON
	 */
	public String toJsonString() {
		return "{\"solutionId\":\"" + solutionId + "\", \"solutionName\":\""
				+ solutionName.trim().replaceAll("[\n\r]", "") + "\", \"version\":\""
				+ version.trim().replaceAll("[\n\r]", "") + "\", \"ownerId\":\"" + author + "\", \"provider\":\""
				+ (null != provider ? provider.trim().replaceAll("[\n\r]", "") : provider) + "\", \"toolKit\":\""
				+ toolKit + "\", \"category\":\"" + category + "\", \"description\":\""
				+ (null != description ? description.trim().replaceAll("[\n\r]", "") : description)
				+ "\", \"visibilityLevel\":\"" + visibilityLevel + "\",  \"isDuplicateSolution\":\"" + isDuplicateSolution + "\", \"created\":\"" + createdDate
				+ "\", \"icon\":\"" + icon + "\"}";
	}

}
