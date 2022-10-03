import anvil.server
from requests import get, post

anvil.server.connect("V23YCA5UMIN3Q52VEH4TDDNU-26NAOBMYMLYZTIUC")

@anvil.server.callable
def connect(url):
  response = post(url + "/connect")
  return response.ok

@anvil.server.callable
def get_sampling_period(url):
  response = get(url + "/sampling_period")
  return response.ok, response.json()

@anvil.server.callable
def set_configuration(url, sampling_period, accelerometer, orientation, location):
  json = {"samplingPeriod": sampling_period, "accelerometer": accelerometer, 
    "orientation": orientation, "location": location}
  response = post(url + "/set-configuration", json = json)
  return response.ok

@anvil.server.callable
def update_devices(url):
  response = get(url + "/devices")
  return response.ok, response.json()

@anvil.server.callable
def get_last_accelerometer_values(url, device, n):
  response = get(url + f"/{device}/accelerometer/last-values/{n}")
  return response.ok, response.json()

@anvil.server.callable
def get_last_orientation_values(url, device, n):
  response = get(url + f"/{device}/orientation/last-values/{n}")
  return response.ok, response.json()
  
@anvil.server.callable
def get_last_location_values(url, device, n):
  response = get(url + f"/{device}/location/last-values/{n}")
  return response.ok, response.json()

anvil.server.wait_forever()