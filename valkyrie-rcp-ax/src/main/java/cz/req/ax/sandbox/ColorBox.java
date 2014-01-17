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

    public static String convertToString(Color color) {
        return color == null ? null : "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public static Color convertToColor(String string) {
        return string == null ? null : Color.decode(string);
    }

    protected void showColorPicker() {
        Color color = JColorChooser.showDialog(this, "Color picker", getColor());
        if (color != null) {
            setColor(color);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        getComponent().setText(convertToString(color));
        panel.setBackground(color);
        for (PropertyChangeListener listener : getChangeMulticaster().getListeners()) {
            listener.propertyChange(new PropertyChangeEvent(this, "color", null, color));
        }
    }
}
