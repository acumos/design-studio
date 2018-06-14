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

from demo_types import ComputeInput, ComputeResult
from acumos.modeling import Model
from acumos.session import AcumosSession, Requirements

def subtract(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the difference of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 - i.f2, i.s + ' difference')

reqs = Requirements(packages=['demo_types'])
model = Model(op=subtract)

# Serialize the model for web on-boarding
session = AcumosSession()
basename = __file__.split('/')[-1] + '_dump'
print('Using session to dump model to subdir {}'.format(basename))
session.dump(model, basename, '.')

# Example code

# On-board the model directly
#host='acumos-host-name.org'
#session=AcumosSession(push_api='https://{}/onboarding-app/v2/models'.format(host), auth_api='https://{}/onboarding-app/v2/models/auth'.format(host))
#print('on-boarding model to host {}'.format(host))
#session.push(model, 'my-model')
