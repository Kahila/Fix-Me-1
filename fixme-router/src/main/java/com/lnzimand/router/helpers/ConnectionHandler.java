package com.lnzimand.router.helpers;

import com.lnzimand.router.models.Attachment;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Attachment attachment) {
        try {
            SocketAddress clientAddress = socketChannel.getRemoteAddress();
            log("Accepted a connection from: " + clientAddress);
            attachment.server.accept(attachment, this);
            InputOutputHandler ioHandler = new InputOutputHandler();
            Attachment newClient = new Attachment();
            newClient.server = attachment.server;
            newClient.client = socketChannel;
            newClient.buffer = ByteBuffer.allocate(1024);
            newClient.isRead = true;
            newClient.clientAddress = clientAddress;
            socketChannel.read(newClient.buffer, newClient, ioHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {
        System.out.println("Failed to accept a connection.");
        throwable.printStackTrace();
    }

    private void log(String message) {
        System.out.println(message);
    }

}
