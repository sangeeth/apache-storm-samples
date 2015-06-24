package com.sangeethlabs.storm.contextaware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

abstract public class BaseContextAwareRichBolt extends BaseRichBolt implements IContextAware {
    private static final long serialVersionUID = 1L;
    
    protected OutputCollector collector;
    
    private List<String> outputFields;

    public BaseContextAwareRichBolt(String... outputFields) {
        super();
        this.outputFields = new ArrayList<>();
        this.outputFields.addAll(Arrays.asList(outputFields));
        this.outputFields.add(EXECUTION_CONTEXT_ATTR_NAME);
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }
    
    @Override
    final public void execute(Tuple input) {
        ExecutionContext executionContext = (ExecutionContext)input.getValueByField(EXECUTION_CONTEXT_ATTR_NAME);
        this.execute(executionContext, input);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(outputFields));
    }
    
    abstract public void execute(ExecutionContext executionContext, Tuple input);
    
    protected void emit(Tuple tuple, Values values) {
        ExecutionContext executionContext = (ExecutionContext)tuple.getValueByField(EXECUTION_CONTEXT_ATTR_NAME);
        values.add(executionContext);
        this.collector.emit(tuple, values);
    }
    
    protected void ack(Tuple tuple) {
        this.collector.ack(tuple);
    }
}
