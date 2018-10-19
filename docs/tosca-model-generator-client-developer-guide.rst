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

===========================================================
Design Studio TOSCA Model Generator Client Developer Guide
===========================================================

1. Overview 
=================

         This is the developers guide to Design Studio TOSCA Model Generator Client. 

1.1. What is TOSCA Model Generator Client\?
----------------------------------------------
	   
  1.  TOSCA Model Generator Client generates TOSCA models 
  2.  Is included in onboarding module as lib.  
  3.  The TOSCA Model generated are : **TGIF.json** & **Protobuf.json** while onboarding the models. 

2. Architecture and Design 
============================

2.1. High-Level Flow
----------------------


2.2. Class Diagrams
------------------------


2.3. Sequence Diagrams
-------------------------


3. Technology and Frameworks
=============================

  **List of the development languages, frameworks, etc.**

  #. Java 8
  #. Maven 4.0.0
  #. Jackson 2.7.5
  #. JUnit 4.12

4. Project Resources
==========================

- Gerrit repo: `desing-studio/TOSCAGeneratorClient <https://gerrit.acumos.org/r/#/admin/projects/design-studio>`_
- `Jira <https://jira.acumos.org/browse/ACUMOS-50?jql=component%20%3D%20design-studio>`_  design-studio

5. Development Setup
=======================

5.1. Get the code
---------------------
		
    Clone the Repository in some user accessible directory, lets call this as <homeDirectory>

	git clone https://<username>@gerrit.acumos.org/r/a/design-studio

    After successful clone, new directory <homeDirectory>/**design-studio** with following sub directories should get created.
	
	.. image:: images/design-studio_gerritRepo.jpg
	  :alt:	design-studio gerritRepository structure
	  

5.2. Import Project in Eclipse
--------------------------------
		
	After successful import, you should view in Project Explorer
	
    .. image:: images/Eclipse_TOSCAModelGeneratorClient.jpg
	   :alt: Eclipse Project Explorer for TOSCAModelGeneratorClient
		  

6. How to Run
===================
	NA


7. How to Test
====================
    Through **JUnit** test cases.