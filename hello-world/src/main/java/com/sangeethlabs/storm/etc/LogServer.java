package com.sangeethlabs.storm.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class LogServer {
    public static void main(String[] args) throws IOException {
        startup();
    }
    
    public static void startup() {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(8765);
            Server server = new Server(serverSocket);
            server.start();
        } catch(Exception e) {
            System.err.printf("Error while starting the Log Server. Reason: %s\n", e.getMessage());
        }
    }

    static class Server extends Thread {
        private ServerSocket serverSocket;

        public Server(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = this.serverSocket.accept();
                    ClientService service = new ClientService(socket);
                    service.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    static class ClientService extends Thread {
        private Socket socket;

        public ClientService(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientAddress = this.socket.getRemoteSocketAddress().toString();
            System.out.println(String.format("%s $ Connected", clientAddress));
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                String command = null;
                while ((command = in.readLine()) != null) {
                    if ("quit".equals(command)) {
                        break;
                    }
                    System.out.println(String.format("%s > %s", clientAddress, command));
                }
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
