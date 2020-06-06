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

=================================================
Design Studio Composition Engine Developer Guide
=================================================

1.	Overview 
=================

         This is the developers guide to Design Studio Composition Engine. 

1.1. What is Composition Engine\?
-----------------------------------

	The Design Studio UI invokes Composition Engine API to:

	1.	Create machine learning applications, hereafter referred to as composite solutions, out of the basic building blocks – the individual Machine Learning (ML) models contributed by the open source user community.

	2.	Validate the composite solutions.

	3.	Generate the blueprint of the composite solution for deployment on the target cloud.

2.	Architecture and Design 
=================================

2.1. High-Level Flow
----------------------
	Coming soon 

2.2. Class Diagrams
----------------------
	Coming soon 

2.3. Sequence Diagrams
--------------------------
	Coming soon

3. Technology and Frameworks
=============================
  **List of the development languages, frameworks, etc.**

  #. Springboot 1.5.16.RELEASE
  #. Java 8
  #. Maven 4.0.0
  #. Jackson 2.7.5
  #. JUnit 4.12

4.	Project Resources
==========================

- Gerrit repo: `desing-studio/ds-compositionengine <https://gerrit.acumos.org/r/#/admin/projects/design-studio>`_
- `Jira <https://jira.acumos.org/browse/ACUMOS-50?jql=component%20%3D%20design-studio>`_  design-studio

5. Development Setup
=====================

5.1. Get the code
---------------------
		
    Clone the Repository in some user accessible directory, lets call this as <homeDirectory>

	git clone --depth 1 https://<username>@gerrit.acumos.org/r/a/design-studio

    After successful clone, new directory <homeDirectory>/**design-studio** with following sub directories should get created.
	
	.. image:: images/design-studio_gerritRepo.jpg
	  :alt:	design-studio gerritRepository structure

	  
5.2. Import Project in Eclipse
-------------------------------
		
	After successful import, you should view in Project Explorer 
	
       .. image:: images/Eclipse_ds-compositionengine.jpg
	      :alt: design-studio ds-compositionengine structure.

6.	How to Run
===================

  Run as Sprintboot application.


7.	How to Test
====================

  **7.1 Run the JUnit testcases**  
  
  **7.2 Using Swagger UI**
    Start the ds-compositionengine as spring boot application and test the API using swagger UI :  http://localhost:8088/swagger-ui.html
	
	.. image:: images/design-studio_swaggerUI.jpg
	   :alt: DS swagger UI image.
