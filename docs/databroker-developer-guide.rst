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

=========================================
Design Studio Databroker Developer Guide
=========================================

1. Introduction
========================

         This is the developers guide to Design Studio Databroker. 

**1.1. What is Databroker\?**

	The DataBroker is a tool which falls under Design Studio. It retrieves the data from the different types Data Sources like Database, File systems (UNIX, HDFS Data Brokers, etc.), Router Data Broker, Zip Archives. Under Data Sources palette we can see these kind of data brokers.
	
	1.	Data Broker retrieves the data from passive Data Sources.

	2.	Converts the data into Protobuf format.

	3.	Provides the data to Models (via Model Connector).
	
	4.  Model Connector explicitly requests the Data Broker to retrieve the data from Data Source, receives the data in response, and provides the data to Models.

**1.2. Data Broker Types**

    1.  File Data Broker: Retrieves the data from CSV Files, JSON Files, or other files where the records (rows) of the file have a pre-defined structure. 
	
    2.  SQL Data Broker: Retrieves the data from SQL databases.
	
2. Architecture and Design
==================================

		The DataBroker Architecture is shown in below figure.
		
			1.	Includes databroker in the composite solution.
			
					.. image:: images/sql-csv/CompositeSolWithDataBroker.jpg
					    :alt: Backend Architecture diagram with DataBroker
						
			2.	Composite Solution with out DataBroker.			
			
					.. image:: images/sql-csv/CompositeSolWithoutDataBroker.jpg
					    :alt: Backend Architecture diagram without DataBroker
						
			3.	MP-PortalDeployer:High Level Deployment Sequence.
			
					.. image:: images/sql-csv/MP-PortalDeployer.jpg
					    :alt: Backend Architecture diagram of MP-PortalDeployer
						
			4.	DataBroker nested protobuf message to Linear format.
			
					.. image:: images/sql-csv/DBNestedProtobufMsgFormat.jpg
					    :alt: Design of DataBroker nested protobuf message to linear format
						
			5.	DataBroker nested protobuf message structure.
			
					.. image:: images/sql-csv/DataBrokerNestedProtobufMsgStructure.jpg
					    :alt: DataBroker output port nested protobuf message format
						 
			6.	DataBroker Source and Target tables mapping information, which is generated after user entering the script.
			
					.. image:: images/sql-csv/DBSourceTargetTableMappingDetails.jpg
					    :alt: DataBroker source and target tables mapping data


						 
2.1. High-Level Flow
-------------------------
	
2.2. Class Diagrams
-------------------------

2.3. Sequence Diagrams
-------------------------

3. Technology and Frameworks
===================================

	**List of the development languages, frameworks, tools, etc.**
		
		#. 	Java 8
		#.	Maven 3.X
		#.	SpringBoot 1.5.16
		#.	JCraft 0.1.53
		#. 	JUnit 4.12
		#. 	Jackson 2.7.5
		#.	Connectivity to Maven Central to download required jars
		
4. Project Resources
===========================

- Gerrit repo: `desing-studio/csvdatabroker <https://gerrit.acumos.org/r/#/admin/projects/design-studio>`_
- Gerrit repo: `desing-studio/sqldatabroker <https://gerrit.acumos.org/r/#/admin/projects/design-studio>`_
- `Jira <https://jira.acumos.org/browse/ACUMOS-50?jql=component%20%3D%20design-studio>`_  design-studio

5. Development set-up
==========================

5.1. Get the code
--------------------

	Clone the Repository in some user accessible directory, lets call this as <homeDirectory>
		
	git clone --depth 1 https://<username>@gerrit.acumos.org/r/a/design-studio
	
	 After successful clone, new directory <homeDirectory>/**design-studio** with following sub directories should get created.
	 
	  .. image:: images/design-studio_gerritRepo.jpg
	      :alt: design-studio gerritRepository structure
		  
5.2. Import Project in Eclipse
-----------------------------------

		After successful import, you should view in Project Explorer
		
			.. image:: images/sql-csv/Eclipse_csvdatabroker.jpg
			
			.. image:: images/sql-csv/Eclipse_sqldatabroker.jpg

6. How to Run
=====================
	
	 **Run the project as Spring Boot application:**
	 
	  Start SQLDataBroker and CSVDataBroker as Spring Boot application service and test the application through Swagger UI.
	   URL : http://localhost:8080/swagger-ui.html#/

7. How to Test
=====================
	
	**Using Junit**
		You can either run all OR the required Junit to test the code.
		

