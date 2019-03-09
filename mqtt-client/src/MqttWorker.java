import org.eclipse.paho.client.mqttv3.MqttClient;

import javafx.util.Pair;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

import java.io.Serializable;
import java.util.Optional;

public class MqttWorker implements Runnable
{
    private MqttQueue _queue;
    private MqttClient _mqttClient;
    private long _publishedMessageCount;
    private long _readMessageCount;
    private static JSR47Logger _logger = new JSR47Logger();

    private void worker()
    {
        while (true)
        {
            Optional<Pair<String, Serializable>> msg = _queue.dequeue();

            msg.ifPresent(_msg -> {
                try {
                    _readMessageCount++;
                    _mqttClient.publish(_msg.getKey(), new MqttMessage(_msg.getValue().toString().getBytes()));
                    _publishedMessageCount++;
                } catch (MqttException e) {
                    _logger.severe(this.getClass().getName(), "worker", e.getMessage());
                }
            });
        }
    }

    public MqttWorker(MqttClient mqttClient, MqttQueue queue) {
        _mqttClient = mqttClient;
        _queue = queue;
    }

    @Override
    public void run() {
        worker();
    }
}
