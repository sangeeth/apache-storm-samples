package com.sangeethlabs.storm.basic;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordFilterBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;

    private OutputCollector collector;
    
    // The word to be filter from further processing
    private String word;
    
    public WordFilterBolt(String word) {
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
        if (word.startsWith(this.word)) {
            this.collector.fail(tuple);
            return;
        }

        this.collector.emit(tuple, new Values(word));
        this.collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}