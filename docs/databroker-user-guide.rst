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

===========================
DataBroker User Guide
===========================

1. Target Users
==================

	The Data Broker is targeted towards user: 
	
	1.	To provide the data to Models via Model Connectors.

2. Overview
===============

	This is a user’s guide that describes how to use the DataBroker.
	
2.1. What is DataBroker?
----------------------------
	
		1.	Data Broker is a tool which falls under design studio, which is there in the Data Sources palette.
	
		2.	Data Broker retrieves the data from passive data sources and converts it into protobuf format.
	
		3.	Data Broker provides the data to the Models via the Model Connector, Models Connector explicitly request the Data Broker to retrieve the data from Data Sources, receives the data in response, and provides the data to Models.
	
2.2. Types of DataBrokers?
------------------------------
	
	There are many types of DataBrokers.
	
		1.	DataBase DataBroker (SQL DataBroker)
		2.	FileSystem DataBroker (HDFS File System, UNIX, Hadoop, CSV, JSON)
		3.	Network DataBroker (Router, Switch, etc.)
		4. 	Zip Archive DataBroker.
	
3. Architecture
===================
	
	The DataBroker Architecture with high level information will be there in the Developer Guide.

4. How to use DataBroker
==============================
	
	Before going to how to use DataBroker, will go to how to On-Board the DataBroker.
	
4.1. How to On-Board the DataBroker?
------------------------------------------
	
		1.	The Data Broker is a Design Studio tool. Unlike other ML Models that have a pre-defined Protobuf file associated with them, the Data Broker does not have a pre-defined Protobuf file associated with it.
		
		2.	Onboard the GenericDataBroker model which has been previously downloaded from an Acumos platform or obtained from somewhere else (where we can stage this is TBD), with files model.zip, metadata.json, and default.proto (NOT model.proto as normal)
		
		3.	While Onboarding the databroker, we can choose the ways of On-Board it, there are two ways,
			
			i.	CLI
			
			ii.	Web Based.
		
		4.	On-Boarding via Web based: 

			i. Upload Model bundle.
			
			While uploading the model bundle make sure that model.zip, metadata.json, default.proto should be there in that bundle which is of zip format. After successfully uploading, click on the On-Board Model.
			
			In this process many steps it will perform like creation of micro service, Dockerization, Add to Repository, Creation of TOSCA and finally it will show On-Boarded or not.
			
		5.	After completion of On-Boarding go to My Model tab then publish the GenericDataBroker model to the public/company marketplace, setting the values Model Category: Data Sources & Toolkit Type: Data Broker
		
		6.	Now the DataBroker will be there in Design Studio under the DataSources palette like shown in below.
		
			.. image:: images/SQL-CSV/GenericDataBroker.jpg
			
4.2. How to work with DataBroker?
---------------------------------------
		
		1.	Create/load a solution in Design Studio.
		
		2.	Select the GenericDataBroker (latest version) from the list of "Data Sources" and drag onto the canvas. This will shown like below.
		
			.. image:: images/SQL-CSV/DataBrokeronCanvas.jpg		
			
		3.	Connect the GenericDataBroker output to the input of the first model.
		
		4.	Select the "S" port at the top of the GenericDataBroker node, which will bring up the "Script" dialog popup.
			
		5.	On click of "s" port of the Data Broker to allow input a free form text – a set of (one or more) Database SQL statements or a set of FileSystem scripts. [Note: these scripts are passed on to the Data Broker after they get deployed by the MP – Portal.] Which is will be shown as below.
		
			.. image:: images/SQL-CSV/DataBrokerScriptPort.jpg
			
		6.	Script Entry UI has the ability to to edit (add, delete, modify, copy and paste) the script.
		
		7.	Design Studio has the capability of storing the script and validating from UI after click on the Done button then save this into back end CDUMP file. When reload the solution on design canvas, design studio has the capability of reload the saved script from cdump file.
			
			The current databroker supports csvDatabroker and sqlDatabroker. The user will need to enter following details:
			
			**For CSV Databroker:**
			
				The image for CSV DataBroker is like below.
				
					.. image:: images/SQL-CSV/CSVDBScriptPort.jpg
					
				a) Data Broker Type = CSV
			
				b) File Path = Will be populated during deployment

				c) Enter Script = Will be populated during deployment 

				d) Choose File = select a sample CSV file with your test data from the local machine, which has the format e.g. for a model that takes two double values:

					f1,f2
					2.0,4.0

				e) Select "First row contains field names" or "First row contains data" based on the file uploaded.

				f) Click "Done"
			
			**For SQL Databroker:**
			
				The image for SQL DataBroker is like below.
				
					.. image:: images/SQL-CSV/SQLDBScriptPort.jpg
		
				a) Data Broker Type = SQL
			
				b) JDBC URL = Greyed out

				c) Enter Script = Greyed out

				d) Choose File = select a file with CREATE TABLE schema loaded in it, in order to parse the table contents for mapping. (Table Name and table field details	are retrieved from the schema.)

				e) Select the jdbc driver name from the dropdown which supports the file uploaded. (Currently, we only support mysql)
			
				f) Enter the database name in which the table is present. It will be shown as below.
				
					.. image:: images/SQL-CSV/SQLDBwithFile.jpg
					
				g) Click "Done"
				
		8.	Auto – Generate Source Table from Script:
		
			a.	Once the user clicks on "Done" button, if its valid then the file contents will be parsed, field names will be extracted, the source table will get auto-populated which can be viewed by clicking on the "Mapping table" button present in the properties, else it will give the helpful error messages. The mapping table in the properties panel will be shown like below.
			
				.. image:: images/SQL-CSV/PropertiesPanel.jpg
				
		9.	The Target table will generate by using the single protobuf file of one of its input message. After connecting the ML Model to the Data Broker
		output, click on the output port of the data broker, then in the property box will display the protobuf input message of the ML Model only.
		
		10.	Auto – Generate Target Table from Protobuf File:
		
			a.	Once the output of Databroker is connected to the input of ML model, databroker acquires its message signature and generates the target table as per the protobuf specification.
				
			b.	Target table contains the N number of rows, where N is number of basic field types in the protobuf message those are Basic field name and
				Basic field type.
				
		11.	On the right, under "Properties" select "Mapping Table", and in the resulting "Mapping Table" dialog: The mapping table in the properties panel will be shown like below.
		
				.. image:: images/SQL-CSV/DBMappingTable.jpg
			
			a) Select each source field from the table, select the field type from the drop down, and the target tag to be mapped to the field. The target tags are captured from the protobuf specification of model that is connected to databroker. 

			b) When you have mapped all fields, select "Done". Which will shown like below.
				
				.. image:: images/SQL-CSV/SourceTableSelection.jpg
				
			c)	Design Studio will facilitate to save the Source Table to Target Table mappings in the backend cdump file when click the Save button on the design canvas – by using modifyNode() API.
				
			d) Design Studio have the capability of retrieving the saved mappings from the backend cdump file and display the mappings in the Property Box when reload solution in the design canvas.
			
		13.	Above the canvas, select the 'Save' button and enter the details of the solution, in order to save the solution. (This will be saved in "My Solutions" area).
		
		14.	Above the canvas, Select the "Validate" button to generate the blueprint.
		
		15.	If validation is successful, then the deploy button would be enabled. On click of any of the cloud platform, you will be redirected to "manage my model" -> "Deploy to cloud"
		
		At this point, this model should be usable with databroker when deployed.
		
	
4.3. How to Dockerize the DataBroker?
--------------------------------------------
	
		The Data Broker is implemented as a java jar package.
		
		After the composite solution is successfully validated in the Design Studio, the Composition Engine performs the following functions:
		
		1.	Retrieve the code of the Data Broker from a specific location in Nexus repository (Mukesh to let us know the location).
		
		2.	Create the Protobuf Wrapper for the Data Broker based on the output message that the Data Broker acquires at its output port when it is connected to an input port of an ML Model in the Design Studio. The input message is of Protobuf type string. This Wrapper converts:
			
			a.	From Java to Protobuf types for the outgoing messages.

			b.	From Protobuf to Java types for the incoming messages. 
			
		3.	Create the jar file of the Data Broker. 
		
		4.	Convert the jar to Microservices.
		
		5.	Create the Docker Image of the Data Broker Microservice from its jar file.
		
		6.	Store the Docker image of the Data Broker in the Docker repository (or Nexus repository). 
		
		7.	Store the location of the docker image in the TGIF.json of the Data Broker.
		
		8.	Store the location of the docker image in the Blueprint.json file (after successful validation). 
		
		
		
		