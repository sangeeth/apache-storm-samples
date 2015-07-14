package com.sangeethlabs.storm.etc;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LogClient {
    private Socket socket;
    
    private PrintWriter out;
    
    private String logServerHost;
    
    private String label;
    
    public LogClient(String logServerHost, String label) {
        super();
        this.logServerHost = logServerHost;
        this.label = label;
    }

    public void connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(logServerHost, 8765));
            
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            out.printf("%s: Connected\n", label);
            out.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void disconnect() {
        if (socket!=null) {
            try {
                out.printf("%s: Disconnecting\n", label);
                out.flush();
                
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message) {
        if (out!=null) {
            try {
                out.println(message);
                out.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
