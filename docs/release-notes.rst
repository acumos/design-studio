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
Design Studio Release Notes
===========================

The Design Studio Composition Engine is packaged within a Docker image and available from the
Acumos docker registry.  The TOSCAGenerator Client library is published in the Acumos Nexus
repository as a jar file.

Version 3.0.6, 2020-05-06
---------------------------
* ACUMOS-3483 : As a Supplier, I should be able allow/deny my model to be included in creation of composite models (i.e. chaining models)

Version 3.0.5, 2020-03-12
---------------------------
* Updated the existing download url for protobuf-java jar in ds-compositionengine pom.xml
* Updated major, minor and patch version 
  TOSCAModelGeneratorClient:2.0.8
* ACUMOS-4062 : Updated TOSCAModelGeneratorClient to create new API to accept model name & protobuf to generate the TOSCA artifacts

Version 3.0.4, 2020-01-29
---------------------------
* Updated DS modules to CDS 3.1.1 version compatibility.

Version 3.0.3, 2019-12-16
---------------------------
* Added platform-oam dependency for MDC logging purpose .

Version 3.0.2, 2019-12-10
---------------------------
* Updated DS modules to CDS 3.1.0 version compatibility.

Version 3.0.1, 2019-10-16
---------------------------
 * ACUMOS-1206 : Sonar 40% code coverage for Design Studio project
 * ACUMOS-3480 : User Guide | Design Studio | Acu-Compose | MLWB : Few section need to update

Version 3.0.0, 2019-09-30
---------------------------
 * ACUMOS-3453 : Update all Design Studio component to use Java 11

Version 2.2.3, 2019-09-20
---------------------------
 * ACUMOS-3451 : Update DS component to use CDS 3.0.0 client
 
Version 2.2.2, 2019-09-09
---------------------------
* ACUMOS-3377 : DS BE need to include Revision ID in response along with other details for API to create and update composite solutions

Version 2.2.1, 2019-08-09
---------------------------
* ACUMOS-3302 : IST2 | Able to save publicly published solution in public solutions palette after updation
* ACUMOS-1206 : Sonar 40% code coverage for Design Studio project.

Version 2.2.0, 2019-07-25
---------------------------
* ACUMOS-3132 : Composite solution deleted from Portal doesn't allow Design Studio to create new composite solution with same name.
* ACUMOS-1206 : Sonar 40% code coverage for Design Studio project.
* ACUMOS-3258 : ds-compositionengine to include flag to indicate duplicate solution (i.e. solution with same name and version)

Version 2.1.0, 2019-05-30
---------------------------
* ACUMOS-2844 : ds-compositionengine log output uses 100s of GB, fills disk.

Version 2.0.9, 2019-05-03
---------------------------
* ACUMOS-2844 : ds-compositionengine log output uses 100s of GB, fills disk.

Version 2.0.8, 2019-04-22
---------------------------
* Updated DSCE components to CDS 2.2.2
* ACUMOS-2756 IST |On-boarded model reflecting twice in drawers after publishing to the company market place.

Version 2.0.7, 2019-04-12
---------------------------
* ACUMOS-2696 : Models have disappeared from LF Design Studio.
* ACUMOS-2709 : ds-compositionengine to populate RevisionId of each solution in CDUMP.

Version 2.0.6, 2019-02-28
---------------------------
* ACUMOS-1670 : Allow sharing private solution created from DesignStudio with other users.

Version 2.0.5, 2019-02-16
---------------------------
* ACUMOS-1801 : CDS clients pass request ID from front-end thru in client calls
* ACUMOS-2327 : Logging Standardization - Design Studio

Version 2.0.4, 2019-01-30
---------------------------
* ACUMOS-2433 : No solutions listed in Design Studio page
* ACUMOS-2405 : Update DS component to use CDS 2.0.2
* ACUMOS-2406 : Update ds-compositionengine to used CDS Client 2.0.2
* ACUMOS-2407 : Update TOSCA-ModelGeneratorClient to use CDS client 2.0.2

Version 2.0.2, 2019-01-11
---------------------------
* ACUMOS-2059 : Create Protobuf (.proto) for new valid Composite Solution.
* ACUMOS-2060 : Update the .proto file of the existing composite solution (for each version).
* ACUMOS-2061 : Delete the .proto file of the existing solution (for each version) on successful deletion.
* ACUMOS-2062 : ds-compositionengine should create .proto on successful validation of new Composite Solution.
* ACUMOS-2066 : If the Data Broker is the first component of the solution, then .proto file should include text.
* ACUMOS-2067 : The .proto file should follow the same naming convention and structure as the .proto file created by the On-boarding module.
* ACUMOS-2068 : The operation name in the .proto file will be the operation name of the first model in the composite solution.
* ACUMOS-2069 : Input and output protobuf message for Composite solution should be the first and last model input and out protobuf msg respectively.
* ACUMOS-2070 : Create .proto file for validated Composite Solution (for each version).
* ACUMOS-2071 : DS should update .proto file when user update the existing version of Composite Solution.
* ACUMOS-2072 : DS should delete the .proto file along with other artifacts, when user deletes composite solution version.
* ACUMOS-2312 : Copy/Array Based composite solution showing invalid solution for valid solution.

Version 2.0.0, 2018-12-14
---------------------------
* ACUMOS-1989 : DC-Staging Failed onboarded models are visible in DS
* ACUMOS-1969 : Default CDS startup check interval too short, attempts too few; pls increase
* ACUMOS-1650 : Senitization for CSS Vulnerability
* ACUMOS-1968 : IST2 | UI showing validation failed error for valid solution
* ACUMOS-2036 : DSCE- If a splitter and collator node is present in cdump file, but it has zero mappings associated with it, the validation should show invalid solution.
* ACUMOS-2033 : Api to get the dsce version
* ACUMOS-2272 : Incorrect Protobuf.json and TGIF.json generated for nested messages

Version 1.40.2, 2018-10-12
---------------------------
* ACUMOS-1886 : IST2 Design Studio fails to start due to exception in populatePublicModelCacheForMatching

Version 1.40.1, 2018-10-01
---------------------------
* Upgrade DSCE and TGC to CDS 1.18.2
* Updated major, minor and patch version 
  csvdatabroker:1.4.0
  sqldatabroker:1.2.0
  gdmservice:1.2.0
  TOSCAModelGeneratorClient:1.33.1

Version 0.0.40, 2018-09-21
---------------------------
* Upgrade DSCE and TGC to CDS 1.18.1

Version 0.0.39, 2018-09-20
---------------------------
* ACUMOS-1756 : Upgrade Design Studio Java server components to Spring-Boot 1.5.16.RELEASE
* ACUMOS-1770 : Unable to on-board model via web on-boarding as getting 'Exception in TOSCA Model Generator Client'.


Version 0.0.38, 2018-09-14
---------------------------
* ACUMOS-624  : log standardization and consistency design studio
* ACUMOS-1665 : Update ds-composition to handle new CDUMP and BLUEPrint file.
* ACUMOS-1666 : Update ds-composition to handle databroke of type : SQL.
* ACUMOS-1667 : Validation for mapping table - user must select at least one column type.
* ACUMOS-1747 : Update CSV databroker code w.r.t change in the Databroker Map.
* ACUMOS-1699 : Design Studio must wait for CDS to start before populating matching-model cache.
* ACUMOS-1551 : Increase code coverage for modules under design-studio repository.
* ACUMOS-662  : Develop SQL DataBroker 
* ACUMOS-1662 : ds-compositio changes w.r.t to SQL Databroker

Version 0.0.37, 2018-09-07
---------------------------
* ACUMOS-1701 : Upgrade DSCE and TGC to CDS 1.18.0


Version 0.0.36, 2018-09-03
---------------------------
* ACUMOS-1191 : DS should ignore model's toolkit type attribute when populating selection palette
* ACUMOS-1563 : Improve matching model search Performance
* ACUMOS-1564 : Redesign and re-implement matching model API to improve the performance
* ACUMOS-1565 : On application Start construct the HashMap of Models (Public and company) for matching
* ACUMOS-1566 : At some configurable time interval refresh the HashMap of Models (published to Public and company) for matching
* ACUMOS-1567 : Change implementation of Matching model API
* ACUMOS-1568 : Construct the Java POJO classes KeyVO and ModelDetailVO
* ACUMOS-1570 : Implement logic to fetch all the public and company level Models from CDS
* ACUMOS-1571 : Populate models into HashMap and push it to the Application Context
* ACUMOS-1572 : Get the updated models using CDS API : findSolutionsByDate
* ACUMOS-1573 : Find the matching from HashMap (from Application Context)
* ACUMOS-1574 : Find the matching model in the private user model list


Version 0.0.35, 2018-08-16
---------------------------
* ACUMOS-1484 : Remove jar files from DS Gerrit repository
* ACUMOS-1599 : Update DS Composition engine w.r.t CDS 1.17.1
* ACUMOS-1585 : Rename TOSCA artifacts - TOSCATGIF and TOSCAPROTOBUF
* ACUMOS-1520 : Restriction pop up is missing if user tries to modify and the public solution.
* ACUMOS-1610 : Revised CSV Databroker, user is no more required to share IP, Location of data file and credentials to access data file
*  ACUMOS-1619 : Revised CSV Databroker : User is no more required to share IP, path and credential to access data file
*   ACUMOS-1627 : Switch between local and Remote Script executor
*   ACUMOS-1634 : Data broker should no more use any kind of script to read the local or remote file


Version 0.0.34, 2018-08-03
---------------------------
* ACUMOS-1488 : Add missing license and copyright notice.
* ACUMOS-1492 : Update DS Composition engine w.r.t CDS 1.16.1


Version 0.0.33, 2018-07-30
---------------------------
* ACUMOS-1357 : Save the Composite Solution Description at Revision Level.
* ACUMOS-1236 : Enhance design studio to store members (parent-child relationships) of composite solutions.
* ACUMOS-1471 : Update DS Composition engine w.r.t CDS 1.16.0.


Version 0.0.32, 2018-07-05
---------------------------
* ACUMOS-1002 : To allow to connect output of multiple model to Collator.
* ACUMOS-1003 : Update CE Modify Node API for Collator (BE).
* ACUMOS-1004 : Update CE Delete Node API for Collator (BE).
* ACUMOS-1005 : Update CE Delete Link API for Collator (BE).
* ACUMOS-1006 : Update CE Validate Composite Solution API.
* ACUMOS-1127 : Enhance DS back end to return error when model cannot be dropped on canvas.(EPIC)
* ACUMOS-1039 : Design Studio Composition Engine (CE) to support message splitting (broadcast and parameter splitting capability).(EPIC)
* ACUMOS-1055 : Update Modify Node API for Splitter (BE).
* ACUMOS-1056 : Update Add Link API for Splitter/Collator (BE).
* ACUMOS-1057 : Update Delete Link API for Splitter (BE).
* ACUMOS-1058 : Update Validate Composite Solution API.
* ACUMOS-1065 : Update DS Modules code to point to CDS 1.15.3.
* ACUMOS-1197 : DS allow single-model composite solution.


Version 0.0.30, 2018-06-06
---------------------------
* ACUMOS-971 : Deploy button active for invalid solution, BluePrint File Changes.


Version 0.0.29, 2018-05-21
---------------------------
* ACUMOS-882 : Include SolutionRevisionId along with other details for the solution with same name and version.
* ACUMOS-928 : Junit TestCases For DS-DataBroker.


Version 0.0.28, 2018-05-15
---------------------------
* ACUMOS-856 : Delete node not working as per the expectations.
* ACUMOS-864 : Deploy button is active for not validated solution.


Version 0.0.27, 2018-05-10
---------------------------
* ACUMOS-791 : Data is present in target table when there is no node or ML is directly connected to the databroker node.
* ACUMOS-794 : Update API : createNewCompositeSolution to set the solution validate flag to false (BE).
* ACUMOS-795 : Update API : saveCompositeSolution to reset the solution validate flag to false.
* ACUMOS-796 : Update API : validateCompositeSolution to reset the solution valid flat to true or false.
* ACUMOS-800 : Construct CSV Databroker as DS tool
* Update to use latest version of Common Data Service : 1.14.4.


Version 0.0.26, 2018-05-03
---------------------------
* ACUMOS-760 : Validation failed if DataBroker input port connected any other node and showing the isolated model names also in error messages


Version 0.0.25, 2018-04-26
---------------------------
* ACUMOS-630 : Update the CDump and Blueprint structure for Databroker (BE) 


Version 0.0.24, 2018-03-25
---------------------------
* ACUMOS-547 : Design Studio stores the protobuf URI of PROTOBUF.json instead of .proto 


Version 0.0.23, 2018-03-09
---------------------------
* Update to use latest version of Common Data Service : 1.14.1.
* ACUMOS-291 Update API behavior : Validate Composite Solution w.r.t inclusion of Databroker
* ACUMOS-293 3.	An output port of a node can be connected to ONLY one input port of another node – add this restriction until we support split and join of links allowed in Design Studio. [NOTE: Design Studio will NOT restrict the user, but during Validation we will flag this error.]
* ACUMOS-294 4.	An input port of a node can be connected to ONLY one output  port of another node – add this restriction until we support split and join of links allowed in Design Studio. [NOTE: Design Studio will NOT restrict the user, but during Validation we will flag this error.]
* ACUMOS-295 5.	A node of type “DataBroker” cannot have its input port connected to any other node.
* ACUMOSE-335 Store the location of the docker image in the Blueprint.json file


Version 0.0.22, 2018-02-16
---------------------------
* Update to use latest version of Common Data Service : 1.13.1.
* ACUMOS-40 : View the on boarded Data Brokers and Training Clients in the Design Studio Palette under the Data Source drawer
* ACUMOS-47 EPIC - Create a composite solution with Data Broker, Training Client, and ML Models connected to each other
* ACUMOS-64 EPIC - Input a set of (multi – line) SQL Query statements or Filesystem scripts in the UI
* ACUMOS-126 EPIC - To validate the composite solution consisting of Data Broker, Training Client and ML Models
* ACUMOS-128 EPIC - Include details of Data Broker client in the blueprint
* ACUMOS-206 EPIC - Log Message Standardization 


Version 0.0.21, 2018-02-16
---------------------------
* Update to use latest version of Common Data Service : 1.13.0.
* ACUMOS-130 EPIC - Deploy Link from Design Studio to Market Place – Portal
* ACUMOSE-189  EPIC – Composite Solution with Probe indicator
* ACUMOSE-193 EPIC – Blueprint Generation



Version 0.0.20, 2018-02-14
---------------------------
* Update to use latest version of Common Data Service : 1.13.0.


Version 0.19.2, 2018-01-23
---------------------------
* Update to use latest version of Common Data Service : 1.12.0.


Version 0.19.1, 2018-01-15
---------------------------
* Update to use latest version of Common Data Service : 1.10.1
* Fix for CD-1972 : Clear functionality not working as expected w.r.t backend.
* Enhance the Building Blocks composition capability of the Design Studio
* Generic Data Mapper to connect two incompatible nodes having same number of fields


Version 0.0.19, 2018-01-10
---------------------------
* Update to use latest version of Common Data Service : 1.10.1


Version 0.0.18, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service


Version 0.0.17, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service
* Udpated as per the LF

Version 0.0.16, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service


Version 0.0.15, 2017-10-04
---------------------------
* Update to use latest version of Common Data Service

Version 0.0.14, 2017-09-28
---------------------------
* Code clean up


Version 0.0.11, 2017-09-28
---------------------------
* Updated the structure of the TGIF file


Version 0.0.10, 2017-09-28
---------------------------
* TGIF Request and Response , field "format" is JSON


Version 0.0.10, 2017-09-28
---------------------------
* GIF Request and Response , field "format" is JSON


Version 0.0.9, 2017-08-25
---------------------------
* to use latest version of Common Data Service 
* Auto generating protobuf to Json conversion


Version 0.0.8, 2017-08-04
---------------------------
* to upload the tgif.json file for the solutionID
* to use Common Data Service 1.1.3


Version 0.0.7, 2017-08-01
---------------------------
* changes to addopt solutionRevision changes


Version 0.0.6, 2017-07-27
---------------------------
* changes to accept the UserID as String instead of long


Version 0.0.5, 2017-07-11	
---------------------------
* Exception Handling


Version 0.0.4, 2017-07-01
---------------------------
* Fixed Integration Issues


Version 0.0.3, 2017-06-29
---------------------------
* Integrated with Nexus-Client and Common Data Micorservice Client

Version 0.0.2, 2017-06-28
---------------------------
* Updated version as its change in the API signature

Version 0.0.1, 2017-06-28
---------------------------
* Integrate TOSCA Model Generator Python Web Service & 2. process the response
* Invoke the library to store the files in Nexus 
* Invoke the Common Data Microservice putArtifact

