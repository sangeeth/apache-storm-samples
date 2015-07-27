package com.sangeethlabs.storm.basic;

import java.util.Arrays;
import java.util.List;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * This example works only on local Storm cluster.
 */
public class Main1 {
    private static final List<String> ZOOKEEPER_SERVERS=Arrays.asList("192.168.59.103");
    private static final String NIMBUS_HOST="192.168.59.103";
    private static final String LOG_SERVER_HOST="192.168.59.3";
    public static void main(String[] args) throws Exception {
        StormTopology topology = createTopology();
        
        Config conf = new Config();
        conf.setDebug(true);
//        conf.put(XmppBolt.XMPP_USER, "storm@domain.local");
//        conf.put(XmppBolt.XMPP_PASSWORD, "storm");
//        conf.put(XmppBolt.XMPP_SERVER, "192.168.59.103");
//        conf.put(XmppBolt.XMPP_TO, "bose@domain.local");
        conf.put(Config.TOPOLOGY_DEBUG, true);
        conf.put(Config.NIMBUS_HOST, NIMBUS_HOST);
        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        conf.put(Config.STORM_ZOOKEEPER_SERVERS, ZOOKEEPER_SERVERS);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, topology);
        Utils.sleep(30000);
        cluster.killTopology("test");
        cluster.shutdown();
    }
    
    private static StormTopology createTopology() {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", 
                          new WordsKafkaSpout(/*logServerHost=*/LOG_SERVER_HOST,
                                               /*kafkaHost=*/ZOOKEEPER_SERVERS.get(0), 
                                               /*topic=*/"words-stream", 
                                               /*id=*/"helloworld-main1") , 
                        1);
        builder.setBolt("word", new WordBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("exclaim1", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("filter", new WordFilterBolt(/*The word to be filter from processing is */ "using"), 1).shuffleGrouping("exclaim1");
        builder.setBolt("exclaim2", new ExclamationBolt(), 1).shuffleGrouping("filter");
//        builder.setBolt("notify", new XmppBolt(), 1).shuffleGrouping("exclaim2");
        builder.setBolt("logger", new LoggerBolt(/*Host IP*/LOG_SERVER_HOST), 1).shuffleGrouping("exclaim2");
        return builder.createTopology();
    }
}