package com.sangeethlabs.storm.contextaware;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseContextAwareBasicBolt {
    private static final long serialVersionUID = 1L;

    public PrinterBolt() {
        super();
    }

    @Override
    public void execute(ExecutionContext executionContext, Tuple tuple, BasicOutputCollector collector) {
        executionContext.setAttribute("printer-bolt", true);
        
        System.out.printf("PRINTER: %s,  ExecutionContext: %s\n", tuple.getStringByField("word"), executionContext.getAttributes());
    }
}