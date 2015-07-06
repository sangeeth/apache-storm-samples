package com.sangeethlabs.storm.basic;

import java.io.File;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * This example runs only on Local Cluster
 */
public class Main2 {
    public static void main(String[] args) throws Exception {
        StormTopology topology1 = createTopology(new String[] { "hello", "world", "using", "apache", "storm" }, new File("target/helloworld.out.txt"));
        StormTopology topology2 = createTopology(new String[] { "welcome", "to", "stream", "processing" }, new File("target/welcome.out.txt"));
        
        Config conf = new Config();
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test1", conf, topology1);
        cluster.submitTopology("test2", conf, topology2);
        Utils.sleep(10000);
        cluster.killTopology("test1");
        cluster.killTopology("test2");
        cluster.shutdown();
    }
    
    private static StormTopology createTopology(String [] words, File outputFile) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word", new WordSpout(words), 1);
        builder.setBolt("exclaim1", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 1).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 1).shuffleGrouping("filter");
        builder.setBolt("printer", new FilerBolt(outputFile), 1).shuffleGrouping("exclaim2");
        return builder.createTopology();
    }
}