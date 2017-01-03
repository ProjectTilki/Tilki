package com.kasirgalabs.tilki.client;

import java.util.ArrayList;
import javax.swing.text.JTextComponent;

public class EmptyTextComponentCounter {

    private final ArrayList<JTextComponent> list = new ArrayList<>();

    public void addComponent(JTextComponent component) {
        if(!list.contains(component)) {
            list.add(component);
        }
    }

    public void removeComponent(JTextComponent component) {
        if(list.contains(component)) {
            list.remove(component);
        }
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}
