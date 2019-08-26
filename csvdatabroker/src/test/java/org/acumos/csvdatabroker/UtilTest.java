/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.csvdatabroker;

import java.io.IOException;

import org.acumos.csvdatabroker.util.DSUtil;
import org.junit.Test;

public class UtilTest {

	@Test
	public void utilTest() {
		DSUtil dSUtil = new DSUtil();
		dSUtil.createCdumpPath("test", "test");
		dSUtil.isValidJSON("{\"name\":\"john\",\"age\":22,\"class\":\"mca\"}");
		dSUtil.deleteFile("test");
		dSUtil.createCdumpPath("test", "test");
		dSUtil.isValidJsonaddLink("test");
		try {
			dSUtil.isValidJsonSchema("test");
		} catch (IOException e) {
			e.printStackTrace();
		}
		dSUtil.readCdumpPath("test", "test");
		try {
			dSUtil.writeDataToFile("test", "test", "test", "test");
		} catch (IOException e) {
			e.printStackTrace();
		}

		dSUtil.rmUserdir("test");
	}

}
