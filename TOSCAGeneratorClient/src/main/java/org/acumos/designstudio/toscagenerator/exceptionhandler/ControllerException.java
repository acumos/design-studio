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

package org.acumos.designstudio.toscagenerator.exceptionhandler;

import org.acumos.designstudio.toscagenerator.util.Properties;

/**
 * 
 * 
 *
 */
public class ControllerException extends AcumosException {
	private static final long serialVersionUID = 1L;


	public static final String CONNECTION_ERROR_CODE = Properties.getConnectionErrorCode();
	public static final String CONNECTION_ERROR_DESC = Properties.getConnectionErrorDesc();
	public static final String DATABASE_ACCESS_ERROR_CODE = Properties.getDatabaseAccessErrorCode();
	public static final String DATABASE_ACCESS_ERROR_DESC = Properties.getDatabaseAccessErrorDesc();
	public static final String DATA_INTEGRITY_VIOLATION_CODE = Properties.getDataIntegrityViolationCode();
	public static final String DATA_INTEGRITY_VIOLATION_DESC = Properties.getDataIntegrityViolationDesc();
	public static final String DATA_NOT_FOUND_CODE = Properties.getDatanotFoundErrorCode();
	public static final String DATA_NOT_FOUND_DESC = Properties.getDatanotFoundErrorDesc();
	public static final String DECRYPTION_ERROR_CODE = Properties.getDecryptionErrorCode();
	public static final String DECRYPTION_ERROR_DESC = Properties.getDecryptionErrorDesc();
	public static final String ENCRYPTION_ERROR_CODE = Properties.getEncryptionErrorCode();
	public static final String ENCRYPTION_ERROR_DESC = Properties.getEncryptionErrorDesc();
	public static final String FILE_DELETION_ERROR_CODE = Properties.getFileDeletionErrorCode();
	public static final String FILE_DELETION_ERROR_DESC = Properties.getFileDeletionErrorDesc();
	public static final String FILE_NOT_FOUND_CODE = Properties.getFileNotFoundErrorCode();
	public static final String FILE_NOT_FOUND_DESC = Properties.getFileNotFoundErrorDesc();
	public static final String FILE_UPLOAD_ERROR_CODE = Properties.getUploadFileErrorCode();
	public static final String FILE_UPLOAD_ERROR_DESC = Properties.getUploadFileErrorDesc();
	public static final String MODELMETADATA_ERROR_CODE = Properties.getMetaDataErrorCode();
	public static final String MODELMETADATA_ERROR_DESC = Properties.getMetaDataErrorDesc();
	public static final String TOSCA_FILE_GENERATION_ERROR_CODE = Properties.getTOSCAFileGenerationErrorCode();
	public static final String TOSCA_FILE_GENERATION_ERROR_DESC = Properties.getTOSCAFileGenerationErrorDesc();


	public ControllerException(String message, String errorCode, String errorDesc) {
		super(message, errorCode, errorDesc);
	}

	public ControllerException(String message, String errorCode, String errorDesc, Throwable cause) {
		super(message, errorCode, errorDesc, cause);
	}

	public ControllerException(String message, String errorCode, String errorDesc, Throwable cause, Object param) {
		super(message, errorCode, errorDesc, cause, param);
	}

}
