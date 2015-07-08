package com.sangeethlabs.storm.basic;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class WordsKafkaProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("metadata.broker.list", "192.168.59.103:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "0");
 
        ProducerConfig config = new ProducerConfig(props);
        
        Producer<String, String> producer = new Producer<String, String>(config);
        
        String string = "Here comes the words from WordsKafkaProducer Hello world using Apache Storm and Kafka";
        String [] words = string.split(" ");
 
        for (String word:words) { 
            KeyedMessage<String, String> data = new KeyedMessage<String, String>("words-stream", "word", word);
            producer.send(data);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }
    
}
