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

The Design Studio Composition Engine is deployed within a Docker image in the Docker registry.  TOSCAGenerator Client is uploaded in Nexus repository as a jar file.

0.0.27-SNAPSHOT, 2018-05-04
---------------------------
* ACUMOS-791 : Data is present in target table when there is no node or ML is directly connected to the databroker node.

0.0.26-SNAPSHOT, 2018-05-03
---------------------------
* ACUMOS-760 : Validation failed if DataBroker input port connected any other node and showing the isolated model names also in error messages

0.0.25-SNAPSHOT, 2018-04-26
---------------------------
* ACUMOS-630 : Update the CDump and Blueprint structure for Databroker (BE) 


0.0.24-SNAPSHOT, 2018-03-25
---------------------------
* ACUMOS-547 : Design Studio stores the protobuf URI of PROTOBUF.json instead of .proto 


0.0.23-SNAPSHOT, 2018-03-09
---------------------------
* Update to use latest version of Common Data Service : 1.14.1.
* ACUMOS-291 Update API behavior : Validate Composite Solution w.r.t inclusion of Databroker
* ACUMOS-293 3.	An output port of a node can be connected to ONLY one input port of another node – add this restriction until we support split and join of links allowed in Design Studio. [NOTE: Design Studio will NOT restrict the user, but during Validation we will flag this error.]
* ACUMOS-294 4.	An input port of a node can be connected to ONLY one output  port of another node – add this restriction until we support split and join of links allowed in Design Studio. [NOTE: Design Studio will NOT restrict the user, but during Validation we will flag this error.]
* ACUMOS-295 5.	A node of type “DataBroker” cannot have its input port connected to any other node.
* ACUMOSE-335 Store the location of the docker image in the Blueprint.json file


0.0.22-SNAPSHOT, 2018-02-16
---------------------------
* Update to use latest version of Common Data Service : 1.13.1.
* ACUMOS-40 : View the on boarded Data Brokers and Training Clients in the Design Studio Palette under the Data Source drawer
* ACUMOS-47 EPIC - Create a composite solution with Data Broker, Training Client, and ML Models connected to each other
* ACUMOS-64 EPIC - Input a set of (multi – line) SQL Query statements or Filesystem scripts in the UI
* ACUMOS-126 EPIC - To validate the composite solution consisting of Data Broker, Training Client and ML Models
* ACUMOS-128 EPIC - Include details of Data Broker client in the blueprint
* ACUMOS-206 EPIC - Log Message Standardization 


0.0.21-SNAPSHOT, 2018-02-16
---------------------------
* Update to use latest version of Common Data Service : 1.13.0.
* ACUMOS-130 EPIC - Deploy Link from Design Studio to Market Place – Portal
* ACUMOSE-189  EPIC – Composite Solution with Probe indicator
* ACUMOSE-193 EPIC – Blueprint Generation



0.0.20-SNAPSHOT, 2018-02-14
---------------------------
* Update to use latest version of Common Data Service : 1.13.0.


0.19.2-SNAPSHOT, 2018-01-23
---------------------------
* Update to use latest version of Common Data Service : 1.12.0.


0.19.1-SNAPSHOT, 2018-01-15
---------------------------
* Update to use latest version of Common Data Service : 1.10.1
* Fix for CD-1972 : Clear functionality not working as expected w.r.t backend.
* Enhance the Building Blocks composition capability of the Design Studio
* Generic Data Mapper to connect two incompatible nodes having same number of fields


0.0.19-SNAPSHOT, 2018-01-10
---------------------------
* Update to use latest version of Common Data Service : 1.10.1


0.0.18-SNAPSHOT, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service


0.0.17-SNAPSHOT, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service
* Udpated as per the LF

0.0.16-SNAPSHOT, 2017-11-16
---------------------------
* Update to use latest version of Common Data Service


0.0.15-SNAPSHOT, 2017-10-04
---------------------------
* Update to use latest version of Common Data Service

0.0.14, 2017-09-28
---------------------------
* Code clean up


0.0.11, 2017-09-28
---------------------------
* Updated the structure of the TGIF file


0.0.10-SNAPSHOT, 2017-09-28
---------------------------
* TGIF Request and Response , field "format" is JSON


0.0.10-SNAPSHOT, 2017-09-28
---------------------------
* GIF Request and Response , field "format" is JSON


0.0.9-SNAPSHOT, 2017-08-25
---------------------------
* to use latest version of Common Data Service 
* Auto generating protobuf to Json conversion


0.0.8-SNAPSHOT, 2017-08-04
---------------------------
* to upload the tgif.json file for the solutionID
* to use Common Data Service 1.1.3


0.0.7-SNAPSHOT, 2017-08-01
---------------------------
* changes to addopt solutionRevision changes


0.0.6-SNAPSHOT, 2017-07-27
---------------------------
* changes to accept the UserID as String instead of long


0.0.5-SNAPSHOT, 2017-07-11	
---------------------------
* Exception Handling


0.0.4-SNAPSHOT, 2017-07-01
---------------------------
* Fixed Integration Issues


0.0.3-SNAPSHOT, 2017-06-29
---------------------------
* Integrated with Nexus-Client and Common Data Micorservice Client

0.0.2-SNAPSHOT, 2017-06-28
---------------------------
* Updated version as its change in the API signature

0.0.1-SNAPSHOT, 2017-06-28
---------------------------
* Integrate TOSCA Model Generator Python Web Service & 2. process the response
* Invoke the library to store the files in Nexus 
* Invoke the Common Data Microservice putArtifact

