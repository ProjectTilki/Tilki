package com.kasirgalabs.tilki.client;

public interface Service<E> {

    void request();

    E getResult() throws Exception;
}
