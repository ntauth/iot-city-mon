package Iot.Mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttAsyncConnectionSingleton
{
    private static MqttAsyncClient _client = null;
    private static MemoryPersistence _persistence;

    public static MqttAsyncClient getClient(String broker, String clientId, MqttCallback callback, MqttConnectOptions options)
            throws MqttException
    {
        if (_client == null)
        {
            _persistence = new MemoryPersistence();
            _client = new MqttAsyncClient(broker, clientId, _persistence);
            _client.setCallback(callback);

            if (options != null)
                _client.connect(options);
            else
                _client.connect();
        }

        return _client;
    }

    public static MqttAsyncClient getClient(String broker, String clientId, MqttCallback callback) throws MqttException {
        return getClient(broker, clientId, callback, null);
    }
}
