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

class ConcatenateInput(NamedTuple):
    '''Type representing an input to a string operation'''
    s: str
    d: float

class ConcatenateOutput(NamedTuple):
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

class ParmInput(NamedTuple):
    '''Type representing an input to a simple math operation'''
    f1: float
    f2: float

class ParmOutput(NamedTuple):
    '''Type representing an output from a simple math operation'''
    f: float

class ParmMessage(NamedTuple):
    difference: float 
    product: float
    average: float
    square: float 
    s: str

class SquareMessage(NamedTuple):
    '''Type representing an input or output in a computation'''
    d: float

# Methods

def add(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the sum of the numeric arguments, and a revised string argument.'''
    print('add: received {0}'.format(i))
    r = ComputeResult(i.f1 + i.f2, i.s + ' sum')
    print('add: returning {0}'.format(r))
    return r

def average(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the mean of the numeric arguments, and a revised string argument.'''
    print('average: received {0}'.format(i))
    r = ComputeResult((i.f1 + i.f2)/2.0, i.s + ' mean')
    print('average: returning {0}'.format(r))
    return r

def concatenate(i: ConcatenateInput) -> ConcatenateOutput:
    '''Stringify a double and concatenate it with the another string input'''
    print('concatenate: received {0}'.format(i))
    r = ConcatenateOutput(i.s + str(i.d))
    print('concatenate: returning {0}'.format(r))
    return r

def classify(i: ComputeInput) -> ComputeInput:
    '''Passes thru a named tuple with two numeric values and one string value.'''
    print('classify: received {0}'.format(i))
    r = i
    print('classify: returning {0}'.format(r))
    return r

def ingest(i: ComputeInput) -> ComputeInput:
    '''Returns its input.'''
    print('ingest: received {0}'.format(i))
    r = i
    print('ingest: returning {0}'.format(r))
    return r

def manipulate(i: ManipulateMessage) -> ManipulateMessage:
    '''Returns its input.'''
    print('manipulate: received {0}'.format(i))
    r = i
    print('manipulate: returning {0}'.format(r))
    return r

def multiply(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the product of the numeric arguments, and a revised string argument.'''
    print('multiply: received {0}'.format(i))
    r = ComputeResult(i.f1 * i.f2, i.s + ' product')
    print('multiply: returning {0}'.format(r))
    return r

def output(i: ManipulateMessage) -> ManipulateMessage:
    '''Returns its input.'''
    print('output: received {0}'.format(i))
    r = i
    print('output: returning {0}'.format(r))
    return r

def padd(i : ParmInput) -> ParmOutput:
    '''Returns a named tuple with the sum of the numeric arguments'''
    print('padd: received {0}'.format(i))
    r = ParmOutput(i.f1 + i.f2)
    print('padd: returning {0}'.format(r))
    return r

def paverage(i : ParmInput) -> ParmOutput:
    '''Returns a named tuple with the mean of the numeric arguments'''
    print('paverage: received {0}'.format(i))
    r = ParmOutput((i.f1 + i.f2) / 2.0)
    print('paverage: returning {0}'.format(r))
    return r

def pmultiply(i: ParmInput) -> ParmOutput:
    '''Returns a named tuple with the product of the numeric arguments'''
    print('pmultiply: received {0}'.format(i))
    r = ParmOutput(i.f1 * i.f2)
    print('pmultiply: returning {0}'.format(r))
    return r

def predict(i : ComputeResultList) -> ComputeResultList:
    '''Returns its input, a list of named tuple, each with one numeric value and one string value.'''
    print('predict: received {0}'.format(i))
    r = i
    print('predict: returning {0}'.format(r))
    return r

def poutput(i: ParmMessage) -> ParmMessage:
    '''Returns its input.'''
    print('poutput: received {0}'.format(i))
    r = i
    print('poutput: returning {0}'.format(r))
    return r

def psubtract(i: ParmInput) -> ParmOutput:
    '''Returns a named tuple with the difference of the numeric arguments'''
    print('psubtract: received {0}'.format(i))
    r = ParmOutput(i.f1 - i.f2)
    print('psubtract: returning {0}'.format(r))
    return r

def square(i: SquareMessage) -> SquareMessage:
    '''Returns a named tuple with the square of the numeric argument.'''
    print('square: received {0}'.format(i))
    r = SquareMessage(i.d * i.d)
    print('square: returning {0}'.format(r))
    return r

def subtract(i: ComputeInput) -> ComputeResult:
    '''Returns a named tuple with the difference of the numeric arguments, and a revised string argument.'''
    print('subtract: received {0}'.format(i))
    r = ComputeResult(i.f1 - i.f2, i.s + ' difference')
    print('subtract: returning {0}'.format(r))
    return r

# Test the methods

print('Test add')
ci = ComputeInput(1.0, 2.0, "string")
res = add(ci)
assert(res.f >= 3.0)

print('Test average')
ci = ComputeInput(1.0, 2.0, "string")
res = average(ci)
assert(res.f > 1 and res.f < 2)

print('Test concatenate')
ci = ConcatenateInput('str', 1.0)
res = concatenate(ci)
assert('str' in res.joined and '1.0' in res.joined)

print('Test classify')
ci = ComputeInput(1.0, 2.0, "string")
res = classify(ci)
assert(res.f1 == ci.f1 and res.f2 == ci.f2 and res.s == ci.s)

print('Test ingest')
ci = ComputeInput(1.0, 2.0, "string")
res = ingest(ci)
assert(res.f1 == ci.f1 and res.f2 == ci.f2 and res.s == ci.s)

print('Test manipulate')
ci = ManipulateMessage(1.0, 2.0, 3.0)
res = manipulate(ci)
assert(res.difference == ci.difference and res.product == ci.product and res.average == ci.average)

print('Test multiply')
ci = ComputeInput(2.0, 3.0, "string")
res = multiply(ci)
assert(res.f >= 6.0)

print('Test output')
ci = ManipulateMessage(1.0, 2.0, 3.0)
res = output(ci)
assert(res.difference == ci.difference and res.product == ci.product and res.average == ci.average)

print('Test padd')
ci = ParmInput(1.0, 2.0)
res = padd(ci)
assert(res.f >= 3.0)

print('Test paverage')
ci = ParmInput(1.0, 2.0)
res = paverage(ci)
assert(res.f > 1.0 and res.f < 2.0)

print('Test pmultiply')
ci = ParmInput(3.0, 4.0)
res = pmultiply(ci)
assert(res.f >= 12.0)

print('Test poutput')
ci = ParmMessage(1.0, 2.0, 3.0, 4.0, "string")
res = poutput(ci)
assert(res.difference > 0 and res.product > 0 and res.average > 0 and res.square > 0 and res.s == "string")

print('Test predict')
cr = ComputeResult(1.0, "str")
ci = ComputeResultList( [ cr ] )
res = predict(ci)
assert(res == ci)

print('Test psubtract')
ci = ParmInput(1.0, 2.0)
res = psubtract(ci)
assert(res.f < 1.0)

print('Test square')
ci = SquareMessage(2.0)
res = square(ci)
assert(res.d >= ci.d * ci.d)

print('Test subtract')
ci = ComputeInput(1.0, 2.0, "string")
res = subtract(ci)
assert(res.f < ci.f1 and res.f < ci.f2)

# Dump and on-board the methods as models
# This relies on the user entering a password on the command line
push='http://cognita-dev1-vm01-core.eastus.cloudapp.azure.com:8090/onboarding-app/v2/models'
auth='http://cognita-dev1-vm01-core.eastus.cloudapp.azure.com:8090/onboarding-app/v2/auth'
session=AcumosSession(push_api=push, auth_api=auth)

for f in add, average, concatenate, classify, ingest, manipulate, multiply, output, padd, paverage, pmultiply, poutput, predict, psubtract, square, subtract:
    d = { f.__name__: f }
    model = Model(**d)
    subdir = 'dump_' + f.__name__
    # if the dump dir exists, assume it was pushed also
    if os.path.isdir(subdir):
        print('Found dump of model {}, skipping'.format(f.__name__))
    else:
        print('Dumping model {}'.format(f.__name__))
        session.dump(model, subdir, '.')
        print('Pushing model {}'.format(f.__name__))
        session.push(model, f.__name__)
