package Iot;

import Iot.Mqtt.MqttConnectionSingleton;
import Iot.Mqtt.MqttQueue;
import Iot.Mqtt.MqttWorker;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.source.git.GitConfigurationSourceBuilder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

import java.io.Serializable;

public class CollectorMain
{
    private static String _GIT_CONFIG_URI = "https://github.com/ntauth/iot-citymon-cfg";
    private static JSR47Logger _logger = new JSR47Logger();

    public static void main(String[] args)
    {
        Pair<String, Serializable> p = new Pair<>();

        // Get the configuration provider
        ConfigurationProvider cfgProvider = ConfigurationProviderSingleton
                .getProvider(new GitConfigurationSourceBuilder()
                                .withRepositoryURI(_GIT_CONFIG_URI)
                                .build());
        MqttConfig cfg = cfgProvider.bind("mqtt", MqttConfig.class);

        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);

        Thread worker = null;
        Thread collector = null;

        try
        {
            String clientIdPfx = System.getenv("MQTT_CLIENT_ID");
            String clientId = clientIdPfx + "_collector";
            MqttClient client = MqttConnectionSingleton.getClient(cfg.brokerUrl(), clientId, opts);
            MqttQueue queue = new MqttQueue();

            // Spawn Iot.Mqtt.MqttWorker thread
            worker = new Thread(new MqttWorker(client, queue));
            worker.start();

            // Spawn Iot.Collector thread
            collector = new Thread(new Collector(s -> {
                if (s != null)
                    queue.enqueue(new Pair<>(String.format("%s/%s", "collectors", clientIdPfx), s));
            }, CollectorSimulatorStrategy.class));
            collector.run();
        }
        catch (Exception e) {
            _logger.severe(CollectorMain.class.getName(), "main", e.getMessage());
        }
    }
}
