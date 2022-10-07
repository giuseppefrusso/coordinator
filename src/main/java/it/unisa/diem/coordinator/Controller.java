package it.unisa.diem.coordinator;

import java.util.Collection;
import java.util.Stack;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	@Autowired
	protected Configuration conf;
	
	@PostMapping("/connect")
	public void connectAndSubscribe() {
		String broadcastTopic = CoordinatorApplication.broadcastTopic;
		try {
			MQTTClient client = CoordinatorApplication.client; 
			
			if(client.isConnected()) {
				System.out.println("Client already connected!");
				return;
			}
			
			client.connect(CoordinatorApplication.username, 
					CoordinatorApplication.password, 
					CoordinatorApplication.connectionTimeout);
			
			System.out.print("Known devices: ");
			if(client.getDevices().isEmpty())
				System.out.print("none");
			else 
				for(String dev : getDevices()) {
					System.out.print(dev + "; ");
				}
			System.out.println();
			
			client.subscribeForDiscoveringDevices();
			
			client.publish(broadcastTopic + "/samplingPeriod", String.valueOf(conf.getSamplingPeriod()));
			client.publish(broadcastTopic + "/accelerometer", String.valueOf(conf.isAccelerometer()));
			client.publish(broadcastTopic + "/orientation", String.valueOf(conf.isOrientation()));
			client.publish(broadcastTopic + "/location", String.valueOf(conf.isLocation()));
			System.out.println("Current configuration values published!");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/devices")
	public Collection<String> getDevices() {
		return CoordinatorApplication.client.getDevices().keySet();
	}
	
	@GetMapping("{deviceID}/accelerometer/last-values/{n}")
	public Stack<AccelerometerMeasurement> getLastNValuesOfAccelerometer(@PathVariable String deviceID, @PathVariable int n) {
		DeviceData allValues = CoordinatorApplication.client.getDevices().get(deviceID).clone();
		Stack<AccelerometerMeasurement> lastValues = new Stack<>();
		
		if(allValues != null)
			for(int i=0; i<n; i++) {
				AccelerometerMeasurement entry = allValues.popAccelerometer();
				if(entry == null)
					break;
				lastValues.push(entry);
			}
		else
			return null;
		
		return lastValues;
	}
	
	@GetMapping("{deviceID}/orientation/last-values/{n}")
	public Stack<OrientationMeasurement> getLastNValuesOfOrientation(@PathVariable String deviceID, @PathVariable int n) {
		DeviceData allValues = CoordinatorApplication.client.getDevices().get(deviceID).clone();
		Stack<OrientationMeasurement> lastValues = new Stack<>();
		
		if(allValues != null)
			for(int i=0; i<n; i++) {
				OrientationMeasurement entry = allValues.popOrientation();
				if(entry == null)
					break;
				lastValues.push(entry);
			}
		
		return lastValues;
	}
	
	@GetMapping("{deviceID}/location/last-values/{n}")
	public Stack<LocationMeasurement> getLastNValuesOfLocation(@PathVariable String deviceID, @PathVariable int n) {
		DeviceData allValues = CoordinatorApplication.client.getDevices().get(deviceID).clone();
		Stack<LocationMeasurement> lastValues = new Stack<>();
		
		if(allValues != null)
			for(int i=0; i<n; i++) {
				LocationMeasurement entry = allValues.popLocation();
				if(entry == null)
					break;
				lastValues.push(entry);
			}
		
		return lastValues;
	}
	
	@GetMapping("/configuration")
	public Configuration getConfiguration() {
		return conf;
	}
	
	@GetMapping("/sampling-period")
	public int getSamplingPeriod() {
		return conf.getSamplingPeriod();
	}
	
	@GetMapping("/accelerometer") 
	public boolean getAccelerometer() { 
		return conf.isAccelerometer();
	}
	
	@GetMapping("/orientation")
	public boolean getOrientation() {
		return conf.isOrientation();
	}
	
	@GetMapping("/location")
	public boolean getLocation() {
		return conf.isLocation();
	}
	
	@PostMapping("/set-configuration") 
	public void postConfiguration(@RequestBody Configuration conf) throws Exception {
		this.conf = conf;
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		prop.setProperty("coordinator.samplingPeriod", conf.getSamplingPeriod());
		prop.setProperty("coordinator.accelerometer", conf.isAccelerometer());
		prop.setProperty("coordinator.orientation", conf.isOrientation());
		prop.setProperty("coordinator.location", conf.isLocation());
		prop.save();
		
		publishConfiguration();
	}
	
	@PostMapping("/send-configuration")
	public void publishConfiguration() throws Exception {
		CoordinatorApplication.client.publish("/samplingPeriod", String.valueOf(conf.getSamplingPeriod()));
		CoordinatorApplication.client.publish("/accelerometer", String.valueOf(conf.isAccelerometer()));
		CoordinatorApplication.client.publish("/orientation", String.valueOf(conf.isOrientation()));
		CoordinatorApplication.client.publish("/location", String.valueOf(conf.isLocation()));
	}
	
	@PostMapping("/set-sampling-period")
	public void postSamplingRate(@RequestBody int samplingPeriod) throws Exception {
		conf.setSamplingPeriod(samplingPeriod);
		CoordinatorApplication.client.publish("/samplingPeriod", String.valueOf(samplingPeriod));
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		prop.setProperty("coordinator.samplingPeriod", conf.getSamplingPeriod());
		prop.save();
	}
	
	@PostMapping("/set-accelerometer")
	public void postAccelerator(@RequestBody boolean accelerometer) throws Exception {
		conf.setAccelerometer(accelerometer);
		
		CoordinatorApplication.client.publish("/accelerometer", String.valueOf(accelerometer));
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		prop.setProperty("coordinator.accelerometer", conf.isAccelerometer());
		prop.save();
	}
	
	@PostMapping("/set-orientation")
	public void postOrientation(@RequestBody boolean orientation) throws Exception {
		conf.setOrientation(orientation);
		
		CoordinatorApplication.client.publish("/orientation", String.valueOf(orientation));
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		prop.setProperty("coordinator.orientation", conf.isOrientation());
		prop.save();
	}
	
	@PostMapping("/set-location")
	public void postLocation(@RequestBody boolean location) throws Exception{
		conf.setLocation(location);
		
		CoordinatorApplication.client.publish("/location", String.valueOf(location));
		
		PropertiesConfiguration prop = new PropertiesConfiguration(CoordinatorApplication.CONFIGURATION_FILE);
		prop.setProperty("coordinator.location", conf.isLocation());
		prop.save();
	}
}
