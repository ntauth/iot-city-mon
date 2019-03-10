package Iot;

import Iot.Mqtt.*;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.source.git.GitConfigurationSourceBuilder;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

public class ProcessorMain
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
        opts.setAutomaticReconnect(true);
        opts.setConnectionTimeout(10);

        Thread processor;

        try
        {
            String clientId = System.getenv("MQTT_CLIENT_ID");
            MqttAsyncClient client = MqttAsyncConnectionSingleton.getClient(cfg.brokerUrl(), clientId, new MqttElasticHandler(), opts);

            // Spawn MqttElasticProcessor thread
            processor = new Thread(new MqttElasticProcessor(client));
            processor.run();
        }
        catch (Exception e) {
            _logger.severe(ProcessorMain.class.getName(), "main", e.getMessage());
        }
    }
}
