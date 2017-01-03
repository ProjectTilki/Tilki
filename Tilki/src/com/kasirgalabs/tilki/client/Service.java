package com.kasirgalabs.tilki.client;

public interface Service<E, T> {

    void request(T data);
}
