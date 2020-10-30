package com.lnzimand.router;

import com.lnzimand.router.controller.RouterController;

/**
 * @author yaqwaqwa
 */
public class App {
    public static void main( String[] args ) {
        RouterController routerController = new RouterController();
        routerController.startServers();
    }
}
