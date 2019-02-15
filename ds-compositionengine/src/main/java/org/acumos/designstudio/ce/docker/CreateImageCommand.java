/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual
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

package org.acumos.designstudio.ce.docker;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.command.BuildImageResultCallback;

/**
 * This command creates a new image from specified Dockerfile.
 *
 * @see <A HREF=
 *      "http://docs.docker.com/reference/api/docker_remote_api_v1.13/#build-an-image-from-dockerfile-via-stdin">Docker
 *      build</A>
 */
public class CreateImageCommand extends DockerCommand {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final File dockerFolder;

	private final String imageName;

	private final String imageTag;

	private final String dockerFile;

	private final boolean noCache;

	private final boolean rm;

	private String buildArgs;

	private String imageId;

	/**
	 * 
	 * @param dockerFolder
	 *            Docker folder
	 * @param imageName
	 *            Image name
	 * @param imageTag
	 *            Image tag
	 * @param dockerFile
	 *            Docker file
	 * @param noCache
	 *            Boolean
	 * @param rm
	 *            RM
	 */
	public CreateImageCommand(File dockerFolder, String imageName, String imageTag, String dockerFile, boolean noCache,
			boolean rm) {
		this.dockerFolder = dockerFolder;
		this.imageName = imageName;
		this.imageTag = imageTag;
		this.dockerFile = dockerFile;
		this.noCache = noCache;
		this.rm = rm;
	}

	public String getBuildArgs() {
		return buildArgs;
	}

	/**
	 * 
	 * @param buildArgs
	 *            The build arguments
	 */
	public void setBuildArgs(String buildArgs) {
		this.buildArgs = buildArgs;
	}

	public String getImageId() {
		return imageId;
	}

	@Override
	public void execute() throws DockerException {
		if (dockerFolder == null) {
			throw new IllegalArgumentException("dockerFolder is not configured");
		}
		if (imageName == null) {
			throw new IllegalArgumentException("imageName is not configured");
		}
		if (imageTag == null) {
			throw new IllegalArgumentException("imageTag is not configured");
		}
		if (!dockerFolder.exists()) {
			throw new IllegalArgumentException("configured dockerFolder '" + dockerFolder + "' does not exist.");
		}
		final Map<String, String> buildArgsMap = new HashMap<String, String>();
		logger.debug(" Docker buildArgsMap for testing {}" , buildArgsMap);
		if ((buildArgs != null) && (!buildArgs.trim().isEmpty())) {
			logger.debug("Parsing buildArgs: {}" , buildArgs);
			String[] split = buildArgs.split(",|;");
			for (String arg : split) {
				String[] pair = arg.split("=");
				if (pair.length == 2) {
					buildArgsMap.put(pair[0].trim(), pair[1].trim());
				} else {
					logger.error("Invalid format for  {}. Buildargs should be formatted as key=value" ,arg);
				}
			}
		}
		String dockerFile = this.dockerFile == null ? "Dockerfile" : this.dockerFile;
		File docker = new File(dockerFolder, dockerFile);
		logger.debug("Docker File for testing {}",  docker);
		if (!docker.exists()) {
			throw new IllegalArgumentException(
					String.format("Configured Docker file '%s' does not exist.", dockerFile));
		}
		DockerClient client = getClient();
		logger.debug("Docker Client for testing {}", client);
		try {
			logger.debug("Docker callback for entered");
			BuildImageResultCallback callback = new BuildImageResultCallback() {
				@Override
				public void onNext(BuildResponseItem item) {
					if (item.getStream() != null)
						logger.debug("StreamData :  {}", item.getStream());
					else
						logger.debug("StreamData: {}", item);
					super.onNext(item);
				}

				@Override
				public void onError(Throwable throwable) {
					logger.error("Failed to creating docker image", throwable);
					throwable.printStackTrace();
					super.onError(throwable);
				}
			};
			logger.debug("Docker callback for closed {} " , callback);
			BuildImageCmd buildImageCmd = client.buildImageCmd(docker)
					.withTags(new HashSet<>(Arrays.asList(imageName + ":" + imageTag))).withNoCache(noCache)
					.withRemove(rm);// .withTag(imageName + ":" + imageTag)
			logger.debug("Docker buildImageCmd for testing {}" , buildImageCmd);
			if (!buildArgsMap.isEmpty()) {
				for (final Map.Entry<String, String> entry : buildArgsMap.entrySet()) {
					buildImageCmd = buildImageCmd.withBuildArg(entry.getKey(), entry.getValue());
				}
			}
			BuildImageResultCallback result = buildImageCmd.exec(callback);

			logger.debug("Docker result for testing start {} " , result);

			this.imageId = result.awaitImageId();

			logger.debug("Docker result for testing end {} ", result);

		} catch (Exception e) {
			logger.error("RuntimeException has came", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDisplayName() {
		return "Create/build image";
	}
}
