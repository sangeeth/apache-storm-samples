package com.sangeethlabs.storm.basic;

import java.util.Arrays;
import java.util.List;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * This example works only on remote Storm cluster.
 */
public class Main4 {
    private static final List<String> ZOOKEEPER_SERVERS=Arrays.asList("10.83.27.200");
    private static final String NIMBUS_HOST="10.83.27.221";
    private static final String LOG_SERVER_HOST="10.127.150.217";

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

        StormSubmitter.submitTopologyWithProgressBar("main-4-topology", conf, topology);
    }
    
    private static StormTopology createTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new WordsKafkaSpout(/*logServerHost=*/LOG_SERVER_HOST,
                                                      /*kafkaHost=*/ZOOKEEPER_SERVERS.get(0), 
                                                      /*topic=*/"words-stream", 
                                                      /*id=*/"helloworld-main4") , 1);
        builder.setBolt("word", new WordBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("exclaim1", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 1).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 1).shuffleGrouping("filter");
        builder.setBolt("logger", new LoggerBolt(/*Host IP*/LOG_SERVER_HOST), 1).shuffleGrouping("word");
        return builder.createTopology();
    }
}