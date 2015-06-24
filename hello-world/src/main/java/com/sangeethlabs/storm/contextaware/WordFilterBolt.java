package com.sangeethlabs.storm.contextaware;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordFilterBolt extends BaseContextAwareRichBolt {
    private static final long serialVersionUID = 1L;

    // The word to be filter from further processing
    private String word;
    
    public WordFilterBolt(String word) {
        super(/*Output Fields are*/"word");
        
        this.word = word;
    }

    @Override
    public void execute(ExecutionContext executionContext, Tuple tuple) {
        executionContext.setAttribute("word-filter-bolt", true);
        
        String word = tuple.getStringByField("word");
        if (word.startsWith(this.word)) {
            this.collector.fail(tuple);
            return;
        }

        super.emit(tuple, new Values(word));
        super.ack(tuple);
    }
}