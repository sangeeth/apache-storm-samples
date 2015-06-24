package com.sangeethlabs.storm.contextaware;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WordSpout extends BaseRichSpout implements IContextAware {
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
        Utils.sleep(100);
        
        final String word = words[i];
        i=(i+1)%words.length;
        
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.setAttribute("word-spout", true);
        
        this.collector.emit(new Values(word, executionContext));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", EXECUTION_CONTEXT_ATTR_NAME));
    }
}