package me.olix3001.gui;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.solids.Solid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GuiUtils {

    public static JPanel createVectorEdition(Vector3 vector, boolean refresh) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("X: ", SwingConstants.TRAILING));
        labels.add(new JLabel("Y: ", SwingConstants.TRAILING));
        labels.add(new JLabel("Z: ", SwingConstants.TRAILING));
        panel.add(labels, BorderLayout.LINE_START);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        FloatSpinner xSpinner = new FloatSpinner(vector.getX());
        controls.add(xSpinner);
        FloatSpinner ySpinner = new FloatSpinner(vector.getY());
        controls.add(ySpinner);
        FloatSpinner zSpinner = new FloatSpinner(vector.getZ());
        controls.add(zSpinner);
        panel.add(controls, BorderLayout.CENTER);

        if (refresh) {
            xSpinner.addChangeListener((e) -> {
                vector.setX(xSpinner.getFloat());
            });
            ySpinner.addChangeListener((e) -> {
                vector.setY(ySpinner.getFloat());
            });
            zSpinner.addChangeListener((e) -> {
                vector.setZ(zSpinner.getFloat());
            });
        }

        return panel;
    }

    public static JPanel createLabelComponentPair(String label, JComponent component) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel(label));

        c.gridx = 1;
        panel.add(component);

        return panel;
    }

    public static JPanel createNameEdit(Solid s, JFrame frame) {
        JTextField nameField = new JTextField(s.getName());
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                s.setName(nameField.getText());
                frame.setTitle(s.getName() + " properties");
            }
        });
        nameField.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        return createLabelComponentPair("Name: ", nameField);
    }

    public static JPanel createColorPicker(String name, Color c) {
        return createLabelComponentPair(name, new ColorChooserButton(c));
    }
}
