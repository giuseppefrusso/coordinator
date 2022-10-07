package it.unisa.diem.coordinator;

import java.time.LocalDateTime;

public class AccelerometerMeasurement {

	private LocalDateTime dateTime;
	private double x, y, z;
	
	public AccelerometerMeasurement(LocalDateTime dateTime, double x, double y, double z) {
		this.dateTime = dateTime;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}	
	
	@Override
	public String toString() {
		return dateTime.toString() + "," + x + "," + y + "," + z;
	}
}
