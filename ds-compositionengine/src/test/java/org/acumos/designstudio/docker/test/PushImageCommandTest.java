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

package org.acumos.designstudio.docker.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.docker.DockerCommand;
import org.acumos.designstudio.ce.docker.PushImageCommand;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClient;

@RunWith(MockitoJUnitRunner.class)
public class PushImageCommandTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private DockerClient client;
	
	@Mock
	private DockerCommand dockerCommand;
	
	@Before
	public void setUp() {
		client = mock(DockerClient.class);
		dockerCommand = mock(DockerCommand.class);
		dockerCommand.setClient(client);
		MockitoAnnotations.initMocks(this);
	}

	PushImageCommand pushImageCommand = new PushImageCommand("H2O", "1.0.0-SNAPSHOT", "Nexus");
	
	@Test
	public void getImage() {
		pushImageCommand.getImage();
	}

	@Test
	public void getTag() {
		pushImageCommand.getTag();
	}

	@Test
	public void getRegistry() {
		pushImageCommand.getRegistry();
	}
	
	@Test
	public void getDisplayNameTest() {
		pushImageCommand.getDisplayName();
	}
	// TODO : WIP Need to work on it
	//@Test
	public void executeTest(){
		when(dockerCommand.getClient()).thenReturn(client);
		PushImageCommand pushImageCommand = new PushImageCommand("H2O", "1.0.0-SNAPSHOT", "Nexus");
		pushImageCommand.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void executeExceptionTest(){
		PushImageCommand pushImageCommand = new PushImageCommand("", "1.0.0-SNAPSHOT", "Nexus");
		pushImageCommand.execute();
	}


}
