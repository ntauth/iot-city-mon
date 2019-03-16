# city-mon
**city-mon** is an IOT system that allows smart cities to collect information about the city, including air and noise pollution, by piggybacking smart sensors (*collectors*) on public transport vectors.

## Rationale
Noise pollution remains a major environmental health problem in Europe, with the transport sector being a major cause. Having a bird’s-eye view at the amount of noise and air pollution a city has would be of great help in effectively decreasing it.

## What it does
Our approach utilizes smart IOT collectors piggybacked on public transportation vectors (buses and similar) in order to provide real-time measurements of various indicators (noise and air pollution being just two). The resulting system is extremely flexible and may also be employed for things such as:
- Road shape fitness monitoring
- Real-time traffic monitoring

## How we built it
The entire system revolves around two entities:

**Collector**: IOT device packing multiple sensors and continuously ingesting state snapshots into a MQTT broker.

**Processor**: A distributed worker service that pulls out collector data, post-processes it and saves it to an Elasticsearch cluster for later retrieval (analytics, machine learning, etc.). City-mon also features an Angular-based dashboard for data visualization, coupled with Elastic’s Kibana visualizer.
