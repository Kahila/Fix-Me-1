package com.lnzimand.router.helpers;

import com.lnzimand.router.models.Attachment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.Selector;

public class Servers implements Runnable {

    private final String HOST;
    private final int PORT_NUMBER;

    public Servers(String host, int PORT_NUMBER) {
        this.HOST = host;
        this.PORT_NUMBER = PORT_NUMBER;
    }

    @Override
    public void run() {

        AsynchronousServerSocketChannel acceptConnection;
        InetSocketAddress socketAddress;

        try {
            Selector selector = Selector.open();
            acceptConnection = AsynchronousServerSocketChannel.open();
            socketAddress = new InetSocketAddress(HOST, PORT_NUMBER);
            acceptConnection.bind(socketAddress);

            log((PORT_NUMBER == 5000) ? "BROKER" : "MARKET", socketAddress);
            Attachment attachment = new Attachment();
            attachment.server = acceptConnection;

            acceptConnection.accept(attachment, new ConnectionHandler());
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void log(String type, InetSocketAddress address) {
        System.out.format("Server listening for %s, at port: %s\n", type, address);
    }
}
