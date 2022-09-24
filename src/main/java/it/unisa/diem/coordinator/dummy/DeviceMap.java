package it.unisa.diem.coordinator.dummy;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class DeviceMap {
	
	private HashMap<String, DeviceData> map;
	
	public DeviceMap() {
		map = new HashMap<>();
	}
	
	/**
	 * 
	 * @param deviceID
	 * @return false if the device is already present so it is not created again, true otherwise
	 */
	public boolean createNewDevice(String deviceID) {
		if(map.containsKey(deviceID))
			return false;
		else {
			map.put(deviceID, new DeviceData());
			return true;
		}
	}
	
	public String putAccelerometerValue(String deviceID, LocalDateTime dateTime, String value) {
		return map.get(deviceID).accelerometerData.put(dateTime, value);
	}
	
	public class DeviceData {

		private TreeMap<LocalDateTime, String> accelerometerData;
		private TreeMap<LocalDateTime, String> orientationData;
		private TreeMap<LocalDateTime, String> locationData;
		
		public DeviceData() {
			accelerometerData = new TreeMap<>();
			orientationData = new TreeMap<>();
			locationData = new TreeMap<>();
		}
		
		public String putAccelerometerValue(LocalDateTime dateTime, String value) {
			return accelerometerData.put(dateTime, value);
		}
		
		public String putOrientationValue(LocalDateTime dateTime, String value) {
			return orientationData.put(dateTime, value);
		}
		
		public String putLocationValue(LocalDateTime dateTime, String value) {
			return locationData.put(dateTime, value);
		}
		
		public LocalDateTime getLastAccelerometerKey() {
			return accelerometerData.lastKey();
		}
		
		public LocalDateTime getLastOrientationKey() {
			return orientationData.lastKey();
		}
		
		public LocalDateTime getLastLocationKey() {
			return locationData.lastKey();
		}
		
		public String getAccelerometerValue(LocalDateTime dateTime) {
			return accelerometerData.get(dateTime);
		}
		
		public String getOrientationValue(LocalDateTime dateTime) {
			return orientationData.get(dateTime);
		}
		
		public String getLocationValue(LocalDateTime dateTime) {
			return locationData.get(dateTime);
		}
		
		public Entry<LocalDateTime, String> pollLastAccelerometerValue() {
			return accelerometerData.pollLastEntry();
		}
		
		public Entry<LocalDateTime, String> pollLastOrientationValue() {
			return orientationData.pollLastEntry();
		}
		
		public Entry<LocalDateTime, String> pollLastLocationValue() {
			return locationData.pollLastEntry();
		}
	}
}


