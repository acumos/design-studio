#! python
# -*- coding: utf-8 -*-
# ===============LICENSE_START=======================================================
# Acumos Apache-2.0
# ===================================================================================
# Copyright (C) 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
# ===================================================================================
# This Acumos software file is distributed by AT&T and Tech Mahindra
# under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# This file is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ===============LICENSE_END=========================================================
"""
Defines a trivial function as an Acumos model aka solution.
Can use a session to on-board this to an Acumos instance.
"""

from demo_compute_result import DemoComputeResult
from acumos.modeling import Model
# from acumos.session import AcumosSession

def subtract(f1: float, f2: float, s: str) -> DemoComputeResult:
    '''Returns a named tuple with the difference of the numeric arguments, and the string argument.'''
    return DemoComputeResult(f1 - f2, s)

model = Model(op=subtract)

# Example code

# Serialize the model to allow web on-boarding
#session = AcumosSession()
#print('dumping model to current subdir')
#session.dump(model, 'my-model', '.')

# On-board the model directly
#host='acumos-host-name.org'
#session=AcumosSession(push_api='http://{}/upload'.format(host), auth_api='http://{}/auth'.format(host))
#print('on-boarding model to host {}'.format(host))
#session.push(model, 'my-model')
