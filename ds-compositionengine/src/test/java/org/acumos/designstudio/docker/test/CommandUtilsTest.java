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

import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.docker.CommandUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class CommandUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
	}
	
	CommandUtils utils = new CommandUtils();
	
	@Test
	public void imageFullNameFromTest(){
		String imageName = CommandUtils.imageFullNameFrom("docker", "nexus", "latest");
		Assert.assertNotNull(imageName);
		String latestImageName = CommandUtils.imageFullNameFrom("", "nexus", "latest");
		Assert.assertNotNull(latestImageName);
		String newImageName = CommandUtils.imageFullNameFrom("", "nexus", "");
		Assert.assertNotNull(newImageName);
		String oldImageName = CommandUtils.imageFullNameFrom("docker", "nexus", "");
		Assert.assertNotNull(oldImageName);
	}
	
	@Test
	public void addLatestTagIfNeededTest(){
		String tag = CommandUtils.addLatestTagIfNeeded("genericmodel");
		Assert.assertNotNull(tag);
		String latestTag = CommandUtils.addLatestTagIfNeeded("[.+:genericmodel");
		Assert.assertNotNull(latestTag);
	}
	
	@Test
	public void sizeInBytesTest(){
		String str = "Dockerimages";
		long bytes = CommandUtils.sizeInBytes(str);
		Assert.assertNotNull(bytes);
		String newStr = "637435";
		long latestBytes = CommandUtils.sizeInBytes(newStr);
		Assert.assertNotNull(latestBytes);
	}
	

}
