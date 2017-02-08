/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.tilki.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TilkiTest {

    // Wrapper thread updates this if
    // the JavaFX application runs without problem.
    private volatile boolean success = false;

    /**
     * Test that a JavaFX application launches.
     */
    @Test
    public void testMain() {
        Thread thread = new Thread() { // Wrapper thread.
            @Override
            public void run() {
                try {
                    Application.launch(Tilki.class); // Run JavaFX application.
                    success = true;
                } catch(Throwable t) {
                    if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                        // We expect to get this exception since we interrupted
                        // the JavaFX application.
                        success = true;
                        return;
                    }
                    // This is not the exception we are looking for so log it.
                    Logger.getLogger(TilkiTest.class.getName()).log(Level.SEVERE, null, t);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting JavaFX application
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            // We don't care if we wake up early.
        }
        assertTrue(success);
    }
}
