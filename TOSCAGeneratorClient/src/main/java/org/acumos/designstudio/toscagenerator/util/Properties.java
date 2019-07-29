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

package org.acumos.designstudio.toscagenerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Properties {
	private static final Logger logger = LoggerFactory.getLogger(Properties.class);

	private static String propertyFileName = "TOSCAApplication.properties";

	// to load application's properties, we use this class
	private static java.util.Properties mainProperties = null;

	private static ConfigurationProperties configurationProperties = null;

	static {
		getMainProperties();
	}

	private static void getMainProperties() {
		logger.debug("--------------- getMainProperties() started -------------");

		if (mainProperties == null) {
			InputStream is = null;
			try {
				mainProperties = new java.util.Properties();
				Properties pro = new Properties();
				is = pro.getClass().getResourceAsStream("/" + propertyFileName);
				if (is == null) {
					logger.debug("Not found in classpath trying to load from filesystem");
					is = new FileInputStream("./TOSCAApplication.properties");
				}
				mainProperties.load(is);

				configurationProperties = ConfigurationProperties.getConfigurationProperties();
				logger.debug("------------  getMainProperties() ended --------------");
			} catch (Exception e) {
				logger.error("-------------- Exception Occured getMainProperties() --------------", e);
			}
		}
	}

	/**
	 * 
	 * @param solutionID
	 *            Solution ID
	 * @param version
	 *            Version String
	 * @return Temporary folder path
	 */
	public static String getTempFolderPath(String solutionID, String version) {
		logger.debug("--------------- getTempFolderPath() started --------------");
		String path = configurationProperties.getToscaOutputFolder();
		int pathlength = 0;
		String lastchar = "";
		if (!path.trim().isEmpty()) {
			pathlength = path.length();
			lastchar = path.substring(pathlength - 1);
			if (lastchar.equals("/")) {
				path = path + solutionID + version + '/';
			} else {
				path = path + "/" + solutionID + version + '/';
			}
			// create the directory for the solution and version specified
			File dir = new File(path);
			logger.debug("------------ Directory for Solution and Version : -------------" + dir);
			if (!dir.exists()) {
				logger.debug("------------ Directory not exists for Solution and Version : --------------------");
				dir.mkdir();
				logger.debug("----------- New Directory created for Solution and Version : ----------------" + dir);
			}
		}
		logger.debug("-------------  getTempFolderPath() ended ---------------");
		return path;
	}

	public static String getToscaGeneratorEndPointURL() {
		return configurationProperties.getToscaGeneratorEndPointURL();
	}

	public static String getMetaDataFileName() {
		return mainProperties.getProperty("metaDataFileName", "");
	}

	public static String getNexusEndPointURL() {
		return configurationProperties.getNexusEndPointURL();
	}

	public static String getNexusUserName() {
		// return mainProperties.getProperty("nexusUserName", "");
		return configurationProperties.getNexusUserName();
	}

	public static String getNexusPassword() {
		// return mainProperties.getProperty("nexusPassword", "");
		return configurationProperties.getNexusPassword();
	}

	public static String getNexusProxy() {
		return mainProperties.getProperty("nexusProxy", "");
	}

	public static String getNexusGropuId() {
		// return mainProperties.getProperty("nexusGroupId", "");
		return configurationProperties.getNexusGroupId();
	}

	public static String getNexusRepositoryHost() {
		return mainProperties.getProperty("nexus_repository_host", "");
	}

	public static String getNexusRepositoryPort() {
		return mainProperties.getProperty("nexus_repository_port", "");
	}

	public static String getNexusRepositoryScheme() {
		return mainProperties.getProperty("scheme", "");
	}

	public static String getNexusSolutionArtifactRepo() {
		return mainProperties.getProperty("solution_artifact_repo", "");

	}

	public static String getCmnDataSvcEndPoinURL() {
		// return mainProperties.getProperty("cmnDataSvcEndPoinURL", "");
		return configurationProperties.getCmnDataSvcEndPoinURL();
	}

	public static String getCmnDataSvcUser() {
		// return mainProperties.getProperty("cmnDataSvcUser", "");
		return configurationProperties.getCmnDataSvcUser();
	}

	public static String getCmnDataSvcPwd() {
		// return mainProperties.getProperty("cmnDataSvcPwd", "");
		return configurationProperties.getCmnDataSvcPwd();
	}

	// For Custom Exceptions

	public static String getConnectionErrorCode() {
		return mainProperties.getProperty("connectionErrorCode", "");
	}

	public static String getConnectionErrorDesc() {
		return mainProperties.getProperty("connectionErrorDesc", "");
	}

	public static String getDatabaseAccessErrorCode() {
		return mainProperties.getProperty("databaseAccessErrorCode", "");
	}

	public static String getDatabaseAccessErrorDesc() {
		return mainProperties.getProperty("databaseAccessErrorDesc", "");
	}

	public static String getDataIntegrityViolationCode() {
		return mainProperties.getProperty("dataIntegrityViolationCode", "");
	}

	public static String getDataIntegrityViolationDesc() {
		return mainProperties.getProperty("dataIntegrityViolationDesc", "");
	}

	public static String getDatanotFoundErrorCode() {
		return mainProperties.getProperty("dataNotFoundCode", "");
	}

	public static String getDatanotFoundErrorDesc() {
		return mainProperties.getProperty("dataNotFoundDesc", "");
	}

	public static String getDecryptionErrorCode() {
		return mainProperties.getProperty("decryptionErrorCode", "");
	}

	public static String getDecryptionErrorDesc() {
		return mainProperties.getProperty("decryptionErrorDesc", "");
	}

	public static String getEncryptionErrorCode() {
		return mainProperties.getProperty("encryptionErrorCode", "");
	}

	public static String getEncryptionErrorDesc() {
		return mainProperties.getProperty("encryptionErrorDesc", "");
	}

	public static String getFileDeletionErrorCode() {
		return mainProperties.getProperty("fileDeletionErrorCode", "");
	}

	public static String getFileDeletionErrorDesc() {
		return mainProperties.getProperty("fileDeletionErrorDesc", "");
	}

	public static String getFileNotFoundErrorCode() {
		return mainProperties.getProperty("fileNotFoundErrorCode", "");
	}

	public static String getFileNotFoundErrorDesc() {
		return mainProperties.getProperty("fileNotFoundErrorDesc", "");
	}

	public static String getUploadFileErrorCode() {
		return mainProperties.getProperty("fileUploadErrorCode", "");
	}

	public static String getUploadFileErrorDesc() {
		return mainProperties.getProperty("fileUploadErrorDesc", "");
	}

	public static String getMetaDataErrorCode() {
		return mainProperties.getProperty("modelMetadataErrorCode", "");
	}

	public static String getMetaDataErrorDesc() {
		return mainProperties.getProperty("modelMetadataErrorDesc", "");
	}

	public static String getTOSCAFileGenerationErrorCode() {
		return mainProperties.getProperty("toscaFileGenerationErrorCode", "");
	}

	public static String getTOSCAFileGenerationErrorDesc() {
		return mainProperties.getProperty("toscaFileGenerationErrorDesc", "");
	}

	public static String getInvalidCredsErrorCode() {
		return mainProperties.getProperty("invalidCredentialsCode", "");
	}

	public static String getInvalidCredsErrorDesc() {
		return mainProperties.getProperty("invalidCredentialsDesc", "");
	}

	public static String getTOSCATypeCode(String toscaType) {
		return mainProperties.getProperty(toscaType + "_code", "1");
	}

	public static String isOptionKeywordRequirede() {
		return mainProperties.getProperty("isOptionKeywordRequired", "");
	}

	public static String getProtobufBasicType() {
		return mainProperties.getProperty("protobufbasicType", "");
	}

}
