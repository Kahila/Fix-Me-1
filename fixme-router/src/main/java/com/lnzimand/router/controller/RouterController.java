package com.lnzimand.router.controller;

import com.lnzimand.router.helpers.Servers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yaqwaqwa
 */
public class RouterController {

    private final String    HOST = "127.0.0.1";
    private final int       PORT_NUMBER = 5000;

    public void startServers() {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new Servers(HOST, PORT_NUMBER));
        service.submit(new Servers(HOST, PORT_NUMBER + 1));
        service.shutdown();
    }

}
