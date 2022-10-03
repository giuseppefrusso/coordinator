package it.unisa.diem.coordinator;

import java.time.LocalDateTime;

public class Measurement {

	private LocalDateTime dateTime;
	private double v1, v2, v3;
	
	public Measurement(LocalDateTime dateTime, double v1, double v2, double v3) {
		super();
		this.dateTime = dateTime;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getV1() {
		return v1;
	}

	public void setV1(double v1) {
		this.v1 = v1;
	}

	public double getV2() {
		return v2;
	}

	public void setV2(double v2) {
		this.v2 = v2;
	}

	public double getV3() {
		return v3;
	}

	public void setV3(double v3) {
		this.v3 = v3;
	}
	
	
}
