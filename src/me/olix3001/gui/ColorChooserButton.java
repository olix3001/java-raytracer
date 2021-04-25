package me.olix3001.gui;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorChooserButton extends JButton {

    private Color current;

    public ColorChooserButton(me.olix3001.pixeldata.Color c) {
        setSelectedColor(c.toAWTColor());

        addActionListener((e) -> {
            JColorChooser chooser = new JColorChooser();
            chooser.setColor(current);
            me.olix3001.pixeldata.Color before = me.olix3001.pixeldata.Color.fromAWTColor(current);
            chooser.getSelectionModel().addChangeListener((ev) -> {
                setSelectedColor(chooser.getColor());
                c.fromAWT(chooser.getColor());
            });
            JDialog dialog = JColorChooser.createDialog(null, "Choose a color", true, chooser, null, (ev) -> {
                Color b = before.toAWTColor();
                setSelectedColor(b);
                c.fromAWT(b);
            });
            dialog.setVisible(true);
        });
    }

    public me.olix3001.pixeldata.Color getSelectedColor() {
        return me.olix3001.pixeldata.Color.fromAWTColor(current);
    }

    public void setSelectedColor(Color newColor) {
        setSelectedColor(newColor, true);
    }

    public void setSelectedColor(Color newColor, boolean notify) {

        if (newColor == null) return;

        current = newColor;
        setIcon(createIcon(current, 64, 16));
        repaint();

        if (notify) {
            // Notify everybody that may be interested.
            for (ColorChangedListener l : listeners) {
                l.colorChanged(newColor);
            }
        }
    }

    public static interface ColorChangedListener {
        public void colorChanged(Color newColor);
    }

    private List<ColorChangedListener> listeners = new ArrayList<>();

    public void addColorChangedListener(ColorChangedListener toAdd) {
        listeners.add(toAdd);
    }

    public static  ImageIcon createIcon(Color main, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(main);
        graphics.fillRect(0, 0, width, height);
        graphics.setXORMode(Color.DARK_GRAY);
        graphics.drawRect(0, 0, width-1, height-1);
        image.flush();
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
