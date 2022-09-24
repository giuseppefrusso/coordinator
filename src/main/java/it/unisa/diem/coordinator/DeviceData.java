package it.unisa.diem.coordinator;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.Map.Entry;

public class DeviceData {
	private TreeMap<LocalDateTime, String> accelerometerData;
	private TreeMap<LocalDateTime, String> orientationData;
	private TreeMap<LocalDateTime, String> locationData;
	
	public DeviceData() {
		accelerometerData = new TreeMap<>();
		orientationData = new TreeMap<>();
		locationData = new TreeMap<>();
	}
	
	public String putAccelerometer(LocalDateTime dateTime, String value) {
		return accelerometerData.put(dateTime, value);
	}
	
	public String putOrientation(LocalDateTime dateTime, String value) {
		return orientationData.put(dateTime, value);
	}
	
	public String putLocation(LocalDateTime dateTime, String value) {
		return locationData.put(dateTime, value);
	}
	
	public LocalDateTime lastAccelerometerKey() {
		return accelerometerData.lastKey();
	}
	
	public LocalDateTime lastOrientationKey() {
		return orientationData.lastKey();
	}
	
	public LocalDateTime lastLocationKey() {
		return locationData.lastKey();
	}
	
	public String getAccelerometer(LocalDateTime dateTime) {
		return accelerometerData.get(dateTime);
	}
	
	public String getOrientation(LocalDateTime dateTime) {
		return orientationData.get(dateTime);
	}
	
	public String getLocation(LocalDateTime dateTime) {
		return locationData.get(dateTime);
	}
	
	public Entry<LocalDateTime, String> pollLastAccelerometerEntry() {
		return accelerometerData.pollLastEntry();
	}
	
	public Entry<LocalDateTime, String> pollLastOrientationEntry() {
		return orientationData.pollLastEntry();
	}
	
	public Entry<LocalDateTime, String> pollLastLocationEntry() {
		return locationData.pollLastEntry();
	}
	
	public boolean isAccelerometerEmpty() {
		return accelerometerData.isEmpty();
	}
	
	public boolean isOrientationEmpty() {
		return orientationData.isEmpty();
	}
	
	public boolean isLocationEmpty() {
		return locationData.isEmpty();
	}
}
