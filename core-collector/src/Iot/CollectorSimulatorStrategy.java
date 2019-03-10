package Iot;

import Iot.Sensors.AirQualitySensor;

import java.util.concurrent.*;

public class CollectorSimulatorStrategy extends CollectorStrategyBase
{
    private ExecutorService _executor;

    public CollectorSimulatorStrategy() {
        _executor = Executors.newCachedThreadPool();
    }

    @Override
    public CompletableFuture<CollectorSnapshot> acquireSnapshot()
    {
        CompletableFuture<CollectorSnapshot> future = new CompletableFuture<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int nextSnapshotTimeout = random.nextInt(1000, 1100);

        _executor.submit(() -> {
            try
            {
                Thread.sleep(nextSnapshotTimeout);

                // Make a random snapshot
                // @todo: simulate GpsSensor using linear interpolation
                CollectorSnapshot snapshot = new CollectorSnapshot();
                snapshot.setAirQualitySensor(new AirQualitySensor((float) random.nextDouble(0.0, 1.0)));

                future.complete(snapshot);
            }
            catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
