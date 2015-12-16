package com.sangeethlabs.hellokafka.storm;

import java.util.Arrays;
import java.util.List;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * This example works only on remote Storm cluster.
 * 
 * The command to deploy this topology on a Storm is as shown below
 * 
 * <pre>
 * storm jar ./target/hello-kafka-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.sangeethlabs.hellokafka.storm.HelloKafkaTopology 192.168.1.221 
 * </pre>
 */
public class HelloKafkaTopology {
    private static final List<String> ZOOKEEPER_SERVERS=Arrays.asList("192.168.1.200");
    private static final String NIMBUS_HOST="192.168.1.221";

    public static void main(String[] args) throws Exception {
        StormTopology topology = createTopology();
        System.out.printf("Number of Bolts: %s\n", topology.get_bolts_size());
        System.out.printf("Number of Spouts: %s\n", topology.get_spouts_size());
        
        Config conf = new Config();
        conf.put(Config.TOPOLOGY_DEBUG, true);
        conf.put(Config.NIMBUS_HOST, NIMBUS_HOST);
        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        conf.put(Config.STORM_ZOOKEEPER_SERVERS, ZOOKEEPER_SERVERS);

        conf.setNumWorkers(3);

        StormSubmitter.submitTopologyWithProgressBar("hello-kafka-topology", conf, topology);
    }
    
    private static StormTopology createTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new MessageKafkaSpout(/*kafkaHost=*/ZOOKEEPER_SERVERS.get(0), 
                                                      /*topic=*/"greeter", 
                                                      /*id=*/"hello-kafka-topology") , 1);
        builder.setBolt("message", new MessageBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("printer", new PrinterBolt(), 1).shuffleGrouping("message");
        return builder.createTopology();
    }
}