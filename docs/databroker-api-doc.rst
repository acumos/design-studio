.. ===============LICENSE_START=======================================================
.. Acumos
.. ===================================================================================
.. Copyright (C) 2017-2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
.. ===================================================================================
.. This Acumos documentation file is distributed by AT&T and Tech Mahindra
.. under the Creative Commons Attribution 4.0 International License (the "License");
.. you may not use this file except in compliance with the License.
.. You may obtain a copy of the License at
..  
..      http://creativecommons.org/licenses/by/4.0
..  
.. This file is distributed on an "AS IS" BASIS,
.. WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
.. See the License for the specific language governing permissions and
.. limitations under the License.
.. ===============LICENSE_END=========================================================

====================================================================
Design Studio Databroker Application Programming Interfaces
====================================================================


API 
====
1.	Set the environment configuration
-------------------------------------------

	**Operation Name**
		configDB
		
	**Trigger**
	
		This API is invoked by Deployer/deploy.sh script to set the configuration details.  
		For SQL Databroker fillowing fields are mandatory : 
		
		  "database_name": "string",
		  
		  "jdbc_driver_data_source_class_name": "string",
		  
		  "table_name": "string", 
		  
		  "target_system_url": "string", //value should be JDBC URL.
		  
		For CSV Databroker following fields are mandatory : 
		
		  "csv_file_field_separator": "string", 
		  
		  "first_row": "string",
		  
		  "target_system_url": "string", // value will be file path. (currently supporting local file only.)
		  
	**Request**
	
		{
		
		  "csv_file_field_separator": "string", //mandatory for csv databroker. 
		  "data_broker_type": "string", //mandatory
		  "database_name": "string", //mandatory for SQL databroker 
		  "first_row": "string", //mandatory for csv databroker 
		  "jdbc_driver_data_source_class_name": "string", //mandatory for SQL databroker 
		  "local_system_data_file_path": "string",
		  "map_inputs": [
		  
			{
			
			  "input_field": {
			  
				"checked": "string",
				"mapped_to_field": "string",
				"name": "string",
				"type": "string"
				
			  }
			  
			}
			
		  ],
		  
		  "map_outputs": [
		  
			{
			
			  "output_field": {
			  
				"name": "string",
				"tag": "string",
				"type_and_role_hierarchy_list": [
				
				  {
				  
					"name": "string",
					"role": "string"
					
				  }
				  
				]
				
			  }
			  
			}
			
		  ],
		  
		  "password": "string", // DB password, will be set by deploy.sh 
		  "protobufFile": "string",
		  "script": "string",
		  "table_name": "string", //mandatory for SQL databroker 
		  "target_system_url": "string", // file path in case of CSV databroker and JDBC URL in case of SQL databroker. 
		  "user_id": "string" // DB username, will be set by deploy.sh 
		  
		}
		
	**Response**
	
		**Success**
		
		{
		
		  "status": 200,
		  "message": "Environment configured successfully !!!"
		  
		}
		
		**Error**
		
		{
		
		  "timestamp": ,
		  "status": 400,
		  "error": "Bad Request",
		  "exception": "Exception details",
		  "message": "Error Message",
		  "path": "/configDB"
		  
		}
		
	**Behavior**
	
		Sets the below details required by Databroker for fetching, converting into protobuf format and pass it on to the Model connector. 
		* The host and port of the machine where the File or Database is located
		* The login credentials (user Id, password) of the Target System (prompt by deploy.sh to the user)
		* The “data_broker_map” section of the Data Broker node from the Blueprint.json file
		* The string version of Protobuf file contents 

	
2. Get Data 
------------

	**Operation Name**

	  pullData

	  This operation fetch a record from the specified resource ( .csv or SQL DB)

	**Trigger**

	  This operation is called by **Model connector** to fetch the data row. 

	**Request**

	  {}

	**Response**

	  {
	  
		  "Protobuf formatted data"
		  
	  }

	**Behavior** 
	
	  Fetch the data row from the specified source and converts it into protobuf format as per the configuration details set. 
	  
	