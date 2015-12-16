package com.sangeethlabs.hellokafka.storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MessageBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;

    private OutputCollector collector;
    
    public MessageBolt() {
        super();
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        Object value = tuple.getValue(0);
//        Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
//        String word = map.get("word").toString();
        this.collector.emit(tuple, new Values(value));
        this.collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}