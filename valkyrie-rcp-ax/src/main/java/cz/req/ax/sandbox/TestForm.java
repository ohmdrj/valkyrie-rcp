package cz.req.ax.sandbox;

import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.widget.AxFormFactory;
import cz.req.ax.widget.AxWidget;
import org.valkyriercp.binding.form.CommitListener;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.BigDecimalTextField;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * TestForm
 *
 * @author Ondrej Burianek
 */
public class TestForm extends AxWidget {

    @Override
    public void createWidget() {
        AxFormFactory formFactory = new AxFormFactory(TestItem.class) {

            JTextField fieldText;
            BigDecimalTextField fieldDecimal;

            @Override
            public void initForm(AxFormBuilder builder) {
                fieldText = new JTextField("...");
                fieldText.setEditable(false);
                fieldDecimal = new BigDecimalTextField(10, 4, true, BigDecimal.class,
                        new DecimalFormat("### ### ##0.00"), new DecimalFormat("0.##"));

                builder.setStandardPreset1();
                builder.addPropertyWithLabel("string");
                builder.addPropertyWithLabel("numberInteger");
                builder.addPropertyWithLabel("numberDecimal");

                builder.addComponentWithLabel("fieldText", fieldText);
                builder.addComponentWithLabel("fieldDecimal", fieldDecimal);


                builder.getFormModel().addCommitListener(new CommitListener() {
                    @Override
                    public void preCommit(FormModel formModel) {
                        System.out.println("PreCommit");
                    }

                    @Override
                    public void postCommit(FormModel formModel) {
                        System.out.println("PostCommit");
                    }
                });

                builder.getFormModel().getFormObjectHolder().addValueChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        System.out.println("Bark");
                    }
                });
                builder.getFormModel().getValueModel("numberInteger").addValueChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        System.out.println("Fool");
                        if (evt.getNewValue() instanceof Integer) {
                            Integer value = (Integer) evt.getNewValue();
                            fieldText.setText(String.valueOf(value / 100f));
                        }
                    }
                });
//                builder.getFormModel().addPropertyChangeListener(new PropertyChangeListener() {
//                    @Override
//                    public void propertyChange(PropertyChangeEvent evt) {
//                        System.out.println("Event! ");
//                        if (evt.getPropertyName().equals("numberInteger")) {
//                            Integer value = (Integer) evt.getNewValue();
//                            fieldText.setText(String.valueOf(value / 100f));
//                        }
//                    }
//                });
            }
        };
        formFactory.getForm().setFormObject(TestItem.itemsList.get(0));
        addRow(formFactory.getForm());
//        TableLookupBox lookupBox = new TableLookupBox(new AxDataProvider(TestItem.class) {
//            @Override
//            public List getList(Object criteria) {
//                return TestItem.itemsList;
//            }
//        }, "string");
//        lookupBox.getTableDescription().add("string", SortOrder.ASCENDING).add("date").add("numberDecimal");
//        addRow(lookupBox);
    }

}
