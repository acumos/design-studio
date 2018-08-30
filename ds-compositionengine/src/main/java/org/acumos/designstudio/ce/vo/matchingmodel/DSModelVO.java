/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */


package org.acumos.designstudio.ce.vo.matchingmodel;

import java.io.Serializable;
import java.util.List;

import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;

public class DSModelVO implements Serializable {

	private static final long serialVersionUID = 8655445351818857907L;

	private MLPSolution mlpSolution;
	private List<MLPSolutionRevision> mlpSolutionRevisions;
	
	/**
	 * @return the mlpSolution
	 */
	public MLPSolution getMlpSolution() {
		return mlpSolution;
	}
	/**
	 * @param mlpSolution the mlpSolution to set
	 */
	public void setMlpSolution(MLPSolution mlpSolution) {
		this.mlpSolution = mlpSolution;
	}
	/**
	 * @return the mlpSolutionRevisions
	 */
	public List<MLPSolutionRevision> getMlpSolutionRevisions() {
		return mlpSolutionRevisions;
	}
	/**
	 * @param mlpSolutionRevisions the mlpSolutionRevisions to set
	 */
	public void setMlpSolutionRevisions(List<MLPSolutionRevision> mlpSolutionRevisions) {
		this.mlpSolutionRevisions = mlpSolutionRevisions;
	}
	
	
}
