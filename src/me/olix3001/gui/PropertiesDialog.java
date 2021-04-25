package me.olix3001.gui;

import me.olix3001.solids.Solid;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class PropertiesDialog extends JFrame {
    private static PropertiesDialog propertiesDialog;

    public void clear() {
        getContentPane().removeAll();
    }

    public void setEmpty() {
        JLabel label = new JLabel("Select an object to edit it's properties");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label);
        label.setVisible(true);
    }

    private PropertiesDialog() {
        super("Object properties");
        Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
        setLayout(new FlowLayout());
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


        setEmpty();
        setVisible(true);
    }

    public static PropertiesDialog get() {
        if (propertiesDialog == null) {
            propertiesDialog = new PropertiesDialog();
        }
        if (!propertiesDialog.isVisible()) {
            propertiesDialog.setVisible(true);
        }
        return propertiesDialog;
    }

    public void editProperties(Solid s) {
        clear();
        setTitle(s.getName() + " properties");
        s.propertiesDialog(this);
        revalidate();
        repaint();
    }
}
