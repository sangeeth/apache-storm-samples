package com.sangeethlabs.storm.basic;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

/**
 * This example runs only on Local Cluster
 */
public class Main5 {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        String[] words = new String[] { "hello", "world", "using", "apache", "storm" };
        builder.setSpout("word-spout", new WordSpout(words), 1);
        builder.setBolt("condition", new ConditionBolt("storm")).shuffleGrouping("word-spout");
        builder.setBolt("condition.false", new ConditionResultBolt(false)).fieldsGrouping("condition", new Fields("condition.output"));
        builder.setBolt("condition.true", new ConditionResultBolt(true)).fieldsGrouping("condition", new Fields("condition.output"));
        builder.setBolt("exclaim", new ExclamationBolt(), 2).shuffleGrouping("condition.true");
        BoltDeclarer printerBoltDeclarer = builder.setBolt("printer", new PrinterBolt(), 3);
        printerBoltDeclarer.shuffleGrouping("exclaim");
        printerBoltDeclarer.shuffleGrouping("condition.false");
        BoltDeclarer loggerBoltDeclarer = builder.setBolt("logger", new LoggerBolt(/*Host IP*/"localhost"), 5);
        loggerBoltDeclarer.shuffleGrouping("exclaim");
        loggerBoltDeclarer.shuffleGrouping("condition.false");
        
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