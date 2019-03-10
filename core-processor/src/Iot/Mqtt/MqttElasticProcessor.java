package Iot.Mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

public class MqttElasticProcessor implements Runnable
{
    private MqttAsyncClient _mqttClient;
    private static JSR47Logger _logger = new JSR47Logger();

    public MqttElasticProcessor(MqttAsyncClient mqttClient) {
        _mqttClient = mqttClient;
    }

    private void processor() throws Exception {
        // Subscribe to collectors
        while (!_mqttClient.isConnected())
            Thread.sleep(500);

        _mqttClient.subscribe("collectors/#", 2);
    }

    @Override
    public void run() {
        try {
            processor();
        } catch (Exception e) {
            _logger.severe(this.getClass().getName(), "run", "Processor thread was abruptly interrupted.");
        }
    }
}
