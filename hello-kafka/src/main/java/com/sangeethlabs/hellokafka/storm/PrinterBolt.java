package com.sangeethlabs.hellokafka.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        System.out.printf("PRINTER: %s\n", tuple.getValueByField("message"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
}