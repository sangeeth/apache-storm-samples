package com.sangeethlabs.storm.basic;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * This example works only on Storm Cluster.
 */
public class Main3 {
    public static void main(String[] args) throws Exception {
        StormTopology topology1 = createTopology(new String[] { "hello", "world", "using", "apache", "storm", "and", "xmpp" });
        
        Config conf = new Config();
        conf.put(XmppBolt.XMPP_USER, "storm@domain.local");
        conf.put(XmppBolt.XMPP_PASSWORD, "storm");
        conf.put(XmppBolt.XMPP_SERVER, "192.168.59.103");
        conf.put(XmppBolt.XMPP_TO, "bose@domain.local");
        conf.put(Config.TOPOLOGY_DEBUG, true);
        conf.setNumWorkers(3);

        StormSubmitter.submitTopologyWithProgressBar("main-3-topology", conf, topology1);
    }
    
    private static StormTopology createTopology(String [] words) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word", new WordSpout(words), 1);
        builder.setBolt("exclaim1", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 1).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 1).shuffleGrouping("filter");
        builder.setBolt("printer", new XmppBolt(), 1).shuffleGrouping("exclaim2");
        return builder.createTopology();
    }
}