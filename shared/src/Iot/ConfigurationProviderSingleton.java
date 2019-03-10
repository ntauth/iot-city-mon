package Iot;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;

public class ConfigurationProviderSingleton
{
    private static ConfigurationProvider _cfgProvider;

    public static ConfigurationProvider getProvider(ConfigurationSource src)
    {
        if (_cfgProvider == null) {
            _cfgProvider = new ConfigurationProviderBuilder()
                                .withConfigurationSource(src)
                                .build();
        }

        return _cfgProvider;
    }
}
