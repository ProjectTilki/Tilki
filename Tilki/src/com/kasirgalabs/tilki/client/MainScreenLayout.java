package com.kasirgalabs.tilki.client;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

public class MainScreenLayout extends CardLayout {

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return currentComponentSize(parent);
    }

    private Component findCurrentComponent(Container parent) {
        for(Component comp : parent.getComponents()) {
            if(comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }

    private Dimension currentComponentSize(Container parent) {
        Component current = findCurrentComponent(parent);
        Insets insets = parent.getInsets();
        Dimension pref = current.getPreferredSize();
        pref.width += insets.left + insets.right;
        pref.height += insets.top + insets.bottom;
        return pref;
    }
}
