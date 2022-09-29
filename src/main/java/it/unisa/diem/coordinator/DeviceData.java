package it.unisa.diem.coordinator;

import java.time.LocalDateTime;
import java.util.Stack;

public class DeviceData {
	private Stack<String> accelerometerData;
	private Stack<String> orientationData;
	private Stack<String> locationData;
	
	public DeviceData() {
		accelerometerData = new Stack<>();
		orientationData = new Stack<>();
		locationData = new Stack<>();
	}
	
	public String pushAccelerometer(LocalDateTime dateTime, String value) {
		return accelerometerData.push(dateTime + "," + value);
	}
	
	public String pushOrientation(LocalDateTime dateTime, String value) {
		return orientationData.push(dateTime + "," + value);
	}
	
	public String pushLocation(LocalDateTime dateTime, String value) {
		return locationData.push(dateTime + "," + value);
	}
	
	public String topAccelerometer() {
		return accelerometerData.peek();
	}
	
	public String topOrientation() {
		return orientationData.peek();
	}
	
	public String topLocation() {
		return locationData.peek();
	}
	
	public String popAccelerometer() {
		if(!accelerometerData.empty())
			return accelerometerData.pop();
		else
			return null;
	}
	
	public String popOrientation() {
		if(!orientationData.empty())
			return orientationData.pop();
		else
			return null;
	}
	
	public String popLocation() {
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
}
