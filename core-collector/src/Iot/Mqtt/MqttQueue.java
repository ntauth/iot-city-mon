package Iot.Mqtt;

import Iot.Pair;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttQueue
{
    private ConcurrentLinkedQueue<Pair<String, Serializable>> _queue;
    private final Object _lock;
    private boolean _locked;

    public MqttQueue() {
        _queue = new ConcurrentLinkedQueue<>();
        _lock = new Object();
        _locked = false;
    }
    public void enqueue(Pair<String, Serializable> msg) {
        synchronized (_lock) {
            if (_locked)
                throw new IllegalStateException("Iot.Mqtt.MqttQueue is locked.");

            _queue.add(msg);
            _lock.notifyAll();
        }
    }

    public Optional<Pair<String, Serializable>> try_dequeue() {
        return Optional.ofNullable(_queue.poll());
    }

    public Pair<String, Serializable> dequeue() throws InterruptedException
    {
        Optional<Pair<String, Serializable>> obj;

        do {
            synchronized (_lock) {
                obj = try_dequeue();

                if (obj.isPresent())
                    return obj.get();

                _lock.wait();
            }
        } while (true);
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
