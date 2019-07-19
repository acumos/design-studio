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

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.docker.CreateImageCommand;
import org.acumos.designstudio.ce.docker.DockerCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;

@RunWith(MockitoJUnitRunner.class)
public class CreateImageCommandTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	private DockerClient dockerClient;
	
	@Mock
	private DockerCommand dockerCommand;
	
	@Mock
	private CreateImageCommand imageCommand;
	
	@Mock
	private BuildImageCmd buildImageCmd;
	
	@Before
	public void setUp() {
		buildImageCmd = mock(BuildImageCmd.class);
		dockerClient = mock(DockerClient.class);
		dockerCommand.setClient(dockerClient);
		dockerCommand = mock(DockerCommand.class);
		MockitoAnnotations.initMocks(this);
	}
	
	File srcFile = new File("newFile.csv");
	
	CreateImageCommand createImageCommand = new CreateImageCommand(srcFile,"H2O","1.0.0","H20",true,true);
	
	@Test
	public void testCommon() {
		CreateImageCommand createImageCommand = new CreateImageCommand(new File("tmp"), "genericjava", "latest",
				"Dockerfile", true, true);
		createImageCommand.setBuildArgs("run");
		createImageCommand.getBuildArgs();
		String str = createImageCommand.getImageId();
		Assert.assertNotNull(createImageCommand.getBuildArgs());
	}
	
	@Test
	public void getDisplayName() {
		Assert.assertNotNull(createImageCommand.getDisplayName());
	}
	// TODO : WIP 
	@Test(expected = RuntimeException.class)
	public void executeTest(){
		File dir = new File("./src/test/java/docker");
		if(!dir.exists()){
			dir.mkdir();
		}
		String fileName = "Dockerfile";
		File tagFile = new File(dir,fileName);
		if (!tagFile.exists()) {
			try {
				tagFile.createNewFile();
			} catch (IOException e) {
				logger.error("Exception occured while creating the new file");
			}
		}
		CreateImageCommand createImageCommand = new CreateImageCommand(dir, "genericjava", "latest",
				"Dockerfile", true, true);
		createImageCommand.setBuildArgs("run,=run");
		createImageCommand.getBuildArgs();
		//when(dockerCommand.getClient()).thenReturn(dockerClient);
		dockerCommand.setClient(dockerClient);
		//Mockito.when(dockerCommand.getClient()).thenReturn(dockerClient);
		when(dockerClient.buildImageCmd(tagFile)).thenReturn(buildImageCmd);
		createImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeDockerFolderTest(){
		CreateImageCommand createImageCommand = new CreateImageCommand(null, "genericjava", "latest",
				"Dockerfile", true, true);
		createImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeImageNameTest(){
		CreateImageCommand createImageCommand = new CreateImageCommand(new File("docker"), null, "latest",
				"Dockerfile", true, true);
		createImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeImageTagTest(){
		CreateImageCommand createImageCommand = new CreateImageCommand(new File("docker"), "genericjava", null,
				"Dockerfile", true, true);
		createImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeDockerFolderExistsTest(){
		CreateImageCommand createImageCommand = new CreateImageCommand(new File("docker"), "genericjava", "latest",
				"Dockerfile", true, true);
		createImageCommand.execute();
	}
	
	@Test
	public void settersTest(){
		imageCommand.setBuildArgs("run");
		imageCommand.getBuildArgs();
		imageCommand.getImageId();
	}

}
