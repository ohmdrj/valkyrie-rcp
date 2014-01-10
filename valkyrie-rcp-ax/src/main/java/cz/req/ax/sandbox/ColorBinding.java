package cz.req.ax.sandbox;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.awt.*;

/**
 * ColorBinding
 *
 * @author Ondrej Burianek
 */
public class ColorBinding extends CustomBinding {

    ColorBox colorBox;

    public ColorBinding(FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, Color.class);
        colorBox = new ColorBox();
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        if (newValue instanceof Color) {
            colorBox.setColor((Color) newValue);
        } else {
            colorBox.setColor(Color.WHITE);
        }
    }

    @Override
    protected JComponent doBindControl() {
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
