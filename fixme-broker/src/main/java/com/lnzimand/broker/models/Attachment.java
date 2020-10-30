package com.lnzimand.broker.models;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {

    public AsynchronousSocketChannel channel;
    public ByteBuffer buffer;
    public Thread mainThread;
    public boolean isRead;

}
