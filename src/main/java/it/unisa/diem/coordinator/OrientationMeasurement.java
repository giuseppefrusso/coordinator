package it.unisa.diem.coordinator;

import java.time.LocalDateTime;

public class OrientationMeasurement {

	private LocalDateTime dateTime;
	private double azimuth, pitch, roll;
	
	public OrientationMeasurement(LocalDateTime dateTime, double azimuth, double pitch, double roll) {
		super();
		this.dateTime = dateTime;
		this.azimuth = azimuth;
		this.pitch = pitch;
		this.roll = roll;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}
	
	@Override
	public String toString() {
		return dateTime.toString() + "," + azimuth + "," + pitch + "," + roll;
	}
}
