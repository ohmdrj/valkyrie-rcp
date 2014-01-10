package cz.req.ax.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * AbstractBoxedField
 *
 * @author Ondrej Burianek
 */
public class AbstractBoxedField<T extends Component> extends JPanel {

    T component;
    JButton button;

    public AbstractBoxedField(T component) {
        super(new BorderLayout(0, 0));
        int add = 1;

        this.component = component;
        component.setMinimumSize(new Dimension(component.getMinimumSize().width, component.getMinimumSize().height + add));
        component.setPreferredSize(new Dimension(component.getPreferredSize().height, component.getPreferredSize().height + add));

        this.button = new JButton();
//        button.setIcon(UIManager.getIcon("LookupBox.arrowIcon"));
        button.setIcon(UIManager.getIcon(getClass().getSimpleName()+".arrowIcon"));
        button.setMinimumSize(new Dimension(26, component.getMinimumSize().height + add));
        button.setPreferredSize(new Dimension(26, component.getPreferredSize().height + add));
        add(component, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);

        component.addPropertyChangeListener("enabled", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                button.setEnabled((Boolean) evt.getNewValue());
            }
        });
    }

    public T getComponent() {
        return component;
    }

    public JButton getButton() {
        return button;
    }

    public void addButtonListener(ActionListener l) {
        button.addActionListener(l);
    }

    public void removeButtonListener(ActionListener l) {
        button.removeActionListener(l);
    }


}
