package com.sangeethlabs.hellokafka.storm;

import backtype.storm.spout.RawScheme;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class MessageKafkaSpout extends KafkaSpout {
    private static final long serialVersionUID = 1L;
    
    private static SpoutConfig createConfig(String host, String topic, String id) {
        BrokerHosts brokerHosts = new ZkHosts(String.format("%s:2181", host));
        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, //List of Kafka brokers
                                                topic,      // Kafka topic to read from
                                                "/stormkafka",
                                                id);  // ID for storing consumer offsets in Zookeeper
        kafkaConfig.scheme = new SchemeAsMultiScheme(new RawScheme());
        kafkaConfig.maxOffsetBehind = 0;
        return kafkaConfig;
    }
    
    public MessageKafkaSpout(String kafkaHost, String topic, String id) {
        super(createConfig(kafkaHost, topic, id));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}