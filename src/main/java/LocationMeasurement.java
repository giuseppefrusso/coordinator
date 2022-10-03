import java.time.LocalDateTime;

public class LocationMeasurement {

	private LocalDateTime dateTime;
	private double latitude, longitude;
	
	public LocationMeasurement(LocalDateTime dateTime, double latitude, double longitude) {
		super();
		this.dateTime = dateTime;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
