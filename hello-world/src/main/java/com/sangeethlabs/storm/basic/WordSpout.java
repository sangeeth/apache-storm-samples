package com.sangeethlabs.storm.basic;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WordSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1L;
    
    private String[] words;
    
    private SpoutOutputCollector collector;
    
    private int i;
    
    public WordSpout(String [] words) {
        this.words = words;
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        Utils.sleep(1000);
        
        final String word = words[i];
        i=(i+1)%words.length;
        this.collector.emit(new Values(word));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}