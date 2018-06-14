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
Creates and dumps several demonstration models.
"""

from acumos.modeling import List, Model, NamedTuple
from acumos.session import AcumosSession

class ComputeInput(NamedTuple):
    '''Type representing an input to a computation'''
    f1: float
    f2: float
    s: str

class ComputeResult(NamedTuple):
    '''Type representing an output from a computation'''
    f: float
    s: str

class ComputeResultList(NamedTuple):
    '''Type representing a list of computation results'''
    l: List[ComputeResult]

def add(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the sum of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 + i.f2, i.s + ' sum')

def average(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the mean of the numeric arguments, and a revised string argument.'''
    return ComputeResult((i.f1 + i.f2)/2.0, i.s + ' mean')

def classify(dci: ComputeInput) -> ComputeInput:
    '''Passes thru a named tuple with two numeric values and one string value.'''
    return dci

def multiply(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the product of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 * i.f2, i.s + ' product')

def predict(l : ComputeResultList) -> ComputeResultList:
    '''Passes thru a list of named tuple, each with one numeric value and one string value.'''
    return l

def subtract(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the difference of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 - i.f2, i.s + ' difference')

# Dump each model for web on-boarding
session = AcumosSession()
# Should be possible to do this in a loop?
model = Model(add = add)
session.dump(model, 'dump_add', '.')
model = Model(average = average)
session.dump(model, 'dump_average', '.')
model = Model(classify = classify)
session.dump(model, 'dump_classify', '.')
model = Model(multiply = multiply)
session.dump(model, 'dump_multiply', '.')
model = Model(predict = predict)
session.dump(model, 'dump_predict', '.')
model = Model(predict = subtract)
session.dump(model, 'dump_subtract', '.')

# Example code

# On-board the model directly
#host='acumos-host-name.org'
#session=AcumosSession(push_api='https://{}/onboarding-app/v2/models'.format(host), auth_api='https://{}/onboarding-app/v2/models/auth'.format(host))
#print('on-boarding model to host {}'.format(host))
#session.push(model, 'my-model')
