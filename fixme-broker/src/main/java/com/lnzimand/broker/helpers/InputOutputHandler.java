package com.lnzimand.broker.helpers;

import com.lnzimand.broker.models.Attachment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InputOutputHandler implements CompletionHandler<Integer, Attachment> {

    @Override
    public void completed(Integer result, Attachment attachment) {
        if (attachment.isRead) {
            attachment.buffer.flip();
            Charset charset = StandardCharsets.UTF_8;
            int limits = attachment.buffer.limit();
            byte[] bytes = new byte[limits];
            attachment.buffer.get(bytes, 0, limits);
            String message = new String(bytes,charset);
            log("[SERVER] responded: " + message);

            message = this.getTextFromUser();

            if (message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("quit")) {
                try {
                    attachment.channel.shutdownInput();
                    attachment.channel.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                return;
            }

            attachment.buffer.clear();
            byte[] data = message.getBytes(charset);
            attachment.buffer.put(data);
            attachment.buffer.flip();
            attachment.isRead = false;
            attachment.channel.write(attachment.buffer, attachment, this);

        } else {
            attachment.isRead = true;
            attachment.channel.read(attachment.buffer, attachment, this);
            attachment.buffer.clear();
        }

    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {
        throwable.printStackTrace();
    }

    private void log(String string) {
        System.out.println(string);
    }

    private String getTextFromUser() {

        System.out.println("Please enter a message (Exit to quit)");
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String message = null;

        try {
            message = console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }
}
