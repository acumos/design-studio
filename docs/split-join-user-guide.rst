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
Split-Join User Guide
=========================================

1. Target Users
=================

	Splitter and Collator is targeted towards user: 
	
	1.	To support the Directed Acyclic Graph (DAG) Topology in Design Studio.
	
	2.	To support both the split and join semantics.
	
	3.	To provide the  different collation semantics at join point.
	
2. Overview
===============

	This is a user’s guide that describes how to use the Splitter and Collator in Design Studio.
	
2.1 What is Splitter?
--------------------------
	
		1.	Splitter is a Design Studio tool, which is the user should be able to connect the one model to multiple models to support the message splitting (broadcast and parameter splitting capability) in Design Studio.
		
		2.	The Splitter should be listed in Data Transformation palette in the Design Studio.
		
		3.	The Splitter will be a new node of type called "Splitter", supported by the DS composition file and the toolkit type code is "SP".
		
		4.	Splitter will supports the user selectable splitting schemes.
	
2.2 Types of Splitting Semantics?
--------------------------------------
		
		1.	Copy-Based Splitting
		
		2.	Parameter-Based Splitting
	
2.3 What is Collator?
---------------------------
	
		1.	Collator is a Design Studio tool, which is the user should be able to connect the multiple models and combine the input from the models in to single output message in Design Studio.
		
		2.	Collator should be listed in the Data Transformation palette in the Design Studio.
		
		3.	The Collator will be a new node of type called "Collator", supported by the DS composition file and the toolkit type code is "CO".
		
		4.	Collator will supports the user selectable collation schemes.
		
2.4 Types of Collation Semantics?
----------------------------------------
		
		1.	Array-Based Collation
		
		2.	Parameter-Based Collation
		

3. Architecture
===================
	
	The Splitter and Collator Architecture with high level information will be there in the Developer Guide.
	
4. How to use Splitter and Collator
======================================

	Before going to how to use Splitter and Collator, will go to how to On-Board the Splitter and Collator.
	
4.1 How to On-Board the Splitter and Collator?
------------------------------------------------------
	
		1.	On-board the Splitter and Collator models which has been previously downloaded from an Acumos platform or obtained from somewhere else (where we can stage this is TBD), with files model.zip, metadata.json, and default.proto (NOT model.proto as normal).
		
		2.	While On-boarding the Splitter and Collator we can choose the ways of On-Board it, there are two ways,
			
			i.	CLI
			
			ii.	Web Based.
		
		3.	On-Boarding via Web based: 

			i. Upload Model bundle.
			
			While uploading the model bundle make sure that model.zip, metadata.json, default.proto should be there in that bundle which is of zip format. After successfully uploading, click on the On-Board Model.
			
			In this process many steps it will perform like creation of micro service, Dockerization, Add to Repository, Creation of TOSCA and finally it will show On-Boarded or not.
		
4.2 How to publish the Splitter and Collator?
------------------------------------------------------
	
		1.	From the Market Place, publish the models to the public/company marketplace, setting the values Model Category: Data Transformation & Toolkit Type: Splitter/ Collator respectively.
		
		2.	Now the Splitter and Collator will be there in Design Studio under the Data Transformation palette like shown in below.
		
			.. image:: images/Split-Join/CO and SPL.jpg
		
4.3 How to work with Splitter and Collator?
----------------------------------------------------
	
		1.	Create/load a solution in Design Studio.
		
		2.	Select the splitter and collator (latest version) from the list of "Data Transform tools" and drag onto the canvas.
		
		**For Splitter:**
			
			a.	After dragging the Splitter on the canvas, it should be displayed as rectangular icon and the input (on left hand side) and output (on right hand side) ports should be disabled.
			
			b.	On the right hand side in properties box, there will be a Splitter Scheme Selector, click on it then a dialogue pop up will appear and need to select the scheme of it. 
			
				.. image:: images/Split-Join/SplitterAfterDrag.jpg
			
			c.	There are two types of schema's are there in Splitter.
			
				i.	Copy-Based Splitting
				
				ii.	Parameter-Based Splitting
				
					.. image:: images/Split-Join/SplitterSelection.jpg
				
				**For Copy-Based Splitting:**
				
					If the schema is Copy-Based Splitting then the user will be able a establish a link to Splitter ports only after selecting the splitter scheme and Send (copy) the input message on all outgoing links. Make sure that both input and output message signatures are identical and both the input and output message data is same.
					
					If a Copy-based splitting scheme is selected, the "Splitter Map" button (present in the properties panel) will not be enabled  as it copies the complete message from the source model to all the target models connected to the splitter. As it is a copy-based splitting, if either input or output port is connected to a ML model, it acquires the message signature from the ML model on both the input and output ports.
					
						.. image:: images/Split-Join/CopyBasedSPLwithModels.jpg
			
					**Case 1: Splitter Input Port is connected first:**
			
					If the input port of the Splitter is connected first to the output port of the (producing) Model, then
				
						i.	Splitter must display the message signature of the output port of the producing model on its input port.
				
						ii.	When the output ports are connected later on, Splitter must make sure that all output messages have the same message signature as the message signature of the input message, otherwise the connection should not be allowed.
				
					**Case 2: Splitter output Port is connected first:**
				
					If the output port of the Splitter is connected first then,
				
						i.	The Splitter should allow the first outgoing link to be connected to its output port without any validation, and make a temporary copy of its message signature.
				
						ii.	For the second and subsequent links that are connected to the output port, the Splitter must make sure that their message signature is the same as that of the first message signature, otherwise the connection should not be allowed.
				
						iii. When the input port is connected later on, the Splitter must make sure that its message signature is the same as that of output message signature on its output port, otherwise the connection should not be allowed.
			
				**For Parameter-Based Splitting:**
				
					If the schema is Parameter-Based Splitting then Split the input message, based on its signature, into (top – level) parameters and send different parts / parameters on different outgoing links. Make sure that Input and output message signatures are different and the collection (i.e., specific arrangement) of output message signatures represents the input message signature.
					
					If a parameter-based Splitting scheme is selected, the "Splitter Map" button will be enabled.The user must connect one model at the input and one or more models at the output port of the splitter. Once the input and output ports are connected, the source and target tables are auto populated that can be viewed when the user clicks on Splitter map button.
					
						.. image:: images/Split-Join/ParameterBasedSPLWithModel.jpg
					
					**Case 1: Splitter Input Port is connected first:**
					
					If the input port of the Splitter is connected first to the output port of the (producing) Model, then
					
						i.	Splitter must display the message signature of the output port of the producing model on its input port.
						
						ii.	When the output port is connected later on, Splitter’s output port should remain ANY.
					
					**Case 2: Splitter output Port is connected first:**
					
					If the output port of the Splitter is connected first then
					
						i.	The Splitter should allow all the outgoing link(s) to be connected to its output port without any validation.
						
						ii.	When the input port is connected later on, the Splitter should allow only one incoming link to be connected to its input port without any message signature validation.
						
					The parameter-based splitter should split the input message into first-level parameters and Copy the required input parameters on each of its outgoing link based on the information in the “splitter map”.Arrange these parameters in a sequence based on the parameter ordering information in the message signature on that outgoing link.Splitter should aggregate all parameters that needs to be sent to a single target in a single message.
					
					The Parameter–based splitter should perform binary to native format conversion before collation and native to binary conversion after collation.
					
			d.	There is a mapping area in the Splitter Map popup, which allows the user to copy a source field (parameter) to the target field(s) (parameter(s)). It is a dropdown having all the source table tags. All the Target side fields must be mapped for a successfull validation. Atleast one source field should be mapped to a target field.A source field may be mapped to multiple target fields.
				
			e.	For every mapping, there is a Error Validator that helps the user know if the mapping is valid or invalid (i,e. if the parameter types on both sides match).
			
				.. image:: images/Split-Join/SplitterMappingDetailsValid.jpg
			
			f.	If any of the mapping is invalid, then validation and blueprint generation will fail.
			
				.. image:: images/Split-Join/SplitterMappingDetailsError.jpg
			
		
		**For Collator:**
		
			a.	After dragging the Collator on the canvas, it should be displayed as rectangular icon and the input (on left hand side) and output (on right hand side) ports should be disabled.
			
			b.	On the right hand side in properties box, there will be a Collator Scheme Selector, click on it then a dialogue pop up will appear and need to select the scheme of it. 
			
				.. image:: images/Split-Join/CollatorAfterDrag.jpg
			
			c.	There are two types of schema's are there in Collator.
			
				i.	Array-Based Collation
				
				ii.	Parameter-Based Collation
				
					.. image:: images/Split-Join/CollatorSelectionSchema.jpg
				
				**For Array-Based Collation:**
				
					If the schema is Array-Based Collation then each incoming link provides complete message data, output the collection (an array) of all input message data. Each input message signature is same, but message content (data) may be different and Output message signature is a collection (i.e., an array, or a repeated structure) of input message signatures.
					
					If an Array-based Collation scheme is selected, the "Collator map" button will not be enabled. The output port of Collator only connects to a model which has a repeated complex message signature of the message at the input port (i.e., if the message signature at input port is "M", the message signature of the output port is "repeated (M)". ALL links connected to the input port must carry the same message signature "M".
					
					That means the output message signature is an array of input message signature (on the input links) which are of same message type. If either of one of the input or the output  port of the Collator is connected to an ML Model, then the input port acquire the message signature "M" and the output port acquires the message signature "repeated(M)".
					
						.. image:: images/Split-Join/ArrayBasedCollatorWithModels.jpg
					
					**Case 1: Collator Output Port is connected first:**
			
					If the output port of the Collator is connected first to the input port of the (consuming) Model, then
				
						i.	Collator must display the message signature of the input port of the consuming model on its output port. Note that this will be a repeated Protobuf data type.
				
						ii.	When the input ports are connected (later on), Collator must make sure that all input messages have the same message signature as message signature of the output message except that input should not be an repeated type, otherwise that connection should not be permitted.
						
					**Case 2: Collator Input Port is connected first:**
				
					If the input port of the Collator is connected first,
				
						i.	The Collator should allow the first incoming link to be connected to its input port without any validation, and make a temporary copy of its message signature.
				
						ii.	For the second and subsequent links that are connected to the input port, the Collator must make sure that their message signature is the same as that of the first message signature, otherwise the connection should not be allowed.
				
						iii. When the output port is connected later on, the Collator must make sure that its message signature is the same as that of repeated (input message signature), otherwise the connection should not be allowed.
						
					The Collator should wait until all messages are received on all of its input ports, based on the incoming link information in cdump file.
					
					When all the messages have been received, the Collator should convert the binary messages into native format and construct an array of the input messages.Collator should convert the array of input messages into a protobuf repeated message structure before delivering it on the output port.
					
				**For Parameter-Based Collation:**
				
					If a Parameter-based collation scheme is selected, the "Collator map" button will be enabled. The user must connect one model at the output port and one or more models at the input port. 
				
					Once the input and output ports are connected, the source and target tables are auto-populated and can be viewed by clicking on the Collator map button. As it is parameter-based collation, Collator output port acquires the message signature of the input port of the ML model connected to it and collator input port remains "ANY" which means any can be connected to it.
					
						.. image:: images/Split-Join/ParameterBasedCOWithModels.jpg
				
					**Case 1 : Collator Output port is connected first:**
				
						i.	The output port of Collator should acquire the message signature of the input port of the Model, then collator’s source table should be auto populated with details viz., the name of the source, parameter name, parameter type, its tag number and an initially empty mapping field in the collator map, based on the information contained in the protobuf file of the source.
					
						ii.	Collator should analyse the output port message signature and split it into its component parts (i.e., into parameters which have tag numbers associated to them).
					
					**Case 2 : Collator Input Port is connected first:**
				
						i.	In this case the input port of Collator should remain as ANY
				
						ii.	Collator’s target table should be auto populated with details the parameter name(s), parameter type(s)  parameter tag number(s), and the mapping field should be populated with the list of output tag numbers, based on the information contained in the protobuf file of the target.
					
			d.	There is a mapping area in the Collator Map pop up, which allows the user to map (i.e., copy) a source field to a target field. It is a drop down having all the target table tags.
			
			e. 	All the Target side fields must be mapped for a successful validation. At least one field from each source should be mapped to a target field, otherwise a validation error will be displayed.
			
			f.	Multiple source fields cannot be mapped to the same target field. A source field cannot be mapped to more than one target field. (mapping table will not allow this)
			
			g.	For every mapping, there is a error validator that helps the user know if the mapping is valid or invalid (i,e. if the parameter types on both sides match). If any of the mapping is invalid, then validation and blueprint generation will fail.
			
				.. image:: images/Split-Join/CollatorMappingDetailsError.jpg
			
		3.	Once the splitter / collator mappings is done, the user may select the 'Save' button and enter the details of the solution, in order to save the solution. (This will be saved in "My Solution" area).
		
		4.	Select the "Validate" button to generate the blueprint.
			
				.. image:: images/Split-Join/CollatorMappingDetailsValid.jpg
		
		5.	If validation is successful, then the Deploy button is enabled. On click of any of the cloud platform, the user will be redirected to "manage my model" -> "Deploy to cloud".
				
4.4 Validation Rules for Splitter and Collator?
-------------------------------------------------------
		
		**For Parameter-Based Splitter:**
			
			i.	The Splitter will allow a mapping between a pair of source and target parameters only if their message signatures match, otherwise an error should be indicated in the mapping area to allow the user to correct it.  (Alternatively show Pop Up when the mapping is invalid).
			
			ii.	A parameter on the source side can be mapped to more than one parameter/tag on the target side as long as target parameters belong to different target models.
			
			iii.	Two or more parameters from the source cannot be mapped to the same parameter/tag in the target message.
			
			iv.	When no parameters from the source are mapped to the parameters on target message, them the Splitter should show an error until the source model is deleted or at least one of the source side parameters is mapped to a target side parameter.
			
			v.	Splitter must make sure that all parameters on the target side models have been mapped to their matching source side parameters, otherwise an error should be shown in the mapping area, until this condition is satisfied.
			
			vi.	When both the source and target side parameters have been mapped correctly, the error mark should be taken away.
			
			vii.	The Splitter input port should have only one incoming link.
			
			viii.	The Splitter output port can have one or more outgoing links (a single outgoing link case is possible if this link provides all parameters required by the single target model.)
			
		**For Copy-Based Splitter :**
		
			i.	A copy – based message splitter can have one or more links connected at its output port. [Note: The case of one link at the output port does not make sense to include a splitter, but it is allowed.]
			
			ii.	The Splitter can have only one link connected at its input port.
			
			iii.	The copy based Splitter must have the same message signature for messages coming out of its  output port into all the outgoing links.
			
			iv.		The message signature at the input and output port of the Splitter must be the same.
			
			v.	The output of a Splitter cannot be connected to the input of a Collator.
			
		**For Parameter-Based Collator:**
			
			i.	The collator will allow a mapping between a pair of source and target parameters only if their message signatures match, otherwise an error should be indicated in the mapping area to allow the user to correct it.  (Alternatively show Pop Up when the mapping is invalid).
			
			ii.	A parameter on the source side cannot be mapped to more than one tag on the target side.
			
			iii. Two or more parameters from the source cannot be mapped to the same tag in the target message.
			
			iv.	Multiple parameters from a single data source (i.e., Model) may map to different tags in the target message.
			
			v.	When no parameters from a source are mapped to the target message (figure – 4), them the Collator should show an error until that data source is deleted or one of the parameters is mapped.
			
			vi.	Collator must make sure that at least one parameter from each source  have been mapped to their corresponding target side tags, otherwise an error should be shown in the mapping area, until this condition is satisfied (i.e., that link is removed and therefore the corresponding un necessary entries are removed).
			
			vii. Collator must make sure that all target side parameters have been mapped, otherwise an error should be shown against those entries in the mapping.
			
			viii. When both the source and target side parameters have been mapped correctly, the error mark should be taken away.
			
			ix.	The output port should have only one outgoing link.
			
			x.	The input port can have one or more links (a single link case is possible if this link provides more parameters than that required by collator’s output port).
			
		**For Array-Based Collator:**
		
			i.	An array – based collator can have one or more links connected at its input port. [Note: In case of a single input link the user may want to convert a Model’s output message into an “array of message” structure before feeding it to the target model which only accepts an array structure.]

			ii.	The Collator can have only one link connected at its output port.
			
			iii.	The array based collator must have the same message signature for messages arriving at its input port from all the incoming links.
			
			iv.	The output port of an array based collator must have a “repeated” structure of the message signature of its incoming links.
			
			v.	The output of a Collator cannot be connected to the input of a Splitter.
			
4.5 Features of Splitter and Collator?
-----------------------------------------------
		
		**Features of Message Collator:**
		
			1.	The Collator is a new node type, called “Collator”,  supported by the DS composition file.
			
			2.	Collator will support user selectable collation schemes.
			
			3.	The Message Collator can accept a variable number of inputs (N) and produces a single output.
			
			4.	The number of inputs is determined dynamically at run time in Design Studio.
			
			5.	The Collator will be represented as a rectangular icon.
			
			6.	The input port will be supported on the left hand side of the collator box.
			
			7.	The input port can support one or more incoming links (Validation). One in case where the output message has a subset of input parameters provided by one incoming link.
			
			8.	The output port will be supported on the right hand side of the collator box.
			
			9.	The output port will support only one outgoing link. (Validation).
			
			10.	The top side will have two ports – Collation Scheme Selection port and Collation Map port, described later.
			
			11.	Collator will support the addition and deletion of its input links and its output link.
			
			12.	Collator needs to perform
				
				i.	Un - marshalling of input protobuf messages into native format.
				
				ii.	Collation of un marshalled messages according to the collation scheme selected by the user.
				
				iii.	Marshall the collated message back into Protobuf format before sending on its output port

		**Features of Message Splitter:**
		
			1.	The Splitter will be a new node type, called “Splitter”,  supported by the DS composition file. The toolKitType code = “SP”.
			
			2.	Splitter will support user selectable splitting schemes.
			
			3.	The Message Splitter can accept a single input message  and produces multiple output messages of the same or different type, depending upon the splitting scheme.
			
			4.	The number of outputs is determined dynamically at run time in Design Studio.
			
			5.	The Splitter will be represented as a rectangular icon.
			
			6.	The input port will be supported on the left hand side of the Splitter box.
			
			7.	The input port will support only one incoming link (Validation).
			
			8.	The output port will be supported on the right hand side of the Splitter box.
			
			9.	The output port can support one or more outgoing links (Validation). One in case where the output message has a subset of input parameters.
			
			10.	The top side will have two ports – Splitter Scheme Selection port and Splitter Map port, described later.
			
			11.	Splitter will support the addition and deletion of its input links and its output link.
			
			12.	Splitter needs to perform:
				
				i.	 Un - marshalling of input protobuf message into native format.
				
				ii.	Splitting of the un marshalled message according to the splitting scheme selected by the user.
				
				iii.	Marshall the output messages back into Protobuf format before sending on its output port.
		
						


			
			

	