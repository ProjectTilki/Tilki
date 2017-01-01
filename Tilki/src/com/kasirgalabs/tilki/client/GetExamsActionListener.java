package com.kasirgalabs.tilki.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class GetExamsActionListener extends DefaultActionListener<JLabel> {

    public GetExamsActionListener(JLabel component) {
        super(component);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        component.setEnabled(true);
        component.setVisible(true);
        component.setText("Ba\u011Fland\u0131");
        component.setForeground(new Color(26, 126, 36));
    }

}
