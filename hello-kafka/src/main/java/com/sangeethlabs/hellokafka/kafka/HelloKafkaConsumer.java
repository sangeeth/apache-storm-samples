package com.sangeethlabs.hellokafka.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class HelloKafkaConsumer {
    public static void main(String[] args) {
        String zooKeeper = "192.168.1.200:2181";
        String groupId = "greeter-consumer";
        String topic = "greeter";
        int threads = 1;
 
        Properties props = new Properties();
        props.put("zookeeper.connect", zooKeeper);
        props.put("group.id", groupId);
        props.put("auto.commit.interval.ms", "1000");

        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, threads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        
        KafkaStream<byte[], byte[]> stream = streams.get(0);
        
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            String message = new String(it.next().message());
            System.out.println(message);
            if ("shutdown".equals(message)) {
                break;
            }
        }

        consumer.shutdown();
    }
}
