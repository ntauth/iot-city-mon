import javafx.util.Pair;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.source.git.GitConfigurationSourceBuilder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

import java.io.Serializable;

public class ClientMain
{
    private static String _GIT_CONFIG_URI = "https://github.com/ntauth/iot-citymon-cfg";
    private static JSR47Logger _logger = new JSR47Logger();

    public static void main(String[] args)
    {
        // Get the configuration provider
        ConfigurationProvider cfgProvider = ConfigurationProviderSingleton
                .getProvider(new GitConfigurationSourceBuilder()
                                .withRepositoryURI(_GIT_CONFIG_URI)
                                .build());
        MqttConfig cfg = cfgProvider.bind("mqtt", MqttConfig.class);

        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);

        try
        {
            MqttClient client = MqttConnectionSingleton.getClient(cfg.brokerUrl(), System.getenv("MQTT_CLIENT_ID"), opts);
            MqttQueue queue = new MqttQueue();

            // Spawn worker thread
            Thread worker = new Thread(new MqttWorker(client, queue));
            worker.start();

            // Ingestion loop
            for (;;)
            {
                try {
                    queue.enqueue(new Pair<String, Serializable>("iot-citymon-test", "ACK"));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    _logger.severe(ClientMain.class.getName(), "main", e.getMessage());
                }
            }
        }
        catch (MqttException e) {
            _logger.severe(ClientMain.class.getName(), "main", e.getMessage());
        }
    }
}
