# Defines a trivial function as an Acumos model aka solution.
# Can use a session to on-board this to an Acumos instance.

from acumos.modeling import Model
# from acumos.session import AcumosSession

def subtract(f1: float, f2: float) -> float:
    '''Returns the difference of the two arguments.''' 
    return f1 - f2

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
