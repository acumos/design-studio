/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual
 * 						Property & Tech
 * 						Mahindra. All rights reserved.
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

import org.acumos.designstudio.ce.docker.TagImageCommand;
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
public class TagImageCommandTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
	}
	
	TagImageCommand tagImageCommand = new TagImageCommand("H2O", "Nexus", "H2O", true, true);
	
	@Test
	public void getDisplayNameTest() {
		Assert.assertNotNull(tagImageCommand.getImage());
	}
	
	@Test
	public void getImage() {
		tagImageCommand.getImage();
		tagImageCommand.getDisplayName();
		tagImageCommand.getIgnoreIfNotFound();
		tagImageCommand.getRepository();
		tagImageCommand.getTag();
		tagImageCommand.getWithForce();
		Assert.assertNotNull(tagImageCommand);
	}
	// TODO : WIP 
	//@Test
	public void executeTest(){
		TagImageCommand tagImageCommand = new TagImageCommand("H2O", "Nexus", "H2O", true, true);
		tagImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeImageExceptionTest(){
		TagImageCommand tagImageCommand = new TagImageCommand("", "Nexus", "H2O", true, true);
		tagImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeRepoExceptionTest(){
		TagImageCommand tagImageCommand = new TagImageCommand("H2O", "", "H2O", true, true);
		tagImageCommand.execute();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void executeTagExceptionTest(){
		TagImageCommand tagImageCommand = new TagImageCommand("H2O", "Nexus", "", true, true);
		tagImageCommand.execute();
	}
}
