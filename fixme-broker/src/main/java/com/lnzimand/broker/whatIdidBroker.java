package com.lnzimand.broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class whatIdidBroker {
    //    public whatIdidBroker() throws IOException {
//    }
    public static void main(String[] args) throws IOException {
        whatIdidBroker what = new whatIdidBroker();
        what.connect();
    }

    public void connect() throws IOException {
        String hostName = "localhost";
        int portNumber = 5000;
        try {
            Socket s = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //waiting for the market to complete the buy or sell massage
            //this is how i did it


        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);

        }
    }
}
