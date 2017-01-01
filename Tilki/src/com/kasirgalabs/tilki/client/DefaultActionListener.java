package com.kasirgalabs.tilki.client;

import java.awt.event.ActionListener;
import javax.swing.JComponent;

public abstract class DefaultActionListener<V extends JComponent> implements
        ActionListener {

    protected V component;

    protected DefaultActionListener(V component) {
        this.component = component;
    }
}
