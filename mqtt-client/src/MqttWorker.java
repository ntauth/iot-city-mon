import com.google.common.collect.Collections2.*;
import org.eclipse.paho.client.mqttv3.MqttClient;

import javafx.util.Pair;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttWorker implements Runnable
{
    private MqttQueue _queue;
    private MqttClient _mqttClient;
    private long _publishedMessageCount;
    private long _readMessageCount;

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
                    e.printStackTrace();
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
