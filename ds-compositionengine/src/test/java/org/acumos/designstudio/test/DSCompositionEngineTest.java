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

package org.acumos.designstudio.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPUser;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class DSCompositionEngineTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(DSCompositionEngineTest.class);
	private String url = "";
	private String user = "";
	private String pass = "";
	public static Properties CONFIG = new Properties();
	ICommonDataServiceRestClient client = null;

	@Before
	/**
	 * This method is used to set default values for the instance of
	 * ICommonDataServiceRestClient and NexusArtifactClient by passing common
	 * data service and nexus url, username and password respectively
	 * 
	 * @throws Exception
	 */
	public void createClient() throws Exception {
		CONFIG.load(SolutionControllerTest.class.getResourceAsStream("/application.properties"));

		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		client = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);
	}
}
