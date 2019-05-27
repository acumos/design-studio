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

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class UtilTest {
	
	@Mock 
	private Runtime mockRuntime;
	
	@Mock
	private Process process;
	
	@Mock
	private ConfigurationProperties confprops;
	@Mock
	private Properties props;
	
	@Test
	public void runCommandTest() throws Exception {
		String cmd = "java";
		PowerMockito.mockStatic(Runtime.class);
		when(Runtime.getRuntime()).thenReturn(mockRuntime);
		when(mockRuntime.exec(cmd)).thenReturn(process);
		when(process.getErrorStream()).thenReturn(getInputStream("error"));
		DSUtil.runCommand(cmd);

		String protobufjarpath = confprops.getLib() + props.getProtobufjar();
		String path = "/local/";
		String packagepath = props.getPackagepath();
		String className = props.getClassName();
		String targetPath = path + props.getTarget();

		String command = "/usr/bin/javac -cp " + protobufjarpath + " " + path + packagepath + className + ".java"
				+ " -d " + targetPath;
		DSUtil.runCommand(command);
	}
	
	private InputStream getInputStream(String text) {
		return new ByteArrayInputStream(text.getBytes());
	}

}
