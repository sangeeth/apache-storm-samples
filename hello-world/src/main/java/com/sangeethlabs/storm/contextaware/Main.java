package com.sangeethlabs.storm.contextaware;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class Main {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        String[] words = new String[] { "hello", "world", "using", "apache", "storm" };
        builder.setSpout("word", new WordSpout(words), 10);
        builder.setBolt("exclaim1", new ExclamationBolt(), 3).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 2).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("filter");
        builder.setBolt("printer", new PrinterBolt(), 3).shuffleGrouping("exclaim2");
        StormTopology topology = builder.createTopology();
        
        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, topology);
        } else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", conf, topology);
            Utils.sleep(10000);
            cluster.killTopology("test");
            cluster.shutdown();
        }
    }
}