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

package org.acumos.csvdatabroker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.service.ProtobufService;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class RemoteScriptExecutor {

	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(RemoteScriptExecutor.class);
	
	//Environment Details 
	private String host;
	private Integer port;
	private String user;
	private String password;
	private String remoteDir;
	private String shellFileName;
	
	// JCraft objects to access Environment
	private JSch jsch;
	private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;
	private ChannelExec execChannel;
	

	private ProtobufService protoService;
	
	
	public RemoteScriptExecutor(String host, Integer port, String user, String password, String remoteDir,
			String shellFileName) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.remoteDir = remoteDir;
		this.shellFileName = shellFileName;
	}
	
	public void setProtobufService(ProtobufService protoService){
		this.protoService = protoService;
	}
	
	
	/**
	 * Included for test case purpose.
	 */
	public RemoteScriptExecutor(){
		
	}
	
	private void connect(String channelType) throws Exception {
		logger.debug("connecting..."+host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host,port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();

			channel = session.openChannel(channelType);  //sftp, exec, 
			
			channel.connect();
			if(channelType.equals("sftp")){
				sftpChannel = (ChannelSftp) channel;
			} else if(channelType.equals("exec")){
				execChannel = (ChannelExec) channel;
				execChannel.setErrStream(System.err);
			}

		} catch (JSchException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in connect()", e);
			throw new ServiceException("Not able to Connect Remote server ","401", "Not able to Connect Remote server ", e);
		}
	}
	
	private void disconnect(String channelType) {
		if(channelType.equals("sftp")){
			logger.debug("disconnecting sftpChannel...");
			sftpChannel.disconnect();
		} else if(channelType.equals("exec")){
			logger.debug("disconnecting execChannel...");
			execChannel.disconnect();
		}
		channel.disconnect();
		session.disconnect();
	}
	
	/**
	 * This method connects to the Remote server and create the shell file for the input Script.
	 * @param script
	 * 		This method accepts script
	 * @throws Exception
	 * 		This method throws the Exception
	 */
	@Deprecated
	public void createshellFile(String script) throws Exception {
		OutputStreamWriter writer = null;
		try {
			connect("sftp");
			// Change to output directory
			sftpChannel.cd(remoteDir);
			OutputStream out = sftpChannel.put(shellFileName);
			writer = new OutputStreamWriter(out);
			writer.write(script);
			writer.close();
			logger.debug("shell file created successfully - ");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createshellFile()", e);
			throw new ServiceException("Not able to create shell File ","401", "Not able to create shell File", e);
		} finally {
			if(null != writer){
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in createshellFile()", e);
				}
			}
		}
		disconnect("sftp");
	}
	
	/**
	 * This method execute the shell on the remote server and return the records at "start" converting it into protob binary format.
	 * @param start
	 * 		This method accepts script
	 * @return byte[]
	 * 		This method returns byte[]
	 * @throws Exception
	 * 		This method throws the Exception
	 */
	@Deprecated
	public byte[] executeShell(int start) throws Exception {
		BufferedReader reader = null;
		byte[] output = null;
		try{
			connect("exec");
			//create the execution channel over the session
			execChannel =  (ChannelExec)session.openChannel("exec");
			
			// Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
            InputStream in = execChannel.getInputStream();
            
            // Set the command that you want to execute
            String command = "sh "+remoteDir+shellFileName;
            execChannel.setCommand(command);
            // Execute the command
            execChannel.connect();
            
            //TODO : Need to re check the logic to make this class independent
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            
            int cnt = 0;
            
            while ((line = reader.readLine()) != null) {
            	if(cnt < start){
            		cnt++;
            		continue;
            	}
            	if(cnt == start ){
            		output = protoService.convertToProtobufFormat(line);
            		break;
            	}
            	
            	
            }
         // Command execution completed here.

            // Retrieve the exit status of the executed command
            int exitStatus = execChannel.getExitStatus();
            if (exitStatus > 0) {
                logger.error(EELFLoggerDelegator.errorLogger,"Remote script exec error! " + exitStatus);
            }
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeShell()", e);
			throw new ServiceException("Not able to execute shell File ","401", "Not able to execute shell File", e);
		} finally {
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeShell()", e);
				}
			}
		}
		disconnect("exec");
		return output;
	}
	
	
	/**
	 * This method read the file on remote server and return the records at "start" converting it into protob binary format.
	 * @param start
	 * 		This method accepts script
	 * @param filePath
	 * 		Remote server file path
	 * @return byte[]
	 * 		This method returns byte[]
	 * @throws Exception
	 * 		This method throws the Exception
	 */
	public byte[] getData(int start, String filePath) throws Exception {
		BufferedReader reader = null;
		byte[] output = null;
		InputStream in = null;
		try{
			connect("sftp");
			in = sftpChannel.get(filePath);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int cnt = 0;
            while ((line = reader.readLine()) != null) {
            	if(cnt < start){
            		cnt++;
            		continue;
            	}
            	if(cnt == start ){
            		output = protoService.convertToProtobufFormat(line);
            		break;
            	}
            }
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
			throw new ServiceException("Not able to read remote File ","401", "Not able to read remote File", e);
		} finally {
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
				}
			}
			if(null != in ){
				in.close();
			}
		}
		disconnect("sftp");
		return output;
	}
	
	/**
	 * This method execute the shell on the remote server and writes data to the OutputStream
	 * @param out
	 * 		This method accepts out
	 * @throws Exception
	 * 		This method throws the Exception
	 * 
	 */
	@Deprecated
	public void executeShell(OutputStream out) throws Exception {

		BufferedReader reader = null;
		try{
			connect("exec");
			//create the excution channel over the session
			execChannel =  (ChannelExec)session.openChannel("exec");
			
			// Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
            InputStream in = execChannel.getInputStream();
            
            // Set the command that you want to execute
            String command = "sh "+remoteDir+shellFileName;
            execChannel.setCommand(command);
            // Execute the command
            execChannel.connect();
            
            //TODO : Need to re check the logic to make this class independent
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            byte[] output = null;
            while ((line = reader.readLine()) != null) {
            	logger.debug(line);
            	output = protoService.convertToProtobufFormat(line);
            	//out.write(line.getBytes());
                out.write(output);
                out.flush();
            }
         // Command execution completed here.

            // Retrieve the exit status of the executed command
            int exitStatus = execChannel.getExitStatus();
            if (exitStatus > 0) {
            	logger.error(EELFLoggerDelegator.errorLogger,"Remote script exec error! " + exitStatus);
            }
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeShell()", e);
			throw new ServiceException("Not able to execute shell File ","401", "Not able to execute shell File", e);
		} finally {
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeShell()", e);
				}
			}
		}
		disconnect("exec");
	}
	
	/**
	 * This method read file on the remote server and writes data to the OutputStream
	 * @param out
	 * 		This method accepts out
	 * @throws Exception
	 * 		This method throws the Exception
	 * 
	 */
	/**
	 * This method read file on the remote server and writes data to the OutputStream
	 * @param out
	 * 			This method accepts out
	 * @param filePath
	 * 			This method accepts filePath
	 * @throws Exception
	 * 			This method throws the Exception
	 */
	public void getData(OutputStream out, String filePath) throws Exception {

		BufferedReader reader = null;
		InputStream in = null;
		try{
			connect("sftp");
			in = sftpChannel.get(filePath);
			reader = new BufferedReader(new InputStreamReader(in));
            String line;
            byte[] output = null;
            while ((line = reader.readLine()) != null) {
            	logger.debug(line);
            	output = protoService.convertToProtobufFormat(line);
            	//out.write(line.getBytes());
                out.write(output);
                out.flush();
            }
		} catch (Exception e){
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
			throw new ServiceException("Not able to read remote File ","401", "Not able to read remote File", e);
		} finally {
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
				}
			}
			if(null != in){
				in.close();
			}
		}
		disconnect("sftp");
	}
}
