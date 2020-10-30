package com.lnzimand.broker;

import com.lnzimand.broker.controller.*;

/**
 * @author yaqwaqwa
 */
public class App {
    public static void main( String[] args ) {
        BrokerController controller = new BrokerController();
        controller.connect();
    }
}
