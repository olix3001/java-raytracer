package me.olix3001.gui;

import javax.swing.*;

public class FloatSpinner extends JSpinner {
    private static final float STEP = 0.01f;

    private SpinnerNumberModel model;

    public FloatSpinner(float start) {
        super();
        model = new SpinnerNumberModel(start, null, null, STEP);
        this.setModel(model);
    }

    public float getFloat() {
        return (float) getValue();
    }
}
