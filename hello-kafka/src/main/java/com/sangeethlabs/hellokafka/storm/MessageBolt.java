package com.sangeethlabs.hellokafka.storm;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangeethlabs.hellokafka.common.Message;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MessageBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(MessageBolt.class);

    private OutputCollector collector;
    
    public MessageBolt() {
        super();
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        byte[] bytes = (byte[])tuple.getValue(0);
        Message message = null;
        try {
            message = (Message)toObject(bytes);
        } catch (Exception e) {
            logger.error("Failed to parse the message", e);
        }
        this.collector.emit(tuple, new Values(message));
        this.collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
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