package com.sangeethlabs.storm.basic;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ConditionBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;

    private OutputCollector collector;
    
    // The word to be filter from further processing
    private String word;
    
    public ConditionBolt(String word) {
        super();
        this.word = word;
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        String word = tuple.getStringByField("word");
        boolean output = word.equals(this.word);
        this.collector.emit(tuple, new Values(output, word));
        this.collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("condition.output","word"));
    }
}