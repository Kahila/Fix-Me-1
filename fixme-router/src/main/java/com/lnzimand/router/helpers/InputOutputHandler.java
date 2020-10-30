package com.lnzimand.router.helpers;

import com.lnzimand.router.models.Attachment;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InputOutputHandler implements CompletionHandler<Integer, Attachment> {
    @Override
    public void completed(Integer result, Attachment attachment) {
        
        if (result == -1) {
            try {
                attachment.client.close();
                System.out.format("Stopped listening to the client at: %s%n", attachment.clientAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (attachment.isRead) {
            attachment.buffer.flip();
            int limits = attachment.buffer.limit();
            byte[] bytes = new byte[limits];

            attachment.buffer.get(bytes, 0, limits);
            Charset charset = StandardCharsets.UTF_8;
            String message = new String(bytes, charset);

            System.out.format("Client as %s says: %s%n", attachment.clientAddress, message);
            attachment.isRead = false;
            attachment.buffer.flip();
            attachment.client.write(attachment.buffer, attachment, this);
        } else {
            attachment.isRead = true;
            if (attachment.buffer.hasRemaining()) attachment.buffer.compact();
            else attachment.buffer.clear();
            attachment.client.read(attachment.buffer, attachment, this);
        }
    }

    @Override
    public void failed(Throwable throwable, Attachment attachment) {
        throwable.printStackTrace();
    }
}
