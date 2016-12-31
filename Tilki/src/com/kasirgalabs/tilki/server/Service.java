package com.kasirgalabs.tilki.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface Service {

    void serve() throws IOException, NoSuchAlgorithmException;
}
