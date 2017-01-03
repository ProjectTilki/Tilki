package com.kasirgalabs.tilki.client;

import java.awt.Container;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class EmptyTextFieldDocumentListener implements DocumentListener {

    private final JTextComponent component;
    private final Container container;

    public EmptyTextFieldDocumentListener(JTextComponent component,
            Container container) {
        this.component = component;
        this.container = container;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        ifEmptyDisable();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        ifEmptyDisable();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        ifEmptyDisable();
    }

    private void ifEmptyDisable() {
        if(component instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) component;
            if(passwordField.getPassword().length == 0) {
                container.setEnabled(false);
                return;
            }
        }
        if(component.getText().trim().isEmpty()) {
            container.setEnabled(false);
            return;
        }
        container.setEnabled(true);
    }

}
