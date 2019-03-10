package Iot;

import Iot.Sensors.AirQualitySensor;
import Iot.Sensors.GpsSensor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CollectorSnapshot implements Serializable
{
    private long timestamp;

    private GpsSensor gpsSensor;
    private AirQualitySensor airQualitySensor;

    public CollectorSnapshot(long timestamp, GpsSensor gpsSensor, AirQualitySensor airQualitySensor) {
        this.timestamp = timestamp;
        this.gpsSensor = gpsSensor;
        this.airQualitySensor = airQualitySensor;
    }

    public CollectorSnapshot() {
        this(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime(), new GpsSensor(), new AirQualitySensor());
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