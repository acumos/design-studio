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
Defines a passthru function as an Acumos model aka solution.
Can use a session to on-board this to an Acumos instance.
"""

from demo_types import ComputeInput
from acumos.modeling import Model
# from acumos.session import AcumosSession

def classify(dci: ComputeInput) -> ComputeInput:
    '''Passes thru a named tuple with two numeric values and one string value.'''
    return dci

model = Model(op=classify)

# Example code

# Serialize the model to allow web on-boarding
#session = AcumosSession()
#print('dumping model to current subdir')
#session.dump(model, 'my-model', '.')

# On-board the model directly
#host='acumos-host-name.org'
#session=AcumosSession(push_api='https://{}/onboarding-app/v2/models'.format(host), auth_api='https://{}/onboarding-app/v2/models/auth'.format(host))
#print('on-boarding model to host {}'.format(host))
#session.push(model, 'my-model')
