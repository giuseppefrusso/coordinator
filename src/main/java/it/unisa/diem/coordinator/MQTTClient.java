package it.unisa.diem.coordinator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MQTTClient {

	private IMqttClient client;
	private boolean connected;
	private String broadcastTopic;
	
	private int samplingPeriod;
	
	private HashMap<String, DeviceData> devices;
	private DateTimeFormatter formatter;
	
	public MQTTClient(String serverURI, String clientID, String broadcastTopic) throws MqttException {
		
		this.client = new MqttClient(serverURI, clientID);
		this.connected = false;
		this.broadcastTopic = broadcastTopic;
		this.devices = new HashMap<>();
		this.formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss.SSS");
	}
	
	public void connect(String username, String password, int connectionTimeout) throws MqttSecurityException, MqttException {
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(connectionTimeout);
		
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		
		System.out.println("Connecting...");
		client.connect(options);
		client.setCallback(new MqttCallback() {

			@Override
			public void connectionLost(Throwable cause) {
				// TODO Auto-generated method stub
				System.out.println("Connection lost! Please re-connect before doing anything");
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(String.format("MESSAGE FROM CONNECTION (%s): %s", topic, message.toString()));
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				// TODO Auto-generated method stub
				
			}
			
		});
		connected = true;
		System.out.println("Connected!");
	}
	
	public void disconnect() throws MqttException {
		System.out.println("Disconnecting...");
		client.disconnect();
		System.out.println("Disconnected!");
	}
	
	protected void subscribeForDiscoveringDevices() throws MqttSecurityException, MqttException {

		System.out.println("Discovering new devices...");
		client.subscribe(broadcastTopic + "/newDevice", new IMqttMessageListener() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				String newDeviceID = message.toString();
				if(devices.containsKey(newDeviceID)) {
					System.out.println(newDeviceID + " already present!");
				} else {
					devices.put(newDeviceID, new DeviceData());
					System.out.println(String.format("New device discovered: %s! Subscribing at its domain topics...", newDeviceID));
					try {
						subscribeAtDomainTopics(newDeviceID);
					} catch (ConfigurationException | MqttException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Subscribed!");
				}
			}
			
		});
	}
	
	private void subscribeAtDomainTopics(String deviceID) throws MqttSecurityException, MqttException, IOException, ConfigurationException {
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		samplingPeriod = prop.getInt("coordinator.samplingPeriod");
			
		client.subscribe(deviceID + "/accelerometer", new IMqttMessageListener() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				System.out.println(String.format("%s: %s", topic, message.toString()));
				DeviceData data = devices.get(deviceID);
				
				String fields[] = message.toString().split(";");
				LocalDateTime dateTimeCurrent, dateTimePast;
				try{
					dateTimeCurrent = LocalDateTime.parse(fields[0], formatter);
					// No need of formatter because the date-time was already stored with the default format
					if(!data.isAccelerometerEmpty())
						dateTimePast = LocalDateTime.parse(data.topAccelerometer().split(",")[0]);
					else
						dateTimePast = LocalDateTime.MIN;
				} catch(DateTimeParseException ex) {
					return;
				}
				
				boolean conditionOfSamplingPeriod = !data.isOrientationEmpty() && 
						checkDateTimeWithSamplingPeriod(dateTimePast, dateTimeCurrent);
				if(conditionOfSamplingPeriod || data.isAccelerometerEmpty()) {
					data.pushAccelerometer(dateTimeCurrent, fields[1]);
					// To update the DeviceData object in the map, it must be reloaded
					devices.put(deviceID, data);
				}	
			}
			
		});
			
		client.subscribe(deviceID + "/orientation", new IMqttMessageListener() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				System.out.println(String.format("%s: %s", topic, message.toString()));
				DeviceData data = devices.get(deviceID);
				
				String fields[] = message.toString().split(";");
				LocalDateTime dateTimeCurrent, dateTimePast;
				try{
					dateTimeCurrent = LocalDateTime.parse(fields[0], formatter);
					// No need of formatter because the date-time was already stored with the default format
					if(!data.isOrientationEmpty())
						dateTimePast = LocalDateTime.parse(data.topOrientation().split(",")[0]);
					else
						dateTimePast = LocalDateTime.MIN;
				} catch(DateTimeParseException ex) {
					return;
				}
				
				// New data are stored if enough milliseconds are spent
				boolean conditionOfSamplingPeriod = !data.isOrientationEmpty() && 
						checkDateTimeWithSamplingPeriod(dateTimePast, dateTimeCurrent);
				if(conditionOfSamplingPeriod || data.isOrientationEmpty()) {
					data.pushOrientation(dateTimeCurrent, fields[1]);
					// To update the DeviceData object in the map, it must be reloaded
					devices.put(deviceID, data);
				}
			}
			
		});
		
		client.subscribe(deviceID + "/location", new IMqttMessageListener() {

			@Override
			public void messageArrived(String topic, MqttMessage message) {
				DeviceData data = devices.get(deviceID);
				
				String fields[] = message.toString().split(";");
				LocalDateTime dateTimeCurrent, dateTimePast;
				try{
					dateTimeCurrent = LocalDateTime.parse(fields[0], formatter);
					// No need of formatter because the date-time was already stored with the default format
					if(!data.isLocationEmpty())
						dateTimePast = LocalDateTime.parse(data.topLocation().split(",")[0]);
					else
						dateTimePast = LocalDateTime.MIN;
				} catch(DateTimeParseException ex) {
					return;
				}
				
				// New data are stored if enough milliseconds are spent
				boolean conditionOfSamplingPeriod = !data.isLocationEmpty() && 
						checkDateTimeWithSamplingPeriod(dateTimePast, dateTimeCurrent);
				if(conditionOfSamplingPeriod || data.isLocationEmpty()) {
					// Prints the message arrived only if it will be stored in the collection
					System.out.println(String.format("%s: %s", topic, message.toString()));
					data.pushLocation(dateTimeCurrent, fields[1]);
					// To update the DeviceData object in the map, it must be reloaded
					devices.put(deviceID, data);
				}
			}
			
		});
	}
	
	public void unsubscribe(String topic) throws MqttException {
		System.out.println(String.format("Subscribing from topic: %s...", topic));
		client.unsubscribe(topic);
		System.out.println("Unsubscribed");
	}
	
	/*
	 * The coordinator sends the configuration with Quality of Service 1
     * because there are no problems if the client received the configuration more times.
	 */
	public void publish(String topic, String payload) throws MqttPersistenceException, MqttException {
		MqttMessage msg = new MqttMessage(payload.getBytes());
		msg.setQos(1);
		msg.setRetained(true);
		client.publish(broadcastTopic + topic, msg);
	}
	
	/**
	 * This method checks if dateTimeCurrent is at least samplingPeriod ms after dateTimePast.
	 * @param dateTimePast relative to last data arrived
	 * @param dateTimeCurrent relative to new data arrived
	 * @return true if the condition is positive, false otherwise
	 */
	public boolean checkDateTimeWithSamplingPeriod(LocalDateTime dateTimePast, LocalDateTime dateTimeCurrent) {
		LocalDateTime dateTimeCurrentMinusSamplingPeriod = dateTimeCurrent.minus(samplingPeriod, ChronoUnit.MILLIS);
		
		if(dateTimeCurrentMinusSamplingPeriod.isAfter(dateTimePast))
			return true;
		else
			return false;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public HashMap<String, DeviceData> getDevices() {
		return this.devices;
	}
	
}
