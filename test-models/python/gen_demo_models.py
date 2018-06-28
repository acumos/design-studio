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

import os, shutil
from acumos.modeling import List, Model, NamedTuple
from acumos.session import AcumosSession

# Named Tuples

class CatenateInput(NamedTuple):
    '''Type representing an input to a string operation'''
    s: str
    d: float

class CatenateOutput(NamedTuple):
    '''Type representing an output from a string operation'''
    joined: str

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

class ManipulateMessage(NamedTuple):
    '''Type representing an input or output in a value manipulation'''
    difference: float 
    product: float
    average: float

class SquareMessage(NamedTuple):
    '''Type representing an input or output in a computation'''
    d: float

# Methods

def add(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the sum of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 + i.f2, i.s + ' sum')

def average(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the mean of the numeric arguments, and a revised string argument.'''
    return ComputeResult((i.f1 + i.f2)/2.0, i.s + ' mean')

def catenate(i: CatenateInput) -> CatenateOutput:
    '''Stringify a double and concatenate it with the another string input'''
    return CatenateOutput(i.s + str(i.d))

def classify(dci: ComputeInput) -> ComputeInput:
    '''Passes thru a named tuple with two numeric values and one string value.'''
    return dci

def ingest(i: ComputeInput) -> ComputeInput:
    '''Returns its input.'''
    return i

def manipulate(i: ManipulateMessage) -> ManipulateMessage:
    '''Returns its input.'''
    return i

def multiply(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the product of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 * i.f2, i.s + ' product')

def output(i: ManipulateMessage) -> ManipulateMessage:
    '''Returns its input.'''
    return i

def predict(l : ComputeResultList) -> ComputeResultList:
    '''Passes thru a list of named tuple, each with one numeric value and one string value.'''
    return l

def square(i: SquareMessage) -> SquareMessage:
    '''Returns a named tuple with the square of the numeric argument.'''
    return ComputeResult(i.d * i.d)

def subtract(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the difference of the numeric arguments, and a revised string argument.'''
    return ComputeResult(i.f1 - i.f2, i.s + ' difference')

#session=AcumosSession()
# This relies on the user entering a password on the command line
push='http://cognita-dev1-vm01-core.eastus.cloudapp.azure.com:8090/onboarding-app/v2/models'
auth='http://cognita-dev1-vm01-core.eastus.cloudapp.azure.com:8090/onboarding-app/v2/auth'
session=AcumosSession(push_api=push, auth_api=auth)

for f in add, average, catenate, classify, ingest, manipulate, multiply, output, predict, square, subtract:
    d = { f.__name__: f }
    model = Model(**d)
    subdir = 'dump_' + f.__name__
    # if the dump dir exists, assume it was pushed also
    if os.path.isdir(subdir):
        print('Found dump dir, skipping {}'.format(f.__name__))
    else:
        print('Dumping model {}'.format(f.__name__))
        session.dump(model, subdir, '.')
        print('Pushing model {}'.format(f.__name__))
        session.push(model, f.__name__)
