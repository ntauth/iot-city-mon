import javafx.util.Pair;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttQueue
{
    private ConcurrentLinkedQueue<Pair<String, Serializable>> _queue;
    private boolean _locked;

    public void enqueue(Pair<String, Serializable> msg) {
        if (_locked)
            throw new IllegalStateException("MqttQueue is locked.");

        _queue.add(msg);
    }

    public Optional<Pair<String, Serializable>> dequeue() {
        return Optional.of(_queue.poll());
    }

    public boolean isLocked() {
        return _locked;
    }

    public void lock() {
        _locked = true;
    }

    public void unlock() {
        _locked = false;
    }
}
