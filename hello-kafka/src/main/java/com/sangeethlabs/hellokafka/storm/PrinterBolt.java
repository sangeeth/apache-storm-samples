package com.sangeethlabs.hellokafka.storm;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(PrinterBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        byte [] bytes = (byte[])tuple.getValueByField("message");
        try {
            logger.info("Value: {}", toObject(bytes));
        } catch (Exception e) {
            logger.error("Failed to parse the message", e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
    
    private static Object toObject(byte[] bytes) throws Exception {
        Object object = null;
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        object = in.readObject();
        in.close();
        return object;
    }    
}