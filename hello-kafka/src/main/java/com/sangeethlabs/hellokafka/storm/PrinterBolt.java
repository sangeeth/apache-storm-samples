package com.sangeethlabs.hellokafka.storm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangeethlabs.hellokafka.common.Message;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(PrinterBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        Message message = (Message)tuple.getValueByField("message");
        logger.info("PRINTER: {}", message);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
}