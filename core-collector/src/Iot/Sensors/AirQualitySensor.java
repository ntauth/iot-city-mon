package Iot.Sensors;

public class AirQualitySensor
{
    private float valueNormalized;

    public AirQualitySensor() {
        valueNormalized = 0.f;
    }

    public AirQualitySensor(float valueNormalized) {
        this.valueNormalized = valueNormalized;
    }

    public float getValueNormalized() {
        return valueNormalized;
    }

    public void setValueNormalized(float valueNormalized) {
        this.valueNormalized = valueNormalized;
    }
}
