package com.sangeethlabs.storm.contextaware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backtype.storm.task.OutputCollector;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

abstract public class BaseContextAwareBasicBolt extends BaseBasicBolt implements IContextAware {
    private static final long serialVersionUID = 1L;
    
    protected OutputCollector collector;
    
    private List<String> outputFields;

    public BaseContextAwareBasicBolt(String... outputFields) {
        super();
        this.outputFields = new ArrayList<>();
        this.outputFields.addAll(Arrays.asList(outputFields));
        this.outputFields.add(EXECUTION_CONTEXT_ATTR_NAME);
    }

    @Override
    final public void execute(Tuple input, BasicOutputCollector collector) {
        ExecutionContext executionContext = (ExecutionContext)input.getValueByField(EXECUTION_CONTEXT_ATTR_NAME);
        this.execute(executionContext, input, collector);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(outputFields));
    }
    
    abstract public void execute(ExecutionContext executionContext, Tuple input, BasicOutputCollector collector);
}
