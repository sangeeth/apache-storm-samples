package com.sangeethlabs.storm.basic;

import java.util.Arrays;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * This example works only on remote Storm cluster.
 */
public class Main4 {
    public static void main(String[] args) throws Exception {
        StormTopology topology = createTopology();
        
        Config conf = new Config();
        conf.put(Config.TOPOLOGY_DEBUG, true);
        conf.put(Config.NIMBUS_HOST, "192.168.59.103");
        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList("192.168.59.103"));

        conf.setNumWorkers(3);

        StormSubmitter.submitTopologyWithProgressBar("main-4-topology", conf, topology);
    }
    
    private static StormTopology createTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new WordsKafkaSpout(/*logServerHost=*/"192.168.59.3",
                                                      /*kafkaHost=*/"192.168.59.103", 
                                                      /*topic=*/"words-stream", 
                                                      /*id=*/"helloworld-main4") , 1);
        builder.setBolt("word", new WordBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("exclaim1", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 1).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 1).shuffleGrouping("filter");
        builder.setBolt("logger", new LoggerBolt(/*Host IP*/"192.168.59.3"), 1).shuffleGrouping("word");
        return builder.createTopology();
    }
}