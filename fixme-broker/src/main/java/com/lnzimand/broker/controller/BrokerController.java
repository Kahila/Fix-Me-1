package com.lnzimand.broker.controller;

import com.lnzimand.broker.helpers.InputOutputHandler;
import com.lnzimand.broker.models.Attachment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BrokerController {

    private final String    HOST = "127.0.0.1";
    private final int       PORT_NUMBER = 5000;

    public void connect() {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            SocketAddress serverAddress = new InetSocketAddress(HOST, PORT_NUMBER);
            Future<Void> result = socketChannel.connect(serverAddress);
            result.get();

            log("Connected to [SERVER] at address: " + socketChannel.getLocalAddress());

            Attachment attachment = new Attachment();
            attachment.channel = socketChannel;
            attachment.buffer = ByteBuffer.allocate(1024);
            attachment.isRead = true;
            attachment.mainThread = Thread.currentThread();

            Charset charset = StandardCharsets.UTF_8;
            String message = "Hello";
            byte[] data = message.getBytes(charset);
            attachment.buffer.put(data);
            attachment.buffer.flip();

            InputOutputHandler ioHandler = new InputOutputHandler();
            socketChannel.write(attachment.buffer, attachment, ioHandler);
            attachment.mainThread.join();

        } catch (InterruptedException | IOException | ExecutionException e) {
            System.err.println(e.getMessage());
        }

    }

    private void log(String string) {
        System.out.println(string);
    }

}
