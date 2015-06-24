package com.sangeethlabs.storm.contextaware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

abstract public class BaseContextAwareRichSpout extends BaseRichSpout implements IContextAware {
    private static final long serialVersionUID = 1L;
    
    private SpoutOutputCollector collector;
    
    private List<String> outputFields;

    public BaseContextAwareRichSpout(String... outputFields) {
        super();
        this.outputFields = new ArrayList<>();
        this.outputFields.addAll(Arrays.asList(outputFields));
        this.outputFields.add(EXECUTION_CONTEXT_ATTR_NAME);
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    protected void emit(ExecutionContext executionContext, Values values) {
        values.add(executionContext);
        this.collector.emit(values);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", EXECUTION_CONTEXT_ATTR_NAME));
    }
}
