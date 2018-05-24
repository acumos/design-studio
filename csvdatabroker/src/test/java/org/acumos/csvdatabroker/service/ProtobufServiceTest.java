package org.acumos.csvdatabroker.service;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.when;
import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.ProtobufUtil;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.DBInputField;
import org.acumos.csvdatabroker.vo.DBMapInput;
import org.acumos.csvdatabroker.vo.DBMapOutput;
import org.acumos.csvdatabroker.vo.DBOTypeAndRoleHierarchy;
import org.acumos.csvdatabroker.vo.DBOutputField;
import org.acumos.csvdatabroker.vo.DataBrokerMap;
import org.acumos.csvdatabroker.vo.Protobuf;
import org.acumos.csvdatabroker.vo.ProtobufMessage;
import org.acumos.csvdatabroker.vo.ProtobufMessageField;
import org.acumos.csvdatabroker.vo.ProtobufService;
import org.acumos.csvdatabroker.vo.ProtobufServiceOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;


@RunWith(MockitoJUnitRunner.class)
public class ProtobufServiceTest {

	@InjectMocks
	private CSVDatabrokerServiceImpl service;
	
	@InjectMocks
	ProtobufServiceImpl protobufServiceImpl;
	
	Configuration conf = null;
	DataBrokerMap map = null;
	
	@Mock
	RemoteScriptExecutor executor; 
	
	@Mock
	Protobuf protobuf1;
	
	@Mock
	ProtobufMessage protobufMessage;
	
	@Mock
	DynamicSchema protobufSchema;
	
	@Mock
	ProtobufService services;
	
	@Mock
	ConfigurationService confService;
	
	@Mock
	ProtobufUtil protobufUtil;
	
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		map = new DataBrokerMap();
		map.setFirst_row(Constants.FIRST_ROW_CONTAINS_FIELDNAMES);
		
		
		conf.setData_broker_map(map);;
	}
	
	@Test
	public void processProtobuf() throws Exception {
		String protobufStr = "message DataFrame { repeated string mime_type = 1;\n} \n\" service Predictor { \"\n\"  rpc predict (AggregateData) returns (Prediction); rpc transform (DataFrame) returns (Prediction);}";		//protobufStr = String.format(protobufStr, "ok", true, "");
		
		Configuration conf = new Configuration();
		
		conf.setProtobufFile(protobufStr);
		protobufServiceImpl.processProtobuf(conf);
		
	}
	
	@Test
	public void processProtobuf1() throws Exception {
		String protobufStr = "service Predictor { rpc predict (AggregateData) returns (Prediction);\n}";		//protobufStr = String.format(protobufStr, "ok", true, "");
		
		Configuration conf = new Configuration();
		
		conf.setProtobufFile(protobufStr);
		protobufServiceImpl.processProtobuf(conf);
	}
	
	@Test
	public void getProtobuf() throws Exception {
		//Mockito.when(protobufServiceImpl.getProtobuf()).thenThrow(new ServiceException("No environment configuration found!  Please set the Environment configuration.","401", "Exception in getData()"));
		protobufServiceImpl.getProtobuf();
		
	}
	
	/*@Test
	public void convertToProtobufFormat() throws Exception {

		List<ProtobufServiceOperation> operations = new ArrayList<ProtobufServiceOperation>();
		ProtobufServiceOperation protobufServiceOperation = new ProtobufServiceOperation();
		List<String> inputMessageNames = new ArrayList<String>();
		inputMessageNames.add("test1");
		inputMessageNames.add("test2");
		List<String> outputMessageNames = new ArrayList<String>();
		outputMessageNames.add("test1");
		outputMessageNames.add("test2");
		protobufServiceOperation.setInputMessageNames(inputMessageNames);
		protobufServiceOperation.setName("sample");
		protobufServiceOperation.setOutputMessageNames(outputMessageNames);
		protobufServiceOperation.setType("Ts");
		ProtobufServiceOperation protobufServiceOperation1 = new ProtobufServiceOperation();
		List<String> inputMessageNames2 = new ArrayList<String>();
		inputMessageNames2.add("test1");
		inputMessageNames2.add("test2");
		List<String> outputMessageNames2 = new ArrayList<String>();
		outputMessageNames2.add("test1");
		outputMessageNames2.add("test2");
		protobufServiceOperation1.setInputMessageNames(inputMessageNames);
		protobufServiceOperation1.setName("sample");
		protobufServiceOperation1.setOutputMessageNames(outputMessageNames);
		protobufServiceOperation1.setType("Ts");
		operations.add(protobufServiceOperation);
		operations.add(protobufServiceOperation1);
		
		Protobuf protobuf  = new Protobuf();
		List<ProtobufMessage> messages = new ArrayList<ProtobufMessage>(); 
		ProtobufMessage protobufMessages = new ProtobufMessage();
		List<ProtobufMessageField> fields =  new ArrayList<ProtobufMessageField>();
		ProtobufMessageField field = new ProtobufMessageField();
		field.setName("demo1");
		field.setRole("repeated");
		field.setTag(1);
		field.setType("string");
		ProtobufMessageField field1 = new ProtobufMessageField();
		field1.setName("demo2");
		field1.setRole("repeated");
		field1.setTag(2);
		field1.setType("string");
		fields.add(field);
		fields.add(field1);
		protobufMessages.setName("Prediction");
		protobufMessages.setFields(fields);
		messages.add(protobufMessages);
		protobuf.setMessages(messages);
		protobuf.setSyntax("test");
		ProtobufService service = new ProtobufService();
		service.setName("Aggregator-1");
		service.setOperations(operations);
		protobuf.setService(service);
		
		Mockito.when(protobuf1.getService()).thenReturn(service);
		Mockito.when(services.getOperations()).thenReturn(operations);
		Mockito.when(protobuf1.getMessage("Prediction")).thenReturn(protobufMessages);
		Mockito.when(protobufMessage.getFields()).thenReturn(fields);
		protobufServiceImpl.convertToProtobufFormat("2");
		
	}*/
	
/*	@Test
	public void convertToProtobufFormat1() throws Exception {
		

		List<ProtobufMessage> messages = new ArrayList<ProtobufMessage>(); 
		ProtobufMessage protobufMessage1 = new ProtobufMessage();
		List<ProtobufMessageField> fields =  new ArrayList<ProtobufMessageField>();
		ProtobufMessageField field = new ProtobufMessageField();
		field.setName("demo1");
		field.setRole("repeated");
		field.setTag(1);
		field.setType("string");
		ProtobufMessageField field1 = new ProtobufMessageField();
		field1.setName("demo2");
		field1.setRole("repeated");
		field1.setTag(2);
		field1.setType("string");
		fields.add(field);
		fields.add(field1);
		protobufMessage1.setName("Prediction");
		protobufMessage1.setFields(fields);
		protobufMessage.setName("test");
		protobufMessage.setFields(fields);
		messages.add(protobufMessage);
		protobuf1.setMessages(messages);
		
		Configuration conf = new Configuration();
		conf = new Configuration();
		conf.setHost("0.0.0.0");
		conf.setPort(80);
		conf.setUserName("xyz");
		conf.setPassword("xyz");
		
		DataBrokerMap databrokerMap = new DataBrokerMap();
		databrokerMap.setScript("this is the script");
		databrokerMap.setCsv_file_field_separator(",");
		databrokerMap.setData_broker_type("SQLDataBroker1");
		databrokerMap.setFirst_row("Yes");
		databrokerMap.setLocal_system_data_file_path("localpath");
		//databrokerMap.setMap_action("mapaction");
		databrokerMap.setTarget_system_url("remoteurl");
		
		// DataBrokerMap Input List
		List<DBMapInput> dbmapInputLst = new ArrayList<DBMapInput>();
		DBMapInput dbMapInput = new DBMapInput();
		DBInputField dbInField = new DBInputField();
		
		dbInField.setChecked("Yes");
		dbInField.setMapped_to_field("1.2");
		dbInField.setName("Name of SourceField");
		dbInField.setType("Float");
		
		dbMapInput.setInput_field(dbInField);
		dbmapInputLst.add(dbMapInput);
		DBMapInput[] dbMapInArr = new DBMapInput[dbmapInputLst.size()];
		dbMapInArr = dbmapInputLst.toArray(dbMapInArr);
		databrokerMap.setMap_inputs(dbMapInArr);
		
		// DataBrokerMap Output List
		List<DBMapOutput> dbmapOutputLst = new ArrayList<DBMapOutput>();
		DBMapOutput dbMapOutput = new DBMapOutput();
		DBOutputField dbOutField = new DBOutputField();
		
		dbOutField.setName("demo1");
		dbOutField.setTag("1.2");
		
		List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
		DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy dboTypeAndRole1 = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;
		
		dboTypeAndRole.setName("DataFrame");
		dboTypeAndRole.setRole("Repeated");
		dboList.add(dboTypeAndRole);
		dboTypeAndRole1.setName("Prediction");
		dboTypeAndRole1.setRole("Repeated"); 
		dboList.add(dboTypeAndRole1);
		dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
		
		dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);
		
		dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);
		dbMapOutput.setOutput_field(dbOutField);
		dbmapOutputLst.add(dbMapOutput);
		
		DBMapOutput[] dbMapOutputArr = new DBMapOutput[dbmapOutputLst.size()];
		dbMapOutputArr = dbmapOutputLst.toArray(dbMapOutputArr);
		databrokerMap.setMap_outputs(dbMapOutputArr);
		
		
		conf.setData_broker_map(databrokerMap);
		
		DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
		schemaBuilder.setName("PersonSchemaDynamic.proto");
		
		MessageDefinition msgDef = MessageDefinition.newBuilder("Prediction") // message Person
				.addField("optional", "int32", "id", 1)		// required int32 id = 1
				.build();

		schemaBuilder.addMessageDefinition(msgDef); 
		DynamicSchema schema = schemaBuilder.build();
		
		// Create dynamic message from schema
		DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Prediction"); 
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		DynamicMessage msg = msgBuilder
				.setField(msgDesc.findFieldByName("id"), 1)
				.build();
		
		System.out.println(msg.getAllFields());
		Mockito.when(protobuf1.getMessage("Prediction")).thenReturn(protobufMessage1);
		Mockito.when(protobufMessage.getFields()).thenReturn(fields);
		Mockito.when(protobufSchema.newMessageBuilder("Prediction")).thenReturn(msgBuilder);
		Mockito.when(confService.getConf()).thenReturn(conf);
		//Mockito.when(msgBuilder.addRepeatedField(msgDesc.findFieldByName("demo1"), 1));
		
		try{
			protobufServiceImpl.convertToProtobufFormat("Prediction","1");
		}catch(IllegalArgumentException ie){
			ie.printStackTrace();
		}
		
		}*/
	
	@Test
	public void readProtobufFormat() throws Exception {
		
		DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
		schemaBuilder.setName("PersonSchemaDynamic.proto");
		
		MessageDefinition msgDef = MessageDefinition.newBuilder("Prediction") // message Person
				.addField("required", "int32", "demo1", 1)		// required int32 id = 1
				.addField("required", "string", "name", 2)	// required string name = 2
				.addField("optional", "string", "email", 3)	// optional string email = 3
				.build();
		
		schemaBuilder.addMessageDefinition(msgDef);
		DynamicSchema schema = schemaBuilder.build();
		DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Prediction"); 
		Descriptor msgDesc = msgBuilder.getDescriptorForType();
		DynamicMessage msg = msgBuilder
				.setField(msgDesc.findFieldByName("demo1"), 1)
				.setField(msgDesc.findFieldByName("name"), "surya")
				.setField(msgDesc.findFieldByName("email"), "at@sis.gov.uk")
				.build();
		
		
		byte[] line = msg.toByteArray();
		
		Mockito.when(protobufSchema.newMessageBuilder("Prediction")).thenReturn(msgBuilder);
		Mockito.when(confService.getConf()).thenReturn(conf);
		//Mockito.when(DynamicMessage.parseFrom(msgDesc, line)).thenReturn(msg);
		
		protobufServiceImpl.readProtobufFormat("Prediction",line);
	}

}
