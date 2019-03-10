package Iot.Mqtt;

import Iot.Pair;

import org.eclipse.paho.client.mqttv3.MqttClient;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

import java.io.Serializable;

public class MqttWorker implements Runnable
{
    private MqttQueue _queue;
    private MqttClient _mqttClient;
    private long _publishedMessageCount;
    private long _readMessageCount;
    private static JSR47Logger _logger = new JSR47Logger();

    private void worker() throws InterruptedException
    {
        for (;;)
        {
            Pair<String, Serializable> msg = _queue.dequeue();

            try {
                _readMessageCount++;
                _mqttClient.publish(msg.getKey(), new MqttMessage(msg.getValue().toString().getBytes()));
                _publishedMessageCount++;
            } catch (MqttException e) {
                _logger.severe(this.getClass().getName(), "worker", e.getMessage());
            }
        }
    }

    public MqttWorker(MqttClient mqttClient, MqttQueue queue) {
        _mqttClient = mqttClient;
        _queue = queue;
    }

    @Override
    public void run() {
        try {
            worker();
        } catch (InterruptedException e) {
            _logger.severe(this.getClass().getName(), "run","Worker was abruptly interrupted.");
        }
    }
}
