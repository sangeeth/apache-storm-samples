package com.sangeethlabs.storm.contextaware;

import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class WordSpout extends BaseContextAwareRichSpout {
    private static final long serialVersionUID = 1L;
    
    private String[] words;
    
    private int i;
    
    public WordSpout(String [] words) {
        super("words");
        this.words = words;
    }

    @Override
    public void nextTuple() {
        Utils.sleep(100);
        
        final String word = words[i];
        i=(i+1)%words.length;
        
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.setAttribute("word-spout", true);
        
        super.emit(executionContext, new Values(word));
    }
}