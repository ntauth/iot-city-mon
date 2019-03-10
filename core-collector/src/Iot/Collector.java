package Iot;

import org.eclipse.paho.client.mqttv3.logging.JSR47Logger;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Collector implements Runnable
{
    private ICollectorStrategy _strategy;
    private Consumer<CollectorSnapshot> _consumer;
    private static JSR47Logger _logger = new JSR47Logger();

    private void collectorLoop() throws Exception
    {
        for (;;)
        {
            CompletableFuture<CollectorSnapshot> future = _strategy.acquireSnapshot();

            CollectorSnapshot snapshot = future.get();
            _consumer.accept(snapshot);

            Thread.sleep(1000);
        }
    }

    public Collector(Consumer<CollectorSnapshot> consumer, Class<? extends ICollectorStrategy> strategy)
            throws IllegalArgumentException
    {
        try {
            _strategy = strategy.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Strategy isn't trivially constructable.");
        }

        _consumer = consumer;
    }

    @Override
    public void run() {
        try {
            collectorLoop();
        } catch (Exception e) {
            _logger.severe(this.getClass().getName(), "run", "Iot.Collector was abruptly interrupted.");
        }
    }
}