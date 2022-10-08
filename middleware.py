import anvil.server
from requests import get, post
import numpy as np

from time import time
from dateutil.parser import parse
from datetime import datetime
from datetime import timedelta

anvil.server.connect("V23YCA5UMIN3Q52VEH4TDDNU-26NAOBMYMLYZTIUC")

@anvil.server.callable
def connect(url):
  response = post(url + "/connect")
  return response.ok

@anvil.server.callable
def get_sampling_period(url):
  response = get(url + "/sampling-period")
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
  start_time = datetime.now()

  response = get(url + f"/{device}/accelerometer/last-values/{n}")
  if not response.ok:
    return response.ok, None, None, None

  lines = [] 
  Xs = []
  Ys = []
  Zs = []
  for entry in response.json():
    lines.append(f"{entry['dateTime']},{entry['x']:.5f},{entry['y']:.5f},{entry['z']:.5f}")
    Xs.append(round(entry['x'],5))
    Ys.append(round(entry['y'],5))
    Zs.append(round(entry['z'],5))

  list_to_string = "\n".join(lines)
  values_text = "Accelerometer\n" + list_to_string

  mean_x = np.mean(Xs)
  mean_y = np.mean(Ys)
  mean_z = np.mean(Zs)
  mean_text = f"Mean\nx: {mean_x:.5f}, y: {mean_y:.5f}, z: {mean_z:.5f}"
  
  var_x = np.var(Xs)
  var_y = np.var(Ys)
  var_z = np.var(Zs)
  variance_text = f"Variance\nx: {var_x:.5f}, y: {var_y:.5f}, z: {var_z:.5f}"
  end_time = datetime.now()
  print(f"Elapsed time for accelerometer: {compute_timedelta_ms(start_time, end_time):.1f} ms")
  return response.ok, values_text, mean_text, variance_text

@anvil.server.callable
def get_last_orientation_values(url, device, n):
  start_time = datetime.now()

  response = get(url + f"/{device}/orientation/last-values/{n}")
  if not response.ok:
    return response.ok, None, None, None

  lines = [] 
  azimuths = []
  pitches = []
  rolls = []
  for entry in response.json():
    lines.append(f"{entry['dateTime']},{entry['azimuth']:.5f},{entry['pitch']:.5f},{entry['roll']:.1f}")
    azimuths.append(round(entry['azimuth'],5))
    pitches.append(round(entry['pitch'],5))
    rolls.append(round(entry['roll'],5))

  list_to_string = "\n".join(lines)
  values_text = "Orientation\n" + list_to_string

  mean_az = np.mean(azimuths)
  mean_pi = np.mean(pitches)
  mean_ro = np.mean(rolls)
  mean_text = f"Mean\naz: {mean_az:.5f}, pi: {mean_pi:.5f}, ro: {mean_ro:.5f}"
  
  var_az = np.var(azimuths)
  var_pi = np.var(pitches)
  var_ro = np.var(rolls)
  variance_text = f"Variance\naz: {var_az:.5f}, pi: {var_pi:.5f}, ro: {var_ro:.5f}"
  end_time = datetime.now()
  print(f"Elapsed time for orientation: {compute_timedelta_ms(start_time, end_time):.1f} ms")
  return response.ok, values_text, mean_text, variance_text
  
@anvil.server.callable
def get_last_location_values(url, device, n):
  start_time = datetime.now()

  response = get(url + f"/{device}/location/last-values/{n}")
  if not response.ok:
    return response.ok, None, None, None

  lines = [] 
  latitudes = []
  longitudes = []
  for entry in response.json():
    lines.append(f"{entry['dateTime']},{entry['latitude']:.5f},{entry['longitude']:.5f}")
    latitudes.append(round(entry['latitude'],5))
    longitudes.append(round(entry['longitude'],5))

  list_to_string = "\n".join(lines)
  values_text = "Location\n" + list_to_string

  mean_lat = np.mean(latitudes)
  mean_long = np.mean(longitudes)
  mean_text = f"Mean\nlat: {mean_lat:.5f}, long: {mean_long:.5f}"
  
  var_lat = np.var(latitudes)
  var_long = np.var(longitudes)
  variance_text = f"Variance\nlat: {var_lat:.5f}, long: {var_long:.5f}"
  end_time = datetime.now()
  print(f"Elapsed time for location: {compute_timedelta_ms(start_time, end_time):.5f} ms")
  return response.ok, values_text, mean_text, variance_text

def compute_timedelta_ms(dt1, dt2):
  return (dt2 - dt1) / timedelta(milliseconds=1)

anvil.server.wait_forever()