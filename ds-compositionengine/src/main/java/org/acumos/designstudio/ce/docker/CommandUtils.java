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

package org.acumos.designstudio.ce.docker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Util class to for docker commands.
 */
public class CommandUtils {

	/**
	 * 
	 * @param registry
	 *            Docker registry
	 * @param repoAndImg
	 *            Repo and image component of URI
	 * @param tag
	 *            Image tag
	 * @return Image full name
	 */
	public static String imageFullNameFrom(String registry, String repoAndImg, String tag) {
		if (StringUtils.isNotBlank(registry) || StringUtils.isNotBlank(tag)) {
			StringBuilder sb = new StringBuilder();
			if (StringUtils.isNotBlank(registry)) {
				sb.append(registry).append("/").append(repoAndImg);
			} else {
				sb.append(repoAndImg);
			}
			if (StringUtils.isNotBlank(tag)) {
				sb.append(":").append(tag);
			}
			return sb.toString();
		} else {
			return repoAndImg;
		}
	}

	/**
	 * 
	 * @param fullImageName
	 *            Full image name
	 * @return Image with tag
	 */

	public static String addLatestTagIfNeeded(String fullImageName) {
		// Assuming that the fullImageName is a valid name, the pattern is
		// enough to decide if it contains tag or not.
		if (fullImageName.matches(".+:[^:/]+$")) {
			return fullImageName;
		}
		return fullImageName + ":latest";
	}

	/**
	 * 
	 * @param size
	 *            Size in bytes allowing for gmkb; e.g., "7m"
	 * @return Long Parsed value
	 */
	public static long sizeInBytes(String size) {
		long returnValue = -1;
		Pattern patt = Pattern.compile("^([\\d.]+)([gmkb]?)$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patt.matcher(size);
		Map<String, Integer> powerMap = new HashMap<String, Integer>();
		powerMap.put("g", 3);
		powerMap.put("m", 2);
		powerMap.put("k", 1);
		powerMap.put("b", 0);
		if (matcher.find()) {
			String number = matcher.group(1);
			int pow = matcher.group(2) != null && matcher.group(2).length() > 0
					? powerMap.get(matcher.group(2).toLowerCase())
					: 0;
			BigDecimal bytes = new BigDecimal(number);
			bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow));
			returnValue = bytes.longValue();
		}
		return returnValue;
	}
}
