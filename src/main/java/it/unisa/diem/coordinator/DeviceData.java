package it.unisa.diem.coordinator;

import java.util.Stack;

public class DeviceData {
	private Stack<AccelerometerMeasurement> accelerometerData;
	private Stack<OrientationMeasurement> orientationData;
	private Stack<LocationMeasurement> locationData;
	
	public DeviceData() {
		accelerometerData = new Stack<>();
		orientationData = new Stack<>();
		locationData = new Stack<>();
	}
	
	public AccelerometerMeasurement pushAccelerometer(AccelerometerMeasurement m) {
		return accelerometerData.push(m);
	}
	
	public OrientationMeasurement pushOrientation(OrientationMeasurement m) {
		return orientationData.push(m);
	}
	
	public LocationMeasurement pushLocation(LocationMeasurement m) {
		return locationData.push(m);
	}
	
	public AccelerometerMeasurement topAccelerometer() {
		return accelerometerData.peek();
	}
	
	public OrientationMeasurement topOrientation() {
		return orientationData.peek();
	}
	
	public LocationMeasurement topLocation() {
		return locationData.peek();
	}
	
	public AccelerometerMeasurement popAccelerometer() {
		if(!accelerometerData.empty())
			return accelerometerData.pop();
		else
			return null;
	}
	
	public OrientationMeasurement popOrientation() {
		if(!orientationData.empty())
			return orientationData.pop();
		else
			return null;
	}
	
	public LocationMeasurement popLocation() {
		if(!locationData.empty())
			return locationData.pop();
		else
			return null;
	}
	
	public boolean isAccelerometerEmpty() {
		return accelerometerData.empty();
	}
	
	public boolean isOrientationEmpty() {
		return orientationData.empty();
	}
	
	public boolean isLocationEmpty() {
		return locationData.empty();
	}
	
	@Override
	public DeviceData clone() {
		DeviceData copy = new DeviceData();
		copy.accelerometerData = (Stack<AccelerometerMeasurement>) this.accelerometerData.clone();
		copy.orientationData = (Stack<OrientationMeasurement>) this.orientationData.clone();
		copy.locationData = (Stack<LocationMeasurement>) this.locationData.clone();
		return copy;
	}
}
