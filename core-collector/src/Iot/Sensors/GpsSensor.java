package Iot.Sensors;

public class GpsSensor extends SensorBase
{
    private float latitude;
    private float longitude;

    public GpsSensor() {
        latitude = 37.234332396f;
        longitude = -115.80666344f;
    }

    public GpsSensor(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
