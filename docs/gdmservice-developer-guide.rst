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

=========================================================
Design Studio Generic Datamapper Service Developer Guide
=========================================================

1. Overview
=================

          This is the developers guide to Design Studio Generic Datamapper Service.

1.1. What is Datamapper\?
---------------------------
	   
    The Datamapper performs data type transformations between Protobuf data types.
       - The Data Mapper is a Design Studio tool. Unlike other ML Models that have a Protobuf file associated with them, the Data - Mapper does not have a pre-defined Protobuf file associated with it.
       - The Data Mapper is implemented as a small java package which primarily consists of data type mapping libraries.
       - The data mappings inside the Data Mapper are defined in the Design Studio during “run time” of the Data Mapper jar file. - These dynamically defined mappings need to be saved in the Data Mapper package

1.2. What does it do\?
------------------------
		
        1. Maps data types between a pair of incompatible ports of the ML Models – map the data type of an output port to the data types of an input port.
		
        2. Any output port of a ML Model can be connected to a Data Mapper, and the Data Mapper can be connected to any input port of the ML Model.
		
		3. Composition Rule: From the Design Studio composition perspective a Data Mapper can accept any inputs and produce any outputs, depending on the ML models that are connected to its input and output side. So its requirements and capability will be indicated any.
		
		4. Data Mapper will perform transformation between basic Protobuf types only.

1.3. How it works\?
----------------------
		
        For Composite solution containing datamapper, ds-composition engine performs below steps before creating Blueprint file :

                1. Constructs FieldMapping.json details
                2. Generates and compile the Protobuf java classes depending on the input and output field details.
                3. Unpack the gdmservice downloaded jar and updated above details and repacks into new jar.
                4. Construct the docker image of newly packed jar and uploaded it to the configured docker registry.
                5. New docker image URI (from above step) is set in the Blueprint file for the corresponding datamapper.

2.	Architecture and Design
===============================

2.1. High-Level Flow
----------------------


2.2. Class Diagrams
----------------------


2.3. Sequence Diagrams
------------------------


3. Technology and Frameworks
=============================

  **List of the development languages, frameworks, etc.**

  #. Java 8
  #. Maven 4.0.0
  #. Jackson 2.7.5
  #. JUnit 4.12

4. Project Resources
========================

- Gerrit repo: `desing-studio/gdmservice <https://gerrit.acumos.org/r/#/admin/projects/design-studio>`_


5. Development Setup
======================

5.1. Get the code
---------------------
		 
    Clone the Repository in some user accessible directory, lets call this as <homeDirectory>

       git clone --depth 1 https://<username>@gerrit.acumos.org/r/a/design-studio

    After successful clone, new directory <homeDirectory>/**design-studio** with following sub directories should get created.
	
	.. image:: images/design-studio_gerritRepo.jpg
	  :alt:	design-studio gerritRepository structure

5.2. Import Project in Eclipse
--------------------------------

       After successful import, you should view in Project Explorer
	   
       .. image:: images/Eclipse_gdmservice.jpg
          :alt: design-studio gerritRepository structure.

6. How to Run
=====================

       **Run the project as Springboot application:**
       	   
       Update the FieldMapping.json as per the requirement.
         Below is the sample JSON :

       Start gdmservice as Springboot application service and test the application through Swagger UI.
         URL : http://localhost:8334/gdmservice/swagger-ui.html
         Below is the sample input for the above FieldMapping.json details.


7. How to Test
========================

  **Using Junit**
    You can either run all OR the required Junit to test the code.
