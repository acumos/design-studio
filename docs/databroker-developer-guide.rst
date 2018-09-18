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

=============================
Design Studio Databroker Developer Guide
=============================

1.	Introduction
========================

         This is the developers guide to Design Studio Databroker. 

**1. What is Databroker\?**
	1.	Data Broker retrieves the data from passive Data Sources.

	2.	Converts the data into Protobuf format.

	3.	Provides the data to Models (via Model Connector).
	
	4.  Model Connector explicitly requests the Data Broker to retrieve the data from Data Source, receives the data in response, and provides the data to Models

**2.  Data Broker Types**
    1.  File Data Broker: Retrieves the data from CSV Files, JSON Files, or other files where the records (rows) of the file have a pre-defined structure. 
    2.  SQL Data Broker: Retrieves the data from SQL databases.

