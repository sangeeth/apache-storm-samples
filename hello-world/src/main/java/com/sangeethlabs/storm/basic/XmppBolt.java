package com.sangeethlabs.storm.basic;

import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class XmppBolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(XmppBolt.class);
    private static final long serialVersionUID = 1L;

    public static final String XMPP_TO = "storm.xmpp.to";
    public static final String XMPP_USER = "storm.xmpp.user";
    public static final String XMPP_PASSWORD = "storm.xmpp.password";
    public static final String XMPP_SERVER = "storm.xmpp.server";

    private XMPPConnection xmppConnection;
    private String to;

    public XmppBolt() {
        super();
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);

        logger.info("Prepare: {}", stormConf);
        this.to = (String) stormConf.get(XMPP_TO);
        ConnectionConfiguration config = new ConnectionConfiguration((String) stormConf.get(XMPP_SERVER));
        this.xmppConnection = new XMPPConnection(config);
        try {
            this.xmppConnection.connect();
            this.xmppConnection.login((String) stormConf.get(XMPP_USER), (String) stormConf.get(XMPP_PASSWORD));
        } catch (XMPPException e) {
            logger.warn("Error initializing XMPP Channel", e);
        }
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        Message msg = new Message(this.to, Message.Type.normal);
        msg.setBody(String.format("> %s\n", tuple.getStringByField("word")));
        this.xmppConnection.sendPacket(msg);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
}