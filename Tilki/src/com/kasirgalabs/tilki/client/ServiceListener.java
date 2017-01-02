package com.kasirgalabs.tilki.client;

import java.util.concurrent.Future;

public interface ServiceListener<E> {

    void servicePerformed(Future<E> future);
}
