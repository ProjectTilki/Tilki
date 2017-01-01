package com.kasirgalabs.tilki.client;

import java.net.Socket;

public class ServiceFactory {

    private final Socket socket;

    public ServiceFactory(Socket socket) {
        this.socket = socket;
    }

    public DefaultService getService(String service) {
        return null;
    }
}
