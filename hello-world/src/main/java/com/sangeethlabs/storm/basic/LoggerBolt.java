package com.sangeethlabs.storm.basic;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.sangeethlabs.storm.etc.LogClient;

public class LoggerBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;
    
    private String loggerHost;
    
    private LogClient logClient;
    
    public LoggerBolt(String loggerHost) {
        super();
        this.loggerHost = loggerHost;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        logClient = new LogClient(this.loggerHost, "LOGGER");
        logClient.connect();

        super.prepare(stormConf, context);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        logClient.log(tuple.getStringByField("word"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }

    @Override
    public void cleanup() {
        logClient.disconnect();
        super.cleanup();
    }
}