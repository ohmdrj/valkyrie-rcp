package cz.req.ax.sandbox;

import cz.req.ax.components.AbstractBoxedField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ColorBox
 *
 * @author Ondrej Burianek
 */
public class ColorBox extends AbstractBoxedField<JTextField> {

    Color color;
    JPanel panel;

    public ColorBox() {
        super(new JTextField());
        getComponent().setEditable(false);

        panel = new JPanel();
        panel.setMinimumSize(getButton().getMinimumSize());
        panel.setPreferredSize(getButton().getPreferredSize());
        panel.setBorder(getComponent().getBorder());
        add(panel, BorderLayout.WEST);
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showColorPicker();
            }
        });
    }

    public ColorBox(JTextField component) {
        super(component);
    }

    protected void showColorPicker() {
        setColor(JColorChooser.showDialog(this, "Color picker", getColor()));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        String colorHex = "#" + Integer.toHexString(color.getRGB()).substring(2);
        getComponent().setText(colorHex);
        panel.setBackground(color);
        for (PropertyChangeListener listener : getChangeMulticaster().getListeners()) {
            listener.propertyChange(new PropertyChangeEvent(this, "color", null, color));
        }
    }
}
