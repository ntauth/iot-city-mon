package Iot;

import java.util.concurrent.CompletableFuture;

interface ICollectorStrategy
{
    CompletableFuture<CollectorSnapshot> acquireSnapshot();
}