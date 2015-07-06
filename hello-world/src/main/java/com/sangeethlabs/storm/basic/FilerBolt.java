package com.sangeethlabs.storm.basic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class FilerBolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(FilerBolt.class);
    private static final long serialVersionUID = 1L;

    private File file;
    
    public FilerBolt(File file) {
        super();
        this.file = file;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            logger.info("Writing to " + file.getAbsolutePath());
            
            FileWriter fout = new FileWriter(file,true);
            PrintWriter out = new PrintWriter(fout);
            out.printf("> %s\n", tuple.getStringByField("word"));
            out.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
}