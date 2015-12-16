package com.sangeethlabs.hellokafka.kafka;

import java.io.Console;
import java.util.Map;
import java.util.TreeMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Command to run this program from command line.
 * <pre>
 * mvn exec:java -Dexec.mainClass="works.kafka.HelloKafkaProducer" -Dexec.classpathScope=compile
 * </pre>
 */
public class HelloKafkaProducer {
	public static void main(String [] args) {
		String brokers = "192.168.1.200:9092";
		
        Map<String, Object> props = new TreeMap<>();
        props.put("bootstrap.servers", brokers);
        props.put("request.required.acks", "0");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
 
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        
        Console console = System.console();
        String line = null;
        while(line==null || !"quit".equals(line)) {
        	line = console.readLine();
        	if (!"quit".equals(line)) {
                ProducerRecord<String, String> record = new ProducerRecord<>(/*topic=*/"greeter", /*value=*/line);
                producer.send(record);
        	}
        }
        
        producer.close();
	}
}