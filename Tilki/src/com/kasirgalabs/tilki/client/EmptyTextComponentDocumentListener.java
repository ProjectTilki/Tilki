package com.kasirgalabs.tilki.client;

import java.awt.Container;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class EmptyTextComponentDocumentListener implements DocumentListener {

    private final JTextComponent component;
    private final Container container;
    private final EmptyTextComponentCounter emptyTextComponentCounter;

    public EmptyTextComponentDocumentListener(JTextComponent component,
            Container container,
            EmptyTextComponentCounter emptyTextComponentCounter) {
        this.component = component;
        this.container = container;
        this.emptyTextComponentCounter = emptyTextComponentCounter;
        emptyTextComponentCounter.addComponent(component);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        adjustCounter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        adjustCounter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        adjustCounter();
    }

    private void adjustCounter() {
        if(component instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) component;
            if(passwordField.getPassword().length == 0) {
                emptyTextComponentCounter.addComponent(component);
                container.setEnabled(false);
                return;
            }
        }
        if(component.getText().trim().isEmpty()) {
            emptyTextComponentCounter.addComponent(component);
            container.setEnabled(false);
            return;
        }
        emptyTextComponentCounter.removeComponent(component);
        if(emptyTextComponentCounter.isEmpty()) {
            container.setEnabled(true);
            return;
        }
        container.setEnabled(false);
    }
}
