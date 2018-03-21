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
Design Studio Developer Guide
=============================

1.	Introduction
========================

         This is the developers guide to Design Studio. 

**1.1 What is Design Studio\?**
	The Design Studio is a web based tool to:

	1.	Create machine learning applications, hereafter referred to as composite solutions, out of the basic building blocks – the individual Machine Learning (ML) models contributed by the open source user community.

	2.	Validate the composite solutions.

	3.	Generate the blueprint of the composite solution for deployment on the target cloud.
	
**1.2	Target Users**
	This guide is targeted towards the open source user community that:

	1.	Intends to understand the backend functionality of the Design Studio.
	
	2.	Intends to contribute code to enhance the functionality of the Design Studio.
	
**1.3	Assumptions**
		It is assumed that the ML Models contributed by the open source community:
		
		1.	Provide the basic request response style of communication.
		
		2.	Can be converted in Microservices
		
		3.	Are capable of communicating via Http REST mechanism. 
		
		4.	Are developed in Java, Python 3.0, R and sourced from toolkits such as Scikit, TensorFlow, H2O, and RCloud.


**1.4 Design Studio – Backend Architecture**

         .. image:: images/BackendArchitecture.jpg
	  :alt: Backend Architecture diagram.	

**1.5 Composition Engine – Function**

	The Design Studio is supported by the backend Composition Engine, as shown in the figure below. The Composition Engine supports all the user activity on the Design Studio UI. It enables the user to:
	
	1.	Create, update, save and delete a composite solution out of the basic building blocks 
	
	2.	Validate the composite solution
	
	3.	Generate the Blueprint of the composite solution for deployment on target cloud(s) supported by Acumos. 
	
	The Composition Engine interacts with the backend Common Data Microservices APIs to:
	
	1.	Retrieve the catalog items (both the basic building blocks and composite solutions) from the backend Nexus repository.
	
	2.	Save and Retrieve the compositions created in the UI Layer.
	
	3.	Save the Blueprint files after successful validation
		
**1.6 Composition Engine – APIs**

	The Composition Engine offers a set of APIs that let the user:

	1.	Create composite solution

	2.	Save composite solution

	3.	Read composite solution

	4.	Close composite solution

	5.	Clear canvas of the composite solution

	6.	Delete composite solution

	7.	Validate composite solution

	8.	Add Node,

	9.	Add Link

	10.	Delete Node

	11.	Delete Link

	12.	Modify Node

	13.	Modify Link

	14.	 Fetch Node Metadata

	15.	Fetch Node Protobuf data

	16.	Get Matching Models.

**1.7 Artifacts created by Composition Engine**

	2.3.1	**CDUMP File**

	The Composition Engine is a backend for creating the composition graphs developed in the Design Canvas of the Design Studio. The Composition Engine maintains an in-memory graph representation that responds to editing operations like:

	1.	Adding nodes and links.

	2.	Deleting nodes and links.

	3.	Modifying node and link properties, etc. 

	These operations update the graph data structures in the Composition Engine. The cdump file is the serialization of “in – memory graph”. The cdump file is a simple structure, consisting of arrays of nodes, relations, inputs, and outputs. Currently the latter two are not used.

	The Composition Engine writes the graph structures as a JSON Objects which can be read back in to re-create the in-memory representation. The cdump file contains complete information on:

	1.	**Common Info:** The name, version, temporary cid, permanent solution Id, create time and last modified time of the composite solution.

	2.	**Node Info:** The complete information of each Node (basic building block) of the solution, 

		a.	Its  Capabilities and Requirements, 

		b.	If the node is a Data Map, then input and output field names, type, and mapping info.

		c.	X and Y coordinates of the nodes in the design canvas. 

	3.	Relations or Link Info: The link name, id, and the nodes connected at either end of the link.


2	Create New Composite Solution
=========================================
**2.1	Operation Name**
	createCompositeSolution
**2.2	Trigger**
	This API is called when the user creates a new composite solution by clicking the + sign in the Design Studio.
**2.3	Request**
	{
	   userId:string;//mandatory

	}
**2.4	Response**
	{
	 cid: string //serves as session Id. 
	success : boolean,
	 errorMessage: string // if generated by the Composition Engine

	}

**2.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”.

	2.	The Composition Engine must return the success as false if the userId is not provided and should return a user friendly error message such as – “User Id required”.

	3.	The Composition Engine will create a new cdump file, which will be named and versioned later on when the save composite solution API is called. Initially the cdump file will not have any nodes or links populated in it. They will be added with each addNode and addLink operation called by the UI Layer, or modified with deleteNode, deleteLink, modify Node, modifyLink operations, described later.

	4.	The Composition Engine will generate a UUID, which serves as a session Id, and populate the cid field in the cdump file with this UUID.

	5.	The Composition Engine must associate the user Id with the cid. 

	6.	The Composition Engine will populate the ctime field with the current time stamp.

	7.	The solution Id is not available to Composition Engine at this time. It will be available when the solution is stored in the backend catalog. 

	8.	At this time the Composition Engine is not storing any data in the Catalog DB nor in the Nexus repository.

	9.	The Composition engine must return the cid and success code to the caller. 

	
3 Save Composite Solution - User clicks Disk Icon on Design Studio
==============================================================================


**3.1	Operation Name**

saveCompositeSolution

This operation creates an entry for a new solution and its version or updates an entry for the existing solution version in Catalog DB and commits the solution (cdump file) to the Nexus repository.

**3.2	Trigger**

This operation is called when the user request the SAVE of the composite solution.

**3.3	Request**

	{

		userId: string,// user logged into Portal – mandatory 

		solutionName: string, //name provided by the user – mandatory – this can be changed at any time on SAVE operation

		solutionVersion: string, //version provided by the user – mandatory – this can be changed at any time on SAVE operation

		solutionId: String, //provided only if an existing solution is being updated, otherwise it will be empty. 

		description: string, //provided by user

		cid: string // composition Id originally generated by backend and returned to UI Layer as a response to createNewCompositeSolution API, now it is provided as input by UI Layer – this field is mandatory if the solution Id is missing. Mandatory for initial save request.

		ignoreLesserVersionConflictFlag: boolean //populated if the user wants to ignore overwriting of lesser version of this solution.

	}

**3.4	Response**

	{

		Success: boolean, //

		errorMessage: string // (for example – when the user attempts to over write a previous 

		cdump file of the composite solution// includes nodes & edges of graph

	}

**3.5	Behavior** 

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that the mandatory parameters have been populated, otherwise it should return a user friendly message, such as “Solution Name missing”, “Solution Version missing”, etc.

	3.	The Composition Engine must ensure that the cid provided in the input is one of the cid’s it had generated earlier, otherwise it should return an error message to the UI Layer – “Unknown Composition Id”. 

	4.	(Future) The Composition Engine must call the Modeling Engine to ensure the TOSCA validation of cdump file. (Future Sprint actions)

	5.	The Composition Engine will:

		a.	CASE – 1: If this is a new solution (cid is provided and solutionId is missing)

			i.	Create a new solution entry in the Catalog DB with the solution name and solution version provided as the input of the API. The Catalog DB will generate and provide the solutionId. The Composition Engine must make sure to set the following values as follows in the backend Catalog DB:

				1.	isComposite is set to TRUE

				2.	toolKitType = “DS”

				3.	visibility level = PRIVATE

			ii.	Correlate the solution Id with the cid provided by the API. 

			iii.	Store the validated cdump JSON file, so far built, in the Nexus – cdump file location. 

			iv.	Update the cdump file location in the solution version table. 

			v.	Populate the solutionId field of the cdump file with the solutionId provided by the Catalog DB. 

			vi.	Populate cname and version fields in cdump file with solution name and version provided in the API input.

			vii.	Populate the mtime field in the cdump file with the current timestamp. 

		b.	CASE – 2: If the solutionId already exists and the solution name and solution version provided (inputted) by the API also already exists (version conflict) in the Catalog, then:

			i.	(Now there exists an updated cdump file in the Composition Engine)

			ii.	Composition Engine must now make an association between cid and solutionId.

			iii.	Populate the mtime field in the cdump file with the current timestamp. 

			iv.	Composition Engine will now replace (and discard) the existing cdump file in Nexus repository with the updated (i.e., in memory) cdump file. 

			v.	Update the existing solution version entry to point to the location of the updated cdump file saved in the Nexus repository.

			vi.	Update the timestamp in the catalog DB. 

		c.	CASE – 3: : If the solutionId and the solution name already exists in the Catalog DB, but the solution version provided by API is different which does not exist in the Catalog DB, then

			i.	(Now there exists an updated cdump file in the Composition Engine)

			ii.	The Composition Engine will create a new version of the Solution in the Catalog DB, against the version number that is provided in the API input.

			iii.	Populate cname and version fields in cdump file with solution name and version provided in the API input. 

			iv.	Populate the mtime field in the cdump file with the current timestamp.

			v.	The Composition Engine will save the in – memory cdump file in the Nexus.

			vi.	The Composition Engine will populate the cdump file location in the new Solution Version table, created in the step above.

			vii.	The Composition Engine will update the timestamp if the Catalog DB.

		d.	CASE – 4: SolutionId, Solution Name and Solution Version already exists in the DB, but the solution version provided by the user is not the latest one (i.e., it is smaller than the most recent version) and the “ignoreLesserVersionConflictFlag” flag is set to False (default value).

			i.	The Composition Engine will do a lookup operation as usual, and if it finds the solution version provided by the user already exists and it is smaller than the most recent version, it must set success flag as false and send error message to the UI Layer – “Do you want to update a previous version of this solution?”

			ii.	The UI Layer will present this message to the user. 

			iii.	If the user accepts, then the UI Layer will send another saveCompositeSolution API call to the Composition Engine, this time with “ignoreLesserVersionConflictFlag” flag set to True.

		e.	CASE – 5: Solution Id, Solution Name and Solution Version already exists in the DB, but the solution version provided by the user is not the latest one (i.e., it is smaller than the most recent version) and the “ignoreLesserVersionConflictFlag” flag is set to True.

			i.	Populate the mtime field in the cdump file with the current timestamp. 

			ii.	Composition Engine will now replace the existing cdump file in Nexus repository with the updated (i.e., in memory) cdump file. 

			iii.	Update the existing solution version entry to point to the location of the updated cdump file saved in the Nexus repository.

	6.	The Composition Engine will populate the following fields in the Catalog DB:

		a.	userId (provided in the request)

		b.	ownerId: Same as userId

		c.	provider: The provider (Organization) should have been already provisioned in the USER TABLE - (check with Chris and Ashwin)

		d.	toolKitType Code: "DS"

		e.	category: (Check with Chris)

		f.	description: provided in the input

		g.	visibilityLevel: "PR"

4 Read complete Solution Graph from Nexus
=================================================

**4.1	Operation Name**
	readCompositeSolution
**4.2	Trigger**
	This operation is called when the user performs a double click operation on an existing composite solution in the Catalog Palette in order to display the complete solution in the Design Canvas. 
**4.3	Request**
	{
		userId: string // mandatory
		solutionId: string, // id of composite solution in catalog - mandatory
		version: string //mandatory
	}
**4.4	Response**
	{
		cdump: JSON, //JSON of cdump
		errorMessage: string //optional
	}
**4.5	Behavior**
	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 
	2.	The Composition Engine must check if the solutionId and version are found in the Catalog DB, otherwise it should return a user friendly error message back in the response, such as “Requested Solution Not Found”.
	3.	The Composition Engine must retrieve the location of the cdump file from the Catalog DB, via a query into Solution and Version Tables.
	4.	The Composition Engine must retrieve the cdump file from the Nexus repository and return the JSONised string of the file to the client.


5 Delete Composite Solution
====================================

**5.1	Operation Name**

	deleteCompositeSolution

**5.2	Trigger**

	This operation is called by the UI Layer when the user requests the deletion of the composite solution.

	Only the owner of the solution can request this operation, otherwise “Not authorized to perform this operation” is returned by the Composition Engine.

**5.3	Request**

	{

	  solutionId: string, // id of composite solution in catalog - mandatory

	  version: string, //mandatory

	  userId: string ///mandatory

	}

**5.4	Response**

	{

	 success: boolean, 

	 errorMessage: string //optional

	}

**5.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must check if the solutionId and version are found in the Catalog DB, otherwise it should return a user friendly error message back in the response, such as “Requested Solution Not Found”.

	3.	The Composition Engine must check the Catalog DB if the userId provided is the owner of the composite solution – both the solutionId and Version, otherwise it should return the success flag as False and send a user friendly error message back in the response, such as “User not authorized to perform the operation”.

	4.	If the user is the owner of the solution, then Composition Engine must perform the following functions:

		a.	Delete the cdump file associated with the solution version from the Nexus.

		b.	Delete the Version entry of the solution in the Catalog DB.


6 Add node
================


**6.1	Operation Name**

	addNode 

**6.2	Trigger**

	This operation is called when the user drags and drops:

	1.	A basic building block (a node) from the Catalog Palette to the Canvas, or

	2.	A Data Mapper from the Data Transformations Palette to the Canvas. 

**6.3	Request**

	{

		userId: string, // mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string //this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution. 

		nodeName: string,// optional – it may not be available initially, provided by the DS User

		nodeId: string, // mandatory – generated by UI Layer

		nodeSolutionId: string //mandatory – solution Id of the basic node in Common Catalog DB. This value is retrieved from fetchCatalogItems API

		nodeVersion: string // mandatory – version of the basic node in Common Catalog DB. This value is retrieved from fetchCatalogItems API

		type: {"name": "DataMapper or MLModel"}, //  Change for Data Mapper

		typeInfo: {}, // Type information -  empty in this Sprint 

		properties: [ ], // JSON List of Node Properties. It is not populated. 

		requirements: [ // this field should be populated by UI Layer if a node has one or more requirements in the TGIF.json file. This is a list of requirements.

		{

			  "name":"",

			  "relationship":"",

			  "id" : "",

			  "capability" : {

			   "name" : "calls.request.format+calls.request.version+calls.response.format+calls.response.versionFor DM populate Any ",  Change for Data Mapper

				"id" : ""

			  },

			  "target" : {

				"name" : " name-of-target-node-of-this-requirement-if-it-is-connected", //otherwise empty

				"description": ""

			  },

			  "target_type" : "Node"

			},

		{

		Another requirement spec. 

		}

		], //end of requirements list

		capabilities: [// this field should be populated by UI Layer if a node has one or more capabilities in the TGIF.json file. This is a list of capabilities.

		{

			  "id" : "",

			  "name" : "",

			  "target" : {

				"name" : "provides.request.format+provides.request.version+provides.response.format+provides.response.versionFor DM populate Any ",  Change for Data Mapper

				"id" : ""

			  },

			  "target_type" : "Capability",

			  "properties" : null

			}, 

			{

			  "id" : "",

			  "name" : "",

			  "target" : {

				"name" : "provides.request.format+provides.request.version+provides.response.format+provides.response.version versionFor DM populate Any ",  Change for Data Mapper

				"id" : ""

			  },

			  "target_type" : "Capability",

			  "properties" : null

			}



		], //end of capabilities list

		"ndata" : {// node’s position in the design canvas

			  "ntype" : "",

			  "px" : 385.89287722216187, number

			  "py" : 380.5962040115248,  number

			  "radius" : 10,  number

			  "fixed" : boolean,

			}

	}//end – of – Request 

**6.4	Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to DS User.

	}

**6.5	Behavior**

	1.	The Composition Engine must ensure that all the fields marked mandatory are populated and the request JSON structure is valid, otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation - Node Name missing”, “Cannot perform requested operation - Node Id missing”, etc.

	2.	The Composition Engine must make sure that the nodeId does not already exist in the cdump file, otherwise it must send success as false and an error message such as “Node Id already exists – cannot perform the requested operation”. 

	3.	The Composition Engine must create/add a child node entry under the “nodes” list of the cdump file.

	4.	The Composition Engine must populate the node element in the cdump file as follows:

		a.	name = node name provided by the API – this is inputted by the DS user

		b.	id = node Id provided by the API – this is generated by the UI Layer

		c.	solutionId = solution Id of the node provided by the API – this is the solution Id of the Node in the Common Catalog Database

		d.	version = version of the node provided by the API – this is the solution version number of the Node in the Common Catalog Database

		e.	type = {} – populate as provide by API. {"name": "DataMapper or MLModel"},

		f.	requirements = List of requirements as received by the API (see sample JSON file)

		g.	capabilities = List of capabilities as received by the API (see sample JSON file)

		h.	properties = [] – populate as empty list

		i.	typeInfo = {} – populate as empty JSON object

		j.	ndata = populate this JSON object with values received by the API.

	5.	The Composition Engine need not save the cdump file in the Nexus repository. 

	6.	(Future – Validation Steps) 


7 Add Link
==================


**7.1 Operation Name**

	addLink

**7.2 Trigger**

	This operation is called when the user: 

	1.	Connects a REQ port to a CAP port between a pair of ML Model nodes, or 

	2.	Connects a REQ port of the ML Model to the input Port of a Data Mapper, or 

	3.	Connects an output port of the Data Mapper to a CAP port of the ML Model.

**7.3 Request**

	{

		userId: string // mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string //this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution.  

		linkName: string, // optional

		linkId: string, // unique to this graph – mandatory

		sourceNodeName: string, // mandatory

		sourceNodeId: string, // id of node already in graph - mandatory

		targetNodeName: string, //mandatory

		targetNodeId: string, // id of node already in graph – mandatory

		sourceNodeRequirement: string //mandatory

		targetNodeCapabilityName: string //mandatory

		"properties": [// NOTE: Input fields are populated by UI Layer when a REQ port of ML Model is connected to DM and output fields are populated when DM is connected to the CAP port of ML Model.  DM Change

				{

				  "data_map": {

					"map_inputs": [

					  {

						"message_name": "Prediction",

						"input_fields": [

						  {

							"tag": "1 or 2 or 3",

							"role": "repeated or optional etc - not used in this sprint",

							"name": "name of the field",

							"type": "type of the field such as int32 string",

							"mapped_to_message": "output field message_name such as Classification or empty if it is not yet mapped", this field is not populated in this API. It will be populated in modifyNode() API

			"mapped_to_field": "tag number of the field in the message, such as 1 or 2 or empty if it is not yet mapped"  this field is not populated in this API. It will be populated in modifyNode() API. 

						  }

						]

					  }

					],

					"map_outputs": [

					  {

						"message_name": "Classification",

						"output_fields": [

						  {

							"tag": "1 or 2 or 3",

							"role": "repeated or optional or",

							"name": "name of the field",

							"type": "type of the field such as int32 string"

						  }

						]

					  }

					]

				  }

				}

			  ]

	}

**7.4 Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to user.

	}

**7.5 Behavior**

	1.	The Composition Engine must ensure that all the fields marked mandatory are populated and the request JSON structure is valid, otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Source Node Name missing”, “Source Node Id missing”, etc.

	2.	The Composition Engine must create/add a child node entry under the “relations” list of the cdump file.

	3.	The Composition Engine must populate the node elements as follows:

		a.	linkName = provided by the API

		b.	linkId = provided by the API

		c.	sourceNodeName = provided by API

		d.	sourceNodeId = provided by API

		e.	targetNodeName = provided by API

		f.	targetNodeId = provided by API

		g.	sourceNodeRequirement = provided by API

		h.	targetNodeCapability = provided by API

		i.	relationship = [] – an empty list

	4.	The Composition Engine must populate the properties section of the Data Mapper node in the cdump file as follows:  DM Change

		1.	Create map_inputs structure and populate the input fields of the target Data Mapper when a REQ port of a ML Model is connected to Data Mapper, with

			a.	Message name

			b.	Field details – tag, role, name and type

			 as shown in the cdump file.

		2.	Create map_outputs structure and populate the output fields of the source Data Mapper when the Data Mapper is connected to CAP port of the ML Model, with 

			a.	Message name

			b.	Field details – tag, role, name and type.

		as shown in the cdump file.

	5.	The Composition Engine need not save the cdump file in the Nexus repository. 


8 Delete Node
===================


**8.1 Operation Name**

	deleteNode

**8.2 Trigger**

	This operation is requested when the user deletes a node in the composition graph. This node may be connected to other nodes or it may be an isolated (un-connected) one. When a node is deleted all links connected to it (either originate from it or terminate on it) must also be deleted. This operation may result in some existing nodes becoming isolated. 

**8.3 Request**

	{

		userId: string, //mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string // composition Id originally generated by backend and returned to UI Layer as a response to createNewCompositeSolution API, now it is provided as input by UI Layer –  this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution. 

		nodeId: string// mandatory

	}

**8.4 Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to user.

	}

**8.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Node Id missing”, etc.

	3.	If the requested nodeId is not found in the cdump file, the Composition Engine must return success as false and a user friendly message, such as “Invalid Node Id – not found”.

	4.	The Composition Engine must:

		a.	Delete the specified node entry in the nodes list of the cdump file.

		b.	Find all the links that are connected to the specified node (originate from the node or terminate on the node) and delete these link entries in the relations list of the cdump file.

		c.	(Sprint - 4) For each link that terminates on the specified node, find the corresponding source node of the link. These source node are the ones whose Requirements are now un-fulfilled. These nodes may now need to display a warning message to the Design Studio user. (I think the UI Layer would automatically be able to display the warning message when a Requirement is un-fulfilled. Perhaps there is no need for the composition engine to send a warning message to be displayed on the affected nodes).

	5.	Return success as True to the client.

	6.	(NOTE: In future, composition engine may have rules to reject deletions)


9 Delete Link
====================


**9.1 Operation Name**

	deleteLink

**9.2 Trigger**

	This operation is requested when the user deletes a link between a pair of nodes in the composition graph. When a link is deleted its target node may become un-connected (isolated). 

	This operation is called to delete the link between

		1.	A REQ port and a CAP port between a pair of ML Model nodes, or 

		2.	A REQ port of the ML Model and the input Port of a Data Mapper, or 

		3.	An output port of the Data Mapper and a CAP port of the ML Model.

**9.3 Request**

	{

		userId: string // mandatory

		cid: string // mandatory if the solutionId is not available to UI Layer, otherwise not

		solutionId: string // mandatory if it is available to the UI Layer – i.e., after the initial SAVE

		version: string // mandatory if it is available to the UI Layer – i.e., after the initial SAVE

		linkId: string //mandatory

	}

**9.4 Response**

	{

		 success: boolean,

		 errorMessage: string // error string to be displayed to user.

	}

**9.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – JSON Invalid”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Link Id missing”, etc.

	3.	If the requested linkId is not found in the cdump file, the Composition Engine must return success as false and a user friendly message, such as “Invalid Link Id – not found”. 

	4.	The Composition Engine must delete the specified link entry in the relations list of the cdump file.

	5.	If a Data Mapper node is the target of the deleted link, then the Composition Engine must delete map_inputs entry in the data_map part of the node’s property section in the cdump file.  DM Change

	6.	 If a Data Mapper node is the source of the deleted link, then the Composition Engine must delete map_outputs entry in the data_map part of the node’s property section in the cdump file.  DM Change

	7.	Return success as True to the client. 

	8.	(In future, engine may have rules to reject deletions).


10 Modify Node
===================


**10.1 Operation Name**

	modifyNode

**10.2	Trigger**

	This operation is called by the UI Layer:

	1.	When the user moves a node on the design canvas or changes the name of the node, or 

	2.	When the user maps, i.e, connects an input field of the Data Mapper node to an output field of the Data Mapper node, or 

	3.	When the user deletes the existing mapping between a pair of input and output fields.

**10.3 Request**

	{

		userId: string // mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string //this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution.

		nodeId: string, // mandatory

		nodeName: string // populated if a new name is assigned to the node, otherwise empty.

		ndata: { 

			ntype: string // populated as “” in this Sprint

			px: number,

			py: number

		}//either nodeName or ndata field or field_map should be populated

		field_map: {  Change for Data Mapper

			map_action: “add or delete”

			input_field_message_name: string,

			input_field_tag_id: string,

			output_field_message_name: string

			output_field_tag_id: string

		}//either nodeName or ndata field or field_map should be populated 

	}

**10.4 Response**

	{

		 success: boolean,

		 errorMessage: string // error string to be displayed to user.

	}

**10.5 Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Node Id missing”, etc.

	3.	If the requested nodeId is not found in the cdump file, the Composition Engine must return success as false and a user friendly message, such as “Invalid Node Id – not found”. 

	4.	The Composition Engine must update the nodeName, ntype, px and py elements of the specified nodeId in the cdump file with the values provided.

	5.	For a Data Mapper node, the Composition Engine must perform the requested map_action (add or delete) by appropriately updating the data_map in the properties section of the node in the cdump file.

	6.	Return success as True to the client. 

	7.	(In future, engine may have rules to reject modifications).


11 Modify Link
=====================


**11.1 Operation Name**

	modifyLink

**11.2	Trigger**

	This operation is called when a link name is provided or modified by the user. 

**11.3	Request**

	{

		userId: string // mandatory

		cid: string // mandatory if the solutionId is not available to UI Layer, i.e., before SAVE, otherwise not

		solutionId: string // mandatory if it is available to the UI Layer – i.e., after the initial SAVE

		version: string // mandatory if it is available to the UI Layer – i.e., after the initial SAVE  

		linkId: string, //mandatory

		linkName: //mandatory

		layout: {}

	}

**11.4	Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to user.

	}

**11.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Link Id missing”, etc.

	3.	If the requested linkId is not found in the cdump file, the Composition Engine must return success as false and a user friendly message, such as “Invalid Link Id – not found”. 

	4.	The Composition Engine must update the linkName element of the specified linkId in the cdump file with the value provided.

	5.	Return success as True to the client. 

	6.	(In future, engine may have rules to reject modifications).


12 Fetch Basic Building Blocks for a User
==================================================


**12.1	Operation Name**

	fetchCatalogItems

**12.2	Trigger**

	This operation is called by the UI Layer when the user initially logs into the Design Studio in order to populate the Palette of catalog items to be displayed to the user based on his credentials. Both the simple solutions and composite solutions are retrieved. Only the following catalog items can be populated in the Palette for a given user:

		1.	Catalog items marked “Public”

		2.	Catalog items marked “Private” to the user. 

		3.	Catalog items marked as belonging to the user’s “Organization” of which the user is a member.

**12.3	Request**

	{

		userId: String // mandatory

	}

**12.4	Response**

	{

	 items: [list of catalog items

		{

		  solutionId: string,

		  version : string,

		  ownerId : string,

		  solutionName: string,

		  description: string,

		  created: date as string,

		  modified: date as string

		  visibilityLevel: "private", "organization", "public",

		  provider: string,

		  toolKit: string,

		  category: string,

		  icon: string // url or other resource id to display as icon in palette

		},

	{

	Another catalog item

	}

	 ]//end item list

	}

**12.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – User Id missing”, etc.

	3.	If the requested userId is not found in the catalog DB, the Composition Engine must return success as false and a user friendly message, such as “User Id – not found”.

	4.	Composition engine will call the catalog database to retrieve all the existing solutions (both basic solutions as well as composite solutions) corresponding to the userId.

	5.	If the requested userId is found in the catalog DB but there are no catalog items (either Private, or Organization, or Public) corresponding to the user Id, the Composition Engine must return success as true and an empty catalog item list to the client

	6.	The Composition Engine must return a list of all catalog items which are: 

	a.	Marked “Public”.

	b.	Marked “Private” to the user. 

	c.	Marked as belonging to the user’s “Organization” of which the user is a member.

	7.	For each catalog item which meets the above criterion, the Composition Engine must retrieve the attributes specified in the response and return them to the client. The success parameter must be set to true. 


13 Fetch Composite Solutions for a User
================================================


**13.1	Operation Name**

	getCompositeSolutions

**13.2	Trigger**

	This operation is called by the UI Layer when the user initially logs into the Design Studio in order to populate the List of Composite Solutions to be displayed to the user based on his credentials. Based on input parameter “visibilityLevel” this operation retrieves the Composite Solutions. User can pass either one, two or all the below option as value for the input parameter “visibilityLevel”, in order to retrieve the required list of Composite Solutions:

		1.	“PR”: to include the private Composite Solutions in the list 

		2.	“OR”: include the organization level visible Composite Solutions. 

		3.	“PB”: to include the public level Composite Solutions.  

**13.3	Request**

	{ 

		userId: string,// user logged into Portal – mandatory, 

		visibilityLevel : string // PR,OR,PB -- mandatory. You can specify multiple value separated by ','. 

	}

**13.4	Response**

	{

		items: [list of catalog items

		{

			  solutionId: string,

			  version : string,

			  ownerId : string,

			  solutionName: string,

			  description: string,

			  created: date as string,

			  modified: date as string

			  visibilityLevel: "private", "organization", "public",

			  provider: string,

			  toolKit: string,

			  category: string,

			  icon: string // url or other resource id to display as icon in palette

		},

		{

			Another Composite Solution

		}

		]//end item list

	}

**13.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – User Id missing”, etc.

	3.	If the requested userId is not found in the catalog DB, the Composition Engine must return success as false and a user friendly message, such as “User Id – not found”.

	4.	Composition engine will call the catalog database to retrieve all the existing Composite solutions corresponding to the userId.

	5.	If the requested userId is found in the catalog DB but there are no Composite Solutions (either Private, or Organization, or Public) corresponding to the user Id, the Composition Engine must return success as true and an empty catalog item list to the client

	6.	The Composition Engine must return a list of Composite Solutions depending on the value(s) of input parameter “visibilityLevel”.


14 Clear canvas of Composite Solution
============================================


**14.1	Operation Name**

	clearCompositeSolution	

**14.2	Trigger**

	This operation is requested when the user clicks “Clear” button to clear the contents of the canvas. This operation should delete all the nodes and links from the CDUMP file. 

**14.3	Request**

	{

		userId: string, //mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string // composition Id originally generated by backend and returned to UI Layer as a response to createNewCompositeSolution API, now it is provided as input by UI Layer –  this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution. 

	}

**14.4	Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to user.

	}

**14.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Node Id missing”, etc.

	3.	The Composition Engine must:

		a.	Delete all the link entry in the nodes list of the cdump file.

		b.	Delete all the node entry in the nodes list of the cdump file. 

	4.	Return success as True to the client.


15 Fetch TOSCA JSON of Basic Solution
=============================================


**15.1	Operation Name**

	fetchToscaJSON

**15.2	Trigger**

	This operation is called by the UI Layer immediately after user has logged in and all the catalog items for the user have been populated in the Palette, via the fetchCatalogItems API.

	For each item in the Palette, the UI Layer calls this operation to retrieve the JSON TOSCA file, i.e., the TGIF.json associated with the basic solution. Note that there is no TGIF.json file associated with the composite solution. TGIF.json only needs to be associated with the basic solutions (nodes).

**15.3	Request**

	{

		userId: string // mandatory

		solutionId: string, // mandatory - global id of basic solution in catalog

		version: string // mandatory 

	}

**15.4	Response**

	{

		JSON representation of TGIF.json file for the requested solution

		success: boolean,

		errorMessage: string // error string to be displayed to user.

	}

**15.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Solution Id (or Version) missing”, etc.

	3.	If the requested solutionId is not found in the catalog DB, the Composition Engine must return success as false and a user friendly message, such as “Incorrect Solution Id – not found”, or “Incorrect Version – not found”.

	4.	For the requested solution Id and version, the Composition Engine must retrieve the location of the TGIF.json from the Catalog DB.

	5.	The Composition Engine must retrieve the TGIF.json from Nexus at the location pointed out by Catalog DB

	6.	The Composition Engine must return the json string of the TGIF.json file to the client, success set to true. 


16 Fetch Protobuf JSON of Basic Solution
=================================================


**16.1	Operation Name**

	fetchProtobufJSON

**16.2	Trigger**

	This operation should be called, for each node, when:

	4.	A node is dragged from the catalog palette to the design canvas, or

	5.	A composite solution is dragged from the catalog palette to the design canvas.

	Note that each node, aka, the basic ML Solution (identified by the combination of solutionId and version), in a composite solution is associated with the following files:

		1.	Protobuf file

		2.	Protobuf.json file

		3.	TGIF.json file

	Output: This operation returns the JSON representation of all the operations specified in the Protobuf File, i.e, the serialized Protobuf.json

	For each operation in the Protobuf.json file, this API should return the

		1.	Operation name

		2.	Input Message name(s)

		3.	Output Message name(s)

		4.	Detailed schema of each input message – as defined in the original Protobuf file. Each schema should be associated with the corresponding message name

		5.	Detailed schema of each output message – as defined in the original Protobuf file. Each schema should be associated with the corresponding message name. 

**16.3	Request**

	{

		userId: string //mandatory

		solutionId: string // mandatory – solution Id of the basic node – this id is available from a previous  fetchCatalogItems API call

		Version: string // mandatory – version if the basic node - this value is available from a previous fetchCatalogItems API call

	}

**16.4	Response**

	{

		protobuf_json: // JSON representation of Protobuf file. 

		success: boolean,

		errorMessage: string // error string to be displayed to user.

	}

**16.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – Node Id missing”, etc.

	3.	Identify the Protobuf.json file associated with the node type. 

	4.	For each operation in the Protobuf.json file, the Composition Engine must retrieve the 

		a.	Operation name

		b.	Input Message name(s)

		c.	Output Message name(s)

		d.	Detailed schema of each input message – as defined in the original Protobuf file. Each schema should be associated with the corresponding message name

		e.	Detailed schema of each output message – as defined in the original Protobuf file. Each schema should be associated with the corresponding message name.

	5.	The Composition Engine must return the serialized representation of Protobuf.json file. 


17 Close Composite Solution
==================================


**17.1	Operation Name**

	closeCompositeSolution 

**17.2	Trigger**

	This operation is called when the user requests the closing of the composite solution currently open in the design canvas. This operation should be called when the user clicks the “X” mark on the top right hand corner of the canvas.

	If there are unsaved changes when the user clicks “X”, then the user should be prompted to save the solution first. 

		a.	User chooses to save the solution: Call the saveCompositeSolution API and when its response is received by the UI Layer, then call the closeCompositeSolution API on the Composition Engine. 

		b.	User declines to save the solution: Any unsaved changes will not be saved to Nexus, but the cdump file will be closed (deleted). Call the closeCompositeSolution API. 

**17.3	Request**

	{

		userId: string, //mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string // composition Id originally generated by backend and returned to UI Layer as a response to createNewCompositeSolution API, now it is provided as input by UI Layer –  this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution.

	}

**17.4	Response**

	{

	 success: boolean,

	 errorMessage: string // error string to be displayed to user.

	}

**17.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – User Id missing”, etc.

	3.	The Composition Engine must close the cdump file, without saving it in Nexus repository. The cdump that existed in the Nexus at the last SAVE operation will serve as the latest cdump when the user wants to read the composite solution later on).


18 On Hover Input Port
================================


**18.1	Operation Name**

	onHoverInputPort – This operation is not handled by the Composition Engine. 

**18.2	Trigger**

	This operation is called when the user hovers the mouse over the input port of the node. 

**18.3	Request**

	{

		solutionId: string // mandatory – solution Id of the basic node – this id is available from a previous  fetchCatalogItems API call

		Version: string // mandatory – version if the basic node - this value is available from a previous fetchCatalogItems API call

		operationName:  string// mandatory – each input port is identified by the name of the operation

	}

**18.4	Response**

**18.5	Behavior**

	1.	The UI Layer should retrieve a list of one or more input message names associated with the given operation name from the JSON object representation of Protobuf already associated with the node. Note that this JSON object is already associated with the node when the node was dragged inside the canvas (or when the composite solution containing this node was dragged into the canvas).

	2.	The UI Layer should display a pop up. 

	3.	The UI Layer should display the name of the operation and a list of one or more input message names inside the pop up. The message names should enclosed inside brackets – such as fit(DataFrame1, DataFrame2).

	4.	The message names should be a hyperlink into the corresponding message schema – as defined in the original Protobuf file. 


19 On Hover Output Port
===============================

**19.1	Operation Name**

	onHoverOutputPort

**19.2	Trigger**

	This operation is called when the user hovers the mouse over the output port of the node.

**19.3	Request**

	solutionId: string // mandatory – solution Id of the basic node – this id is available from a previous  fetchCatalogItems API call

	Version: string // mandatory – version if the basic node - this value is available from a previous fetchCatalogItems API call

	operationName:  string// mandatory – each output port is identified by the name of the operation

**19.4	Response**

**19.5	Behavior**

	1.	The UI Layer should retrieve a list of one or more output message names associated with the given operation name from the JSON object representation of Protobuf already associated with the node. Note that this JSON object is already associated with the node when the node was dragged inside the canvas (or when the composite solution containing this node was dragged into the canvas).

	2.	The UI Layer should display a pop up. 

	3.	The UI Layer should display the name of the operation and a list of one or more output message names inside the pop up. The message names should enclosed inside brackets – such as fit(Prediction).

	4.	The message names should be a hyperlink into the corresponding message schema – as defined in the original Protobuf file. 


20 On Click of Message (Input or Output)
===============================================

**20.1 Operation Name**

	onClickMessage

**20.2 Trigger**

	This operation is called when the user clicks on an input or an output message in the input/output port of the node.

**20.3	Request**

	{

		operationName: string //// mandatory – each input port is associated with an operation

		messageName:  string// mandatory – each operation name has input and output message(s)

	}

**20.4	Response**

**20.5	Behavior**

	1. The UI Layer should retrieve the message schema of the named message from the JSON Object representation associated with the node.

	2. The UI Layer should convert the JSON representation of the message into its original Protobuf message schema format.

	3. The UI Layer should send the Protobuf message schema format to the Properties box.

	4.	The Properties Box should display the message schema in the original Protobuf format.


21 Get Matching Models for a Port
===========================================

**21.1	Operation Name**

	getMatchingModels

**21.2	Trigger**

	This operation is called by the UI Layer when the user clicks on the port of a node in the design canvas, in order to get a list of ML Models (i.e., basic building blocks) that match the message signature of the port.

	The requirement is to enable the DS user to drag and drop the matching models from the “Matching Models” pane into the design canvas. 

**21.3	Request**

	{

		userId: string // mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string //this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution.

		port_data: { 

			  port_type: “provider” or “consumer”/ Provider and Consumer ports are associated with the Input (unfilled circle) and Output (filled in circle) of   an Operation 

			  protbuf_data: [] //Array of JSON representation of one or more messages inside the Port 

			}//mandatory

	}

**21.4	Response**

	{

		success: boolean,

		matchingModels: [

			{

				name: String// name of the matching ML Model,

				tgifReference: String //location of TGIF file in Nexus

			}

		] // list of the names of matching ML Models, i.e., basic building blocks,

		errorMessage: string // error string to be displayed to user.

	}

**21.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – User Id missing”, etc.

	3.	The Composition Engine must:

		a.	Identify if the request is to find the matching models of a “Provider” port or a “Consumer” port.

		b.	Retrieve the TGIF.json files of ML Models – the basic building blocks in the CCD, one after another.

		c.	For a consumer port, search and match requested message signature with the message signatures on the Provider port(s) of the TGIF.json file, and if there is a match found, then populate the name of the ML Model and the TGIF.json reference of the Model in the matchingModels list (see Response section).

		d.	For a provider port, search and match requested message signature with the message signatures on the Consumer port(s) of the TGIF.json file, and if there is a match found, then populate the name of the ML Model and the TGIF.json reference of the Model in the matchingModels list (see Response section)

		e.	If no matches are found, then return success as false, and populate the errorMessage as “No matching models found”, otherwise return success as True. 

	4.	Return the response to the UI Layer. 


22 Validate Composite Solution
======================================

**22.1	Operation Name**

	validateCompositeSolution 

**22.2	Trigger**

	This operation is called by the UI Layer when the user clicks on the Validate Button in the Design Studio. 

	When the response to this API is received, the UI Layer, should populate the Validation Console with either a single success message or a list of error and warning messages returned by the backend Composition Engine. 

**22.3	Request**

	{

		userId: string, //mandatory

		solutionId: string // this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		version: string// this field will be empty for a new un – saved solution. It is mandatory for a saved solution

		cid: string // composition Id originally generated by backend and returned to UI Layer as a response to createNewCompositeSolution API, now it is provided as input by UI Layer –  this field should be populated (mandatory) if the solutionId and version is missing such as  for a new un – saved solution. 

	}

**22.4	Response**

	{

		success: boolean,

		validationMessages[]: string // A single “Validation Successful” message or a list of one or more Error messages and Warning Messages.

		errorMessage: string // error string to be displayed to user.

	}

**22.5	Behavior**

	1.	The Composition Engine must check if the request JSON structure is valid, otherwise it should return success as false and a user friendly message, such as “Incorrectly formatted input – Invalid JSON”. 

	2.	The Composition Engine must ensure that all the fields marked mandatory are populated,  otherwise it must return success as “false” and populate the helpful error message which is displayed to the user, such as “Cannot perform requested operation – User Id missing”, etc.

	3.	The Composition Engine must:

		a.	Must retrieve the cdump file associated with the solution from Nexus repository.

		b.	Perform validation of the cdump file to make sure that no model (basic building blocks) is isolated / unconnected. 

		c.	If there are isolated models in the composite solution, then for each such model, the composition engine must create an error message such as “Error – Mode Name is not connected.”

		d.	The Composition Engine must set success as False and send a list of error messages in the “validationMessages” list to the client.

		e.	If there are no errors, the Composition Engine must:

			i.	Create the Blueprint.json file (as described)

			ii.	Store the Blueprint.json in Nexus

			iii.	Store the location of Blueprint.json in Common Catalog DB.

			iv.	Set success as True and send “Successful” message in the “validationMessages” list to the client. 


23 Auto – Save Feature (Future Sprint – TBD)
====================================================

**23.1	Description**

	The Composition Engine should periodically perform an auto save operation of the cdump file. The cdump file should be stored in a pre-designated space in the Nexus repository

**23.2	Trigger**

	This feature should be activated periodically without an API request from the UI Layer. The activation frequency should be assignable at the Design Studio installation time and should be changeable by the Design Studio admin.

**23.3	Behavior**

	1.	Auto save of the initial composite solution without a solution id, name, and version assigned:

	2.	Auto save of the composite solution with a solution id, name and version assigned:

	3.	When a user logs in (how does the composition engine know that a user has logged into the DS?).


24 Properties Panel
============================

	The following properties of the model should be visible on the Properties panel of the Design Studio. These properties are read only. Some of these properties, such as model name, owner Id, provider name, description, category, visibility level can be changed on the “Manage My Models” page of the Market Place Portal by the model owner only.

		a.	Name of the Model (source – Catalog DB) 

		b.	Model Package Name (source - TGIF.json)

		c.	Capability Names (source - TGIF.json)

		d.	Requirement Names (source - TGIF.json) 

		e.	Model Owner Id (source – Catalog DB)

		f.	Mode Provider Name: The provider (Organization) should have been already provisioned in the USER TABLE - (source – Catalog DB)

		g.	ToolKit Type Name: Scikit, RCloud, H2O, Argus, etc. (source – Catalog DB)  (Ideally should be populated during on Boarding, source – Catalog DB). 

		h.	Mode category: Prediction | Classification | Data Transformation, etc. (source – Catalog DB)

		i.	Model description: provided in the input. (source – Catalog DB)

		j.	Model visibilityLevel: PRIVATE | ORGANIZATION | PUBLIC (source – Catalog DB)

25 Generic Data Mapper
=================================

**25.1	Requirements**

	1.	Maps between any outputs and inputs that need to be connected.

	2.	Data Mapper maps or transforms the data between a pair of ports – output message and an input message.

	3.	Any output port of a ML Model can be connected to a Data Mapper, and the Data Mapper can be connected to any input port of the ML Model.

	4.	Composition Rule: From the Design Studio composition perspective a Data Mapper can accept any inputs and produce any outputs, depending on the ML models that it are connected to its input and output side.. So its requirements and capability will be indicated any.  

	5.	Data Mapper will perform transformation between basic Protobuf types only.

**25.2	Initial Delivery Requirements for Static Data Mapper**

	ML team feels that at the moment the only data transformation /mapping that is required is between integer and float numbers. Mapping between Strings and Integers is optional.

		1.	Develop a Data Mapper that can transform data from integer types to floating point numbers and vice versa.

		2.	Develop a Data Mapper that can transform data between string timestamp and integer timestamp. 

		3.	Data Mapper will have a Protobuf file with an operation such as mapData(int, String) : returns (float, int)

**25.3	How to On Board the Data Mapper**

	The Data Mapper is a DS tool. Unlike other ML Models that have a Protobuf file associated with them, the DM does not have a Protobuf file associated with it. However, to enable the DM to make use of the On boarding features such as Microservices generation, Dockerization and TGIF generation, a Protobuf file has been defined (see DataMapper-proto.proto). This allows the Data Mapper to be on boarded to the Common Catalog Database (CCD) and Nexus repository. 

26 Blueprint Generator
===============================

**26.1	Requirements**

	1.	Deploy the composite solution as a set of multiple docker containers.

	2.	Deployment Target – Azure or OpenStack or AWS

	3.	Develop a Blueprint Generator that can generate the Kubernetes deployment script of the composite solution. 

	4.	Develop a Blueprint Generator that can generate Azure deployment script of the composite solution. 

	5.	Dynamically generate the docker image of the Data Mapper.









