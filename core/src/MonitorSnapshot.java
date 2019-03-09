import Sensors.AirQualitySensor;
import Sensors.GpsSensor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MonitorSnapshot implements Serializable
{
    private long timestamp;

    private GpsSensor gpsSensor;
    private AirQualitySensor airQualitySensor;

    public MonitorSnapshot(long timestamp, GpsSensor gpsSensor, AirQualitySensor airQualitySensor) {
        this.timestamp = timestamp;
        this.gpsSensor = gpsSensor;
        this.airQualitySensor = airQualitySensor;
    }

    public MonitorSnapshot() {
        timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime();
        gpsSensor = new GpsSensor();
        airQualitySensor = new AirQualitySensor();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public GpsSensor getGpsSensor() {
        return gpsSensor;
    }

    public void setGpsSensor(GpsSensor gpsSensor) {
        this.gpsSensor = gpsSensor;
    }

    public AirQualitySensor getAirQualitySensor() {
        return airQualitySensor;
    }

    public void setAirQualitySensor(AirQualitySensor airQualitySensor) {
        this.airQualitySensor = airQualitySensor;
    }

    @Override
    public String toString()
    {
        String json;

        try {
            json = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            json = "";
        }
        return json;
    }
}