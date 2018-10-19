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

==============================================
Design Studio Generic Data Mapper User Guide
==============================================

1. Target Users
=================

	"Modelers": community that has understanding of

	1.	The ML Models, the functions they perform

	2.	Components, interfaces, and operations supported by them

2. Overview
============

	This is a user’s guide that describes how to use the Design Studio tool : Generic Data Mapper.

2.1. What is Generic Data Mapper\?
-------------------------------------
	   
	Allow user to connect two ML model 'A' and 'B' where number of : output fields of model 'A' and input fields of model 'B' are same.  User is allowed to connect the fields of model 'A' to required field of model 'B'. The Datamapper performs data type transformations between Protobuf data types.
	To use Datamapper User should be well aware of the output value of each field of model 'A' and expected input value of each field of model 'B' to get desired final output.  
	
2.2. What does it do\?
------------------------
	   
	 1.	Maps data types between a pair of incompatible ports of the ML Models – map the data type of an output port to the data types of an input port.
	 
	 2.	Any output port of a ML Model can be connected to a Data Mapper, and the Data Mapper can be connected to any input port of the ML Model.
	 
     3.	Composition Rule: From the Design Studio composition perspective a Data Mapper can accept any inputs and produce any outputs, depending on the ML models that are connected to its input and output side. So its requirements and capability will be indicated any.  
	 
     4.	Data Mapper will perform transformation between basic Protobuf types only.
 

3. Architecture
================
Provide high-level information about the feature components and how it fits
into the Acumos platform. Detailed architecture should be part of the Developer Guide.

 Coming soon

4. How to Use Data Mapper 
================================

4.1. Onboarding Generic Data Mapper
-------------------------------------

		**a. Construct the Generic Data Mapper model :** Generic Data Model need to be on boarded similar to any other ML model.  Admin user need to do this one time activity per Acumos Instance. At run time, actual Data Mapper is constructed as per the details and injected into composite solution. To upload Generic Data Mapper user need GenericDataMapper.zip file. 
		Admin user can use any frame work model to construct Generic Data Mapper , for e.g. 
		
			1. Java based Generic Data mapper, GenericDataMapper.zip should contain following artifacts : 
				- modelpackage.zip 
					- GenericDataMapper.jar (any jar will work named as mentioned)
					- GenericModelService.jar (latest Model service jar compatible with Acumos environment is required)
					- protbuf-java-3.4.0.jar 
					- application.properties
					- modelCofig.properties 
				- metadata.json
				- default.proto
				
			2. H2O based Generic Data Mapper, GenericDataMapper.zip should contain following artifacts : 
				- modelpackage.zip 
					- application.properties
					- H2OModelService
					- GenericDataMapper.zip (any H2O model zip named as mentioned)
				- metadata.json 
				- default.proto
					
		
		**b. default.proto**: Following is the default.proto file required for Generic Data Mapper
			syntax = "proto3";

			message ANY {
			
			}

			service DataMapper {
			
			  rpc mapData (ANY) returns (ANY);
			  
			}
			
		**c. On boarding Generic Data Mapper :** Admin user can on board Generic Data Mapper same was as ML Model either through Web-Onboarding or CLI.  For more detaisl on onboarding model please refer **On Boarding User Guide**.
		
		**d. Publish Generic Data Mapper to public market place :** Once Generic Data Mapper is on boarded successfully, Admin user can public the it to public market place.  While publishing Admin user should set the model **Category** as **"Data Transfer"** and **TooKit Type** based on the framework/library used for construction GenericDataMapper.zip. 
		
		**e. Check availability of Generic Data Mapper :** After successfully published to Public Market place, Generic Data Mapper should be available in Design Studio UI under palette **"Data Transfrom Tools"** to all the users. 
		
4.2. Connecting incompatible ports using Generic Data Mapper
--------------------------------------------------------------
	
		Coming soon.