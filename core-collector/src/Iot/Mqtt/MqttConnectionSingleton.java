package Iot.Mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnectionSingleton
{
    private static MqttClient _client = null;
    private static MemoryPersistence _persistence;

    public static MqttClient getClient(String broker, String clientId, MqttConnectOptions options) throws MqttException
    {
        if (_client == null)
        {
            _persistence = new MemoryPersistence();
            _client = new MqttClient(broker, clientId, _persistence);

            if (options != null)
                _client.connect(options);
            else
                _client.connect();
        }

        return _client;
    }

    public static MqttClient getClient(String broker, String clientId) throws MqttException {
        return getClient(broker, clientId, null);
    }
}
