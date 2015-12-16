package com.sangeethlabs.hellokafka.kafka;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.sangeethlabs.hellokafka.common.Message;

/**
 * Command to run this program from command line
 * <pre>
 * mvn exec:java -Dexec.mainClass="works.kafka.BinaryHelloKafkaProducer" -Dexec.classpathScope=compile
 * </pre>
 */
public class BinaryHelloKafkaProducer {
	public static void main(String [] args) throws Exception {
		String brokers = "192.168.1.200:9092";
		
        Map<String, Object> props = new TreeMap<>();
        props.put("bootstrap.servers", brokers);
        props.put("request.required.acks", "0");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", ByteArraySerializer.class.getName());
 
        Producer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
        
        Console console = System.console();
        String line = null;
        while(line==null || !"quit".equals(line)) {
        	line = console.readLine();
        	if (!"quit".equals(line)) {
        	    Message message = new Message(line);
        	    byte [] value = toBytes(message);
                ProducerRecord<String, byte[]> record = new ProducerRecord<>(/*topic=*/"greeter", /*value=*/value);
                producer.send(record);
        	}
        }
        
        producer.close();
	}
	
	private static byte[] toBytes(Object object) throws Exception {
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(bout);
	    out.writeObject(object);
	    out.close();
	    return bout.toByteArray();
	}
}