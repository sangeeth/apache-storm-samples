package com.sangeethlabs.storm.basic;

import java.util.Map;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import com.sangeethlabs.storm.etc.LogClient;

public class WordsKafkaSpout extends KafkaSpout {
    private static final long serialVersionUID = 1L;
    
    private static SpoutConfig createConfig(String host, String topic, String id) {
        BrokerHosts brokerHosts = new ZkHosts(String.format("%s:2181", host));
        SpoutConfig kafkaConfig = new SpoutConfig(brokerHosts, //List of Kafka brokers
                                                topic,      // Kafka topic to read from
                                                "/stormkafka",
                                                id);  // ID for storing consumer offsets in Zookeeper
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        return kafkaConfig;
    }
    
    private String logServerHost;
    
    private LogClient logClient;
    
    public WordsKafkaSpout(String logServerHost, String kafkaHost, String topic, String id) {
        super(createConfig(kafkaHost, topic, id));
        
        this.logServerHost = logServerHost;
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        logClient = new LogClient(this.logServerHost, "WordsKafkaSpout");
        logClient.connect();
        
        super.open(conf, context, collector);
    }

    public void nextTuple() {
        logClient.log("Fetching next tuple");

        super.nextTuple();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public void close() {
        logClient.disconnect();
        
        super.close();
    }
}