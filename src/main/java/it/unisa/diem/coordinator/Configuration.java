package it.unisa.diem.coordinator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("coordinator")
public class Configuration {

	private int samplingPeriod;
	private boolean accelerometer, orientation, location;
	
	public Configuration() {
		
	}
	
	public Configuration(int samplingPeriod, boolean accelerometer, boolean orientation, boolean location) {
		super();
		this.samplingPeriod = samplingPeriod;
		this.accelerometer = accelerometer;
		this.orientation = orientation;
		this.location = location;
	}

	public int getSamplingPeriod() {
		return samplingPeriod;
	}

	public void setSamplingPeriod(int samplingPeriod) {
		this.samplingPeriod = samplingPeriod;
	}

	public boolean isAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(boolean accelerometer) {
		this.accelerometer = accelerometer;
	}

	public boolean isOrientation() {
		return orientation;
	}
	
	public void setOrientation(boolean orientation) {
		this.orientation = orientation;
	}

	public boolean isLocation() {
		return location;
	}

	public void setLocation(boolean location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Configuration [samplingPeriod=" + samplingPeriod + ", accelerometer=" + accelerometer + ", orientation="
				+ orientation + ", location=" + location + "]";
	}
}
