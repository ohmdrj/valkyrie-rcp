/*
 * Copyright 2009-2013 Ondřej Buránek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax.components;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Slider swing component for Integer value selection. Hook ChangeListener.
 *
 * @author Ondrej Burianek
 */
public class IntegerSlider extends JPanel implements ChangeListener, DocumentListener {

    public IntegerSlider() {
        super(new BorderLayout(5, 0));
        setOpaque(false);
        slider.setOpaque(false);
        slider.addChangeListener(this);
        add(slider, BorderLayout.CENTER);
        add(field, BorderLayout.EAST);
        int h = 24;
        field.setMinimumSize(new Dimension(40, h));
        field.setPreferredSize(new Dimension(60, h));
        field.getDocument().addDocumentListener(this);
        setMinimumSize(new Dimension(80, h));
        setPreferredSize(new Dimension(120, h));

    }

    private JSlider slider = new JSlider(0, 0, 0);
    private JFormattedTextField field = new JFormattedTextField();
    private Integer value, minimum, maximum;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        slider.setValue(value == null ? 0 : value);
        field.setText(String.valueOf(value));
        firePropertyChange("", null, value);
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
        slider.setMaximum(maximum == null ? 0 : maximum);
        firePropertyChange("Maximum", null, maximum);
        if (maximum != null && value != null) {
            if (value > maximum) {
                setValue(maximum);
            }
        }
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
        slider.setMinimum(minimum == null ? 0 : minimum);
        firePropertyChange("Minimum", null, minimum);
        if (maximum != null && value != null) {
            if (value < minimum) {
                setValue(minimum);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.value = slider.getValue();
        field.setText(String.valueOf(this.value));
        firePropertyChange("value", null, slider.getValue());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        slider.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    public void setFieldVisible(boolean aFlag) {
        field.setVisible(aFlag);
    }

    public boolean isFieldVisible() {
        return field.isVisible();
    }

    public void setPaintTicks(boolean b) {
        slider.setPaintTicks(b);
    }

    public void setMinorTickSpacing(int n) {
        slider.setMinorTickSpacing(n);
    }

    public void setMajorTickSpacing(int n) {
        slider.setMajorTickSpacing(n);
    }

    public void setSnapToTicks(boolean b) {
        slider.setSnapToTicks(b);
    }

    public boolean getSnapToTicks() {
        return slider.getSnapToTicks();
    }

    public boolean getPaintTicks() {
        return slider.getPaintTicks();
    }

    public int getMinorTickSpacing() {
        return slider.getMinorTickSpacing();
    }

    public int getMajorTickSpacing() {
        return slider.getMajorTickSpacing();
    }

    public void addChangeListener(ChangeListener l) {
        slider.addChangeListener(l);
    }

    private void fieldEvent() {
        try {
            slider.setValue(Integer.parseInt(field.getText()));
        } catch (Exception ex) {
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        fieldEvent();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        fieldEvent();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        fieldEvent();
    }
}
