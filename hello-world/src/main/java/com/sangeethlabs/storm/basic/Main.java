package com.sangeethlabs.storm.basic;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * This example runs only on Local Cluster
 */
public class Main {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        String[] words = new String[] { "hello", "world", "using", "apache", "storm" };
        builder.setSpout("word", new WordSpout(words), 1);
        builder.setBolt("exclaim1", new ExclamationBolt(), 2).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 2).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("filter");
        //builder.setBolt("printer", new PrinterBolt(), 3).shuffleGrouping("exclaim2");
        builder.setBolt("logger", new LoggerBolt(/*Host IP*/"192.168.59.3"), 5).shuffleGrouping("exclaim2");
        StormTopology topology = builder.createTopology();
        
        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, topology);
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
}