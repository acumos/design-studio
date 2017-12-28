package org.acumos.designstudio.ce.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.cdump.DataMap;
import org.acumos.designstudio.cdump.DataMapInputField;
import org.acumos.designstudio.cdump.DataMapOutputField;
import org.acumos.designstudio.cdump.MapInputs;
import org.acumos.designstudio.cdump.MapOutput;
import org.acumos.designstudio.cdump.Nodes;
import org.acumos.designstudio.cdump.Property;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@Service("GenericDataMapperServiceImpl")
public class GenericDataMapperServiceImpl implements IGenericDataMapperService {

	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GenericDataMapperServiceImpl.class);
	
	@Autowired
	ConfigurationProperties confprops;
	
	@Autowired
	Properties props;
	
	private static String PROTOBUF_TEMPLATE_NAME = "Protobuf_Template.txt";
	
	private String path = null;
	
	/*
	
	private String packagepath;
	
	private String className;
	
	
	public GenericDataMapperServiceImpl(){
		packagepath = props.getPackagepath();
		className = props.getClassName();
	}*/
	
	@Override
	public String createDeployGDM(Cdump cdump, String userId) throws ServiceException {
		String dockerImageURI = null;
		boolean result = false;
		logger.debug(EELFLoggerDelegator.debugLogger, "------ createProtobuf() : Begin -------");
		
		//1. Create Proto buf file for cdump 
		result = createProtobufFile(cdump, userId);
		
		if(result){
			//2. generate and compile the java code as per the protobuf file.
			result = generateProtoJavaCode(userId);
		} else {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createDeployGDM() ");
			throw new ServiceException("Not able to create Protobuf File ", props.getSolutionErrorCode(),
					"Not able to Generate & compile  Proto Java Code ");
		}
		
		if(result){
			//3. Generate the MappingDetails as per the cdump.
			result = generateMappingDetails(cdump);
		} else {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createDeployGDM() ");
			throw new ServiceException("Not able to Generate & compile  Proto Java Code ", props.getSolutionErrorCode(),
					"Not able to Generate & compile  Proto Java Code ");
		}
		
		String jarPath = null;
		if(result){
			//4. Create new the GDMJar 
			jarPath = createNewGDMJar(userId);
		} else {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createDeployGDM() ");
			throw new ServiceException("Not able to Generate Field Mapping Details ", props.getSolutionErrorCode(),
					"Not able to Generate Field Mapping Details ");
		}
		
		if(null != jarPath){
			//5. Create Docker Image
			//dockerImageURI = createDockerImage(jarPath);
		} else {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createDeployGDM() ");
			throw new ServiceException("Not able to create Generic Data Mapper Jar", props.getSolutionErrorCode(),
					"Not able to create Generic Data Mapper Jar");
		}
		
		logger.debug(EELFLoggerDelegator.debugLogger, "------ createProtobuf() : End -------");
		return dockerImageURI;
	}

	private String createNewGDMJar(String userId) throws ServiceException {
		
		String result = null;
		path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		String libPath = confprops.getLib();
		String gdmJarName = libPath + props.getGdmJarName();
		UUID id = UUID.randomUUID();
		String tempJarName = path + id.toString() + "_" + props.getGdmJarName();
		
		String fieldMappingJarEntryName = "BOOT-INF/classes/FieldMapping.json";
		String DataVOClassEntryName = "BOOT-INF/classes/org/acumos/vo/";
		
		File jarFile = new File(gdmJarName);
		File tempJarFile = new File(tempJarName);
		JarFile jar = null;
		JarOutputStream tempJar = null;
		
		// Allocate a buffer for reading entry data.
        byte[] buffer = new byte[1024];
        int bytesRead;
		
		boolean jarUpdated = false;
		try {
			
			//Create the temp jar 
			tempJar = new JarOutputStream(new FileOutputStream(tempJarFile));
			
	         
	         //add the FieldMapping file 
	         addFieldMapping(fieldMappingJarEntryName, tempJar);
	         
	         //add DavaVO.class file
	         List<String> dataVOEntryList = addDataVOClasses(DataVOClassEntryName,tempJar);
             JarEntry entry = null;
           //Open the original jar 
 			jar = new JarFile(jarFile);
          // Loop through the jar entries and add them to the temp jar,
             // skipping the entry that was added to the temp jar already.

             for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
                // Get the next entry.

                 entry = (JarEntry) entries.nextElement();

                // If the entry has not been added already, add it.
                 
                if (! entry.getName().equals(fieldMappingJarEntryName) && !dataVOEntryList.contains(entry.getName())) {
                   // Get an input stream for the entry.
                
                   InputStream entryStream = jar.getInputStream(entry);

                   // Read the entry and write it to the temp jar.

                   tempJar.putNextEntry(entry);

                   while ((bytesRead = entryStream.read(buffer)) != -1) {
                      tempJar.write(buffer, 0, bytesRead);
                   }
                }
             }
             jarUpdated = true;
             result = tempJarName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "IOException in updateGDMJar() ");
			throw new ServiceException("Failed to create Generic Data Mapper Jar because of IOException", props.getSolutionErrorCode(),
					"Failed to create Generic Data Mapper Jar because of IOException");
		} /*catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in updateGDMJar() ");
			throw new ServiceException("Failed to create Generic Data Mapper Jar because of Exception", props.getSolutionErrorCode(),
					"Failed to create Generic Data Mapper Jar because of Exception");
		}*/
		finally {
            try {
            	tempJar.close();
            	jar.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
		return result;
	}
	
	private List<String> addDataVOClasses(String dataVODirEntryName, JarOutputStream tempJar) throws ServiceException {
		List<String> dataVOClassEntries = new ArrayList<String>();
		String voDirPath = path + props.getTarget()+ props.getPackagepath();
		File vodir = new File(voDirPath);
		File[] VOlist = vodir.listFiles();
		
		FileInputStream fileInputStream = null;
		String VOClass = null;
        byte[] buffer = new byte[1024];
        int bytesRead;
		
		try {
			for(File file : VOlist) {
				if(file.isFile() && file.getName().endsWith(".class")) {
					VOClass = file.getName();
					dataVOClassEntries.add(dataVODirEntryName + VOClass);
					fileInputStream = new FileInputStream(voDirPath + VOClass);
					// Create a jar entry and add it to the temp jar.
			           
			        JarEntry entry = new JarEntry(dataVODirEntryName + VOClass);
			        entry.setTime(vodir.lastModified());
			        tempJar.putNextEntry(entry);
			        
			     // Read the file and write it to the jar.

			        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
			           tempJar.write(buffer, 0, bytesRead);
			        }
			        System.out.println(entry.getName() + " added.");
				}
			}
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "FileNotFoundException in addDataVOClasses() ");
			throw new ServiceException("FileNotFoundException : Failed to add DataVO Classes to GDM Jar", props.getSolutionErrorCode(),
					"Failed to add DataVO Classes to GDM Jar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "IOException in addDataVOClasses() ");
			throw new ServiceException("IOException : Failed to add DataVO Classes to GDM Jar", props.getSolutionErrorCode(),
					"Failed to add DataVO Classes to GDM Jar");
		} finally {
			try {
            	if(null != fileInputStream)
            		fileInputStream.close();
            	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataVOClassEntries;
	}

	private void addFieldMapping(String fieldMappingjarEntryName, JarOutputStream tempJar) throws ServiceException {
		String fieldMappingFileName = path + props.getTarget()+ props.getFieldMapping() + ".json";
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fieldMappingFileName);
			// Allocate a buffer for reading entry data.
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        
	        // Create a jar entry and add it to the temp jar.
	           
	        JarEntry entry = new JarEntry(fieldMappingjarEntryName);
	        tempJar.putNextEntry(entry);
	        
	        // Read the file and write it to the jar.

	        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
	           tempJar.write(buffer, 0, bytesRead);
	        }
	        
	        System.out.println(entry.getName() + " added.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "FileNotFoundException in addFieldMapping() ");
			throw new ServiceException("FileNotFoundException : Failed to add FieldMapping.json to GDM Jar", props.getSolutionErrorCode(),
					"Failed to add FieldMapping.json to GDM Jar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "IOException in addFieldMapping() ");
			throw new ServiceException("IOException : Failed to add FieldMapping.json to GDM Jar", props.getSolutionErrorCode(),
					"Failed to add FieldMapping.json to GDM Jar");
		} finally {
            try {
            	if(null != fileInputStream)
            		fileInputStream.close();
            	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
		
	}
	
	private void createGDMJar(String userId) {
		path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		
		String protobufFileName = props.getProtobufFileName();
		boolean result = false;
		String cmd;
		int exitVal = -1;

		//cmd = "jar cvf " + "D:/VS00485966/ATT/Cognita/Temp/output/123456/test.jar" + " -C D:/VS00485966/ATT/Cognita/Temp/output/123456/target/classes ."; 
		cmd = "jar cvf " + path + "test.jar" + " -C " + path + props.getTarget() + " . ";  
		try {
			exitVal = DSUtil.runCommand(cmd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		if (exitVal != 0){
			logger.error("Failed to construct jar");
		}
		
	}
	
	
	private boolean generateMappingDetails(Cdump cdump) {
		boolean result = false;
		List<Nodes> nodes = null;
		DataMap dataMap = null;
		int propLength = 0;
		nodes = cdump.getNodes();
		for(Nodes node : nodes){
			Property[] properties = node.getProperties();
			if(null != properties && properties.length > 0 && null == dataMap){
				propLength = properties.length;
				for(int i = 0 ; i < propLength; i++){
					dataMap = properties[i].getData_map();
					if(null!=dataMap){
						break;
					}
				}
			}
		}
		
		if(null!= dataMap){
			Gson gson = new Gson();
			String payload = gson.toJson(dataMap);
			String targetPath = path + props.getTarget();
			File targetDir = new File(targetPath);
			if(!targetDir.exists()){
				targetDir.mkdirs();
			}
			DSUtil.writeDataToFile(targetPath, props.getFieldMapping(), "json", payload);
			result = true;
		}
		return result;
	}
	
	private boolean generateProtoJavaCode(String userId) {
		path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		
		String protobufFileName = props.getProtobufFileName();
		boolean result = false;
		String cmd;
		int exitVal = -1;

		cmd = "protoc -I=" + path + " --java_out=" + path + " " + path + protobufFileName + ".proto";

		try {
			exitVal = DSUtil.runCommand(cmd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		if (exitVal != 0){
			logger.debug("Failed to generate java code");
		}
		else {
			logger.debug("----------------Compiling java code-----------");
			result = compileProtoJavaCode();
			if(result){
				//DSUtil.rmdir(new File(path+"org/"));
			}
		}
		
		return result;
		
	}
	
	
	private boolean compileProtoJavaCode() {
		boolean result = false;
		//String buildPath;
		//String protoJavaRuntimeJar;
		String cmd;
		int exitVal = -1;

		String protobufjarpath = confprops.getLib() + props.getProtobufjar();
		String targetPath = path + props.getTarget();
		String packagepath = props.getPackagepath();
		String className = props.getClassName();
		
		File targetDir = new File(targetPath);
		if(targetDir.exists()){
			targetDir.delete();
		}
		
		targetDir.mkdirs();
		cmd = "javac -cp " + protobufjarpath + " " + path + packagepath + className + ".java"
				+ " -d " + targetPath;

		try {
			exitVal = DSUtil.runCommand(cmd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		if (exitVal != 0){
			logger.debug("Proto Code Compilation Failed...");
		} else {
			logger.debug("Proto Code compilation Successfull...");
			result = true;
		}
		return result;
	}
	
	private boolean createProtobufFile(Cdump cdump, String userId) throws ServiceException {
		//Get file from resources folder
		
		boolean result = false;
		StringBuilder inputMessage = new StringBuilder();
		StringBuilder outputMessage = new StringBuilder();
		String line = null;
		
		ClassLoader classLoader = getClass().getClassLoader();
		String protobufDetails = null;
		List<Nodes> nodes = null;
		DataMap dataMap = null;
		DataMapInputField inputfield = null;
		DataMapOutputField outputfield = null;
		int propLength = 0;
		try {
			
			
			nodes = cdump.getNodes();
			for(Nodes node : nodes){
				Property[] properties = node.getProperties();
				if(null != properties && properties.length > 0 && null == dataMap){
					propLength = properties.length;
					for(int i = 0 ; i < propLength; i++){
						dataMap = properties[i].getData_map();
						if(null!=dataMap){
							break;
						}
					}
				}
			}
			if(null != dataMap ){
				MapInputs[] inputs = dataMap.getMap_inputs();
				MapOutput[] outputs = dataMap.getMap_outputs();
				
				int inputLength = inputs.length;
				int outputLength = outputs.length;
				
				//TODO : Extract the below code into method
				//TODO : current logic does not support the nested messages.  
				for(int i = 0 ; i < inputLength ; i++){
					DataMapInputField[] inputfields = inputs[i].getInput_fields();
					int inputfieldlen = inputfields.length;
					for(int j = 0 ; j < inputfieldlen ; j++	){
						inputfield = inputfields[j];
						line = "   " + inputfield.getRole() + " " + inputfield.getType() + " " + inputfield.getName() + " = " + inputfield.getTag() + ";";
						inputMessage.append(line);
						inputMessage.append("\n");
						
					}
				}
				
				//TODO : Extract the below code into method
				for(int i = 0 ; i < outputLength ; i++){
					DataMapOutputField[] outputfields = outputs[i].getOutput_fields();
					int outputfieldlen = outputfields.length;
					for(int j = 0 ; j < outputfieldlen ; j++){
						outputfield = outputfields[j];
						line = "   " + outputfield.getrole() + " " + outputfield.gettype() + " " + outputfield.getname() + " = " + outputfield.gettag() + ";";
						outputMessage.append(line);
						outputMessage.append("\n");
						
					}
				}
				
				String packagepath = props.getPackagepath();
				String className = props.getClassName();
				String packageName = packagepath.replace("/", ".");
				String end = String.valueOf(packageName.charAt(packageName.length()-1));
				packageName =  (String.valueOf(packageName.charAt(packageName.length()-1))).equals(".")? packageName.substring(0, packageName.length()-1) : packageName;
				
				protobufDetails = DSUtil.readFile(classLoader.getResource(PROTOBUF_TEMPLATE_NAME).getFile());
				protobufDetails = protobufDetails.replace("packageName", packageName);
				protobufDetails = protobufDetails.replace("className", className);
				protobufDetails = protobufDetails.replace("inputmessage", inputMessage);
				protobufDetails = protobufDetails.replace("outputmessage", outputMessage);
				
				path = DSUtil.createCdumpPath(userId, confprops.getToscaOutputFolder());
				DSUtil.writeDataToFile(path, props.getProtobufFileName(), "proto", protobufDetails);
				result = true;
				
			} else {
				logger.error(EELFLoggerDelegator.errorLogger, "Exception in createProtobufFile()");
				throw new ServiceException("---  Exception in createProtobufFile() ----", props.getSolutionErrorCode(),
						"No Generic Data Mapper found");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createProtobufFile()", e);
			throw new ServiceException("Exception in createProtobufFile()", props.getSolutionErrorCode(),
					props.getSolutionErrorDesc());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in createProtobufFile()", e);
			throw new ServiceException("Exception in createProtobufFile()", props.getSolutionErrorCode(),
					props.getSolutionErrorDesc());
		} finally {
			
							
		}
		
		return result;
	}
	
	
	
	/*
	
	 public static void main1(String[] args) {
		IGenericDataMapperService service = new GenericDataMapperServiceImpl();
		
		ObjectMapper mapper = new ObjectMapper();
		String userId = "12234";
		try {
			Cdump cdump = mapper.readValue(new File("D:/VS00485966/ATT/Cognita/Studio/Sprint6C/CDUMP.json"), Cdump.class);
			service.createDeployGDM(cdump, userId);
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	public static void main2(String[] args) {
		String path = "";
		
		
		boolean result = false;
		String cmd;
		int exitVal = -1;

		cmd = "jar cvf " + "D:/VS00485966/ATT/Cognita/Temp/output/123456/test.jar" + " -C D:/VS00485966/ATT/Cognita/Temp/output/123456/target/classes/ .";  
		//cmd = "D:/VS00485966/ATT/Cognita/Temp/output/123456/target/classes/ && jar -cvf test.jar *";
		try {
			exitVal = DSUtil.runCommand(cmd);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		if (exitVal != 0){
			logger.debug("Failed to generate jar");
		}
		
	}
	
	public static void main(String[] args) {
		GenericDataMapperServiceImpl service = new GenericDataMapperServiceImpl();
		service.updateGDMJarTest("123456");
	}
	
	
	public void updateGDMJarTest(String userId){
		String path = "D:/VS00485966/ATT/Cognita/Temp/output/123456/";
		String libPath = "D:/VS00485966/ATT/Cognita/Temp/lib/";
		
		String gdmJarName = libPath + "gdmservice-0.0.1-SNAPSHOT.jar";
		String tempJarName = path + "gdmservice-0.0.1-SNAPSHOT.jar";
		
		String fieldMappingJarEntryName = "BOOT-INF/classes/FieldMapping.json";
		String dataVODirEntryName = "BOOT-INF/classes/org/acumos/vo/";
		
		File jarFile = new File(gdmJarName);
		File tempJarFile = new File(tempJarName);
		JarFile jar = null;
		JarOutputStream tempJar = null;
		FileInputStream file = null;
		String fieldMappingFileName = path + "target/classes/"+ "FieldMapping" + ".json";
		boolean jarUpdated = false;
		try {
			
			//Open the original jar 
			jar = new JarFile(jarFile);
			
			
			//Create the temp jar 
			tempJar = new JarOutputStream(new FileOutputStream(tempJarFile));
			// Allocate a buffer for reading entry data.
	         byte[] buffer = new byte[1024];
	         int bytesRead;
	         
	         //Update the FieldMapping file 
	         
	         file = new FileInputStream(fieldMappingFileName);
	         // Create a jar entry and add it to the temp jar.
             JarEntry entry = new JarEntry(fieldMappingJarEntryName);
             tempJar.putNextEntry(entry);
             
             // Read the file and write it to the jar.

             while ((bytesRead = file.read(buffer)) != -1) {
                tempJar.write(buffer, 0, bytesRead);
             }
             
             System.out.println(entry.getName() + " added.");
             
           //add DavaVO.class file
	         List<String> dataVOEntryList = addDataVOClassesTest(dataVODirEntryName, tempJar);
             
             
             
             
          // Loop through the jar entries and add them to the temp jar,
             // skipping the entry that was added to the temp jar already.

             for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
                // Get the next entry.

                 entry = (JarEntry) entries.nextElement();

                // If the entry has not been added already, add it.

                if (! entry.getName().equals(fieldMappingJarEntryName)  && !dataVOEntryList.contains(entry.getName())) {
                   // Get an input stream for the entry.
                
                   InputStream entryStream = jar.getInputStream(entry);

                   // Read the entry and write it to the temp jar.

                   tempJar.putNextEntry(entry);

                   while ((bytesRead = entryStream.read(buffer)) != -1) {
                      tempJar.write(buffer, 0, bytesRead);
                   }
                } else {
                	System.out.println("not addeind : " + entry.getName());
                }
             }
             jarUpdated = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
            try {
            	if(null != file)
				file.close();
            	tempJar.close();
            	jar.close();
            	
                System.out.println(gdmJarName + " jar closed.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
		if (jarUpdated) {
	         
	         System.out.println(gdmJarName + " updated.");
	      }
		
		
	}
	
	
	private List<String> addDataVOClassesTest(String dataVODirEntryName, JarOutputStream tempJar) {
		String basepath = "D:/VS00485966/ATT/Cognita/Temp/output/123456/";
		String libPath = "D:/VS00485966/ATT/Cognita/Temp/lib/";
		
		List<String> dataVOClassEntries = new ArrayList<String>();
		String voDirPath = basepath + "target/classes/"+ "org/acumos/vo/";
		File vodir = new File(voDirPath);
		File[] VOlist = vodir.listFiles();
		
		FileInputStream fileInputStream = null;
		String VOClass = null;
        byte[] buffer = new byte[1024];
        int bytesRead;
		
		try {
			for(File file : VOlist) {
				if(file.isFile() && file.getName().endsWith(".class")) {
					VOClass = file.getName();
					dataVOClassEntries.add(dataVODirEntryName + VOClass);
					fileInputStream = new FileInputStream(voDirPath + VOClass);
					// Create a jar entry and add it to the temp jar.
			           
			        JarEntry entry = new JarEntry(dataVODirEntryName + VOClass);
			        entry.setTime(vodir.lastModified());
			        tempJar.putNextEntry(entry);
			        
			     // Read the file and write it to the jar.

			        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
			           tempJar.write(buffer, 0, bytesRead);
			        }
			        System.out.println(entry.getName() + " added.");
				}
			}
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
            	if(null != fileInputStream)
            		fileInputStream.close();
            	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataVOClassEntries;
	}
*/
}
