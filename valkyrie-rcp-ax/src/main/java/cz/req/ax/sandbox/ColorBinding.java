package cz.req.ax.sandbox;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ColorBinding
 *
 * @author Ondrej Burianek
 */
public class ColorBinding extends CustomBinding {

    ColorBox colorBox;
    Class propertyClass;

    public ColorBinding(FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, detectPropertyClass(formModel, formPropertyPath));
        propertyClass = detectPropertyClass(formModel, formPropertyPath);
        colorBox = new ColorBox();
    }

    static Class detectPropertyClass(FormModel formModel, String formPropertyPath) {
        Class detected = formModel.getFieldMetadata(formPropertyPath).getPropertyType();
        return detected;
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        if (newValue instanceof String && propertyClass.isAssignableFrom(String.class)) {
            newValue = Color.decode((String) newValue);
        }
        if (newValue instanceof Color && propertyClass.isAssignableFrom(Color.class)) {
            colorBox.setColor((Color) newValue);
        } else {
            colorBox.setColor(Color.WHITE);
        }
    }

    @Override
    protected JComponent doBindControl() {
        colorBox.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                controlValueChanged(evt.getNewValue());
            }
        });
        return colorBox;
    }

    @Override
    protected void readOnlyChanged() {
//        colorBox.setEditable()
    }

    @Override
    protected void enabledChanged() {
        colorBox.setEnabled(isEnabled());
//        readOnlyChanged();
    }
}
