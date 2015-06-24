package com.sangeethlabs.storm.contextaware;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExclamationBolt extends BaseContextAwareRichBolt {
    private static final long serialVersionUID = 1L;

    public ExclamationBolt() {
        super(/*Output Fields are*/"word");
    }

    @Override
    public void execute(ExecutionContext executionContext, Tuple tuple) {
        executionContext.setAttribute("exclamation-bolt", true);
        String word = tuple.getStringByField("word");
        String output = word + "!!!";
        
        super.emit(tuple, new Values(output));
        super.ack(tuple);
    }
}