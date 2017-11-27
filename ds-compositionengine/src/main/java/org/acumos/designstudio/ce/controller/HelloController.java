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

package org.acumos.designstudio.ce.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.vo.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//@RestController
//@RequestMapping("/dc")
public class HelloController {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(HelloController.class);

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@ApiOperation(value = "Greets the user.", response = Greeting.class, responseContainer = "String")
	@RequestMapping(value = "/greeting", method = RequestMethod.GET)
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		logger.debug(EELFLoggerDelegator.debugLogger, "------ greeting() starts------");

		logger.debug(EELFLoggerDelegator.debugLogger, "------ greeting() Ends------");
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

}
