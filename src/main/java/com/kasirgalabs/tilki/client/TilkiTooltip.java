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

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * TilkiTooltip is a utility class for creating custom tooltips.
 * Such as a tooltip with short delays.
 */
public final class TilkiTooltip {
    public static final double DEFAULT_DURATION = 100.0;
    private static final Logger LOG = Logger.getLogger(TilkiTooltip.class.getName());

    private TilkiTooltip() {

    }

    /**
     * Returns a custom tooltip with the specified text.
     * The tooltip will have short delays.
     *
     * @param message The text to display in the tooltip.
     *
     * @return A custom tooltip.
     */
    public static Tooltip getCustomTooltip(String message) {
        Tooltip tooltip = new Tooltip(message);
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);
            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(DEFAULT_DURATION)));
        } catch(NoSuchFieldException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return tooltip;
    }

    public static Tooltip getCustomTooltip() {
        return getCustomTooltip("");
    }
}
