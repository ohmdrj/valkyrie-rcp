package org.valkyriercp.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.binding.format.support.AbstractFormatterFactory;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.component.PatchedJFormattedTextField;
import org.valkyriercp.core.support.LabelInfo;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.util.Alignment;
import org.valkyriercp.util.GuiStandardUtils;
import org.valkyriercp.util.UIConstants;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Default component factory implementation that delegates to JGoodies component
 * factory.
 *
 * @author Keith Donald
 */
public class DefaultComponentFactory implements ComponentFactory, MessageSourceAware {

	private final Log logger = LogFactory.getLog(getClass());

	private int textFieldColumns = 25;
    private MessageSource messageSource;


    public TableFactory getTableFactory() {
        return ValkyrieRepository.getInstance().getApplicationConfig().tableFactory();
    }

    public MessageSource getMessageSource() {
        if(messageSource == null)
            return ValkyrieRepository.getInstance().getApplicationConfig().messageSource();
        return messageSource;
    }

    /**
	 * {@inheritDoc}
	 */
	public JLabel createLabel(String labelKey) {
		JLabel label = createNewLabel();
		getLabelInfo(getRequiredMessage(labelKey)).configureLabel(label);
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	public JLabel createLabel(String[] labelKeys) {
		JLabel label = createNewLabel();
		getLabelInfo(getRequiredMessage(labelKeys)).configureLabel(label);
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	public JLabel createLabel(String labelKey, Object[] arguments) {
		JLabel label = createNewLabel();
		getLabelInfo(getRequiredMessage(labelKey, arguments)).configureLabel(label);
		return label;
	}

	/**
	 * Parse the given label to create a {@link LabelInfo}.
	 *
	 * @param label The label to parse.
	 * @return a {@link LabelInfo} representing the label.
	 * @see LabelInfo#valueOf(String)
	 */
	protected LabelInfo getLabelInfo(String label) {
		return LabelInfo.valueOf(label);
	}

	/**
	 * Get the message for the given key. Don't throw an exception if it's not
	 * found but return a default value.
	 *
	 * @param messageKey Key to lookup the message.
	 * @return the message found in the resources or a default message.
	 */
	protected String getRequiredMessage(String messageKey) {
		return getRequiredMessage(messageKey, null);
	}

	/**
	 * Get the message for the given key. Don't throw an exception if it's not
	 * found but return a default value.
	 *
	 * @param messageKeys The keys to use when looking for the message.
	 * @return the message found in the resources or a default message.
	 */
	protected String getRequiredMessage(final String[] messageKeys) {
		MessageSourceResolvable resolvable = new MessageSourceResolvable() {

			public String[] getCodes() {
				return messageKeys;
			}

			public Object[] getArguments() {
				return null;
			}

			public String getDefaultMessage() {
				if (messageKeys.length > 0) {
					return messageKeys[0];
				}
				return "";
			}
		};
		return getMessages().getMessage(resolvable);
	}

	/**
	 * Returns the messageSourceAccessor.
	 */
	private MessageSourceAccessor getMessages() {
		return ValkyrieRepository.getInstance().getApplicationConfig().messageSourceAccessor();
	}

	/**
	 * {@inheritDoc}
	 */
	public JLabel createLabel(String labelKey, ValueModel[] argumentValueHolders) {
		return new LabelTextRefresher(labelKey, argumentValueHolders).getLabel();
	}

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private class LabelTextRefresher implements PropertyChangeListener {

		private String labelKey;

		private JLabel label;

		private ValueModel[] argumentHolders;

		public LabelTextRefresher(String labelKey, ValueModel[] argumentHolders) {
			this.labelKey = labelKey;
			this.argumentHolders = argumentHolders;
			this.label = createNewLabel();
			subscribe();
			updateLabel();
		}

		private void subscribe() {
			for (int i = 0; i < argumentHolders.length; i++) {
				ValueModel argHolder = argumentHolders[i];
				argHolder.addValueChangeListener(this);
			}
		}

		public JLabel getLabel() {
			return label;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			updateLabel();
		}

		private void updateLabel() {
			Object[] argValues = new Object[argumentHolders.length];
			for (int i = 0; i < argumentHolders.length; i++) {
				ValueModel argHolder = argumentHolders[i];
				argValues[i] = argHolder.getValue();
			}
			getLabelInfo(getRequiredMessage(labelKey, argValues)).configureLabel(label);
		}
	}

	private String getRequiredMessage(String messageKey, Object[] args) {
		try {
			String message = getMessages().getMessage(messageKey, args);
			return message;
		}
		catch (NoSuchMessageException e) {
			return messageKey;
		}
	}

	public JLabel createTitleLabel(String labelKey) {
		return com.jgoodies.forms.factories.DefaultComponentFactory.getInstance().createTitle(
				getRequiredMessage(labelKey));
	}

	public JComponent createTitledBorderFor(String labelKey, JComponent component) {
		component.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder(getRequiredMessage(labelKey)), GuiStandardUtils
                .createEvenlySpacedBorder(UIConstants.ONE_SPACE)));
		return component;
	}

	public JLabel createLabelFor(String labelKey, JComponent component) {
		JLabel label = createNewLabel();
		getLabelInfo(getRequiredMessage(labelKey)).configureLabelFor(label, component);
		return label;
	}

	public JLabel createLabelFor(String[] labelKeys, JComponent component) {
		JLabel label = createNewLabel();
		getLabelInfo(getRequiredMessage(labelKeys)).configureLabelFor(label, component);
		return label;
	}

	protected JLabel createNewLabel() {
		return new JLabel();
	}

	public JButton createButton(String labelKey) {
		return (JButton) getButtonLabelInfo(getRequiredMessage(labelKey)).configure(getButtonFactory().createButton());
	}

	protected CommandButtonLabelInfo getButtonLabelInfo(String label) {
		return CommandButtonLabelInfo.valueOf(label);
	}

	protected ButtonFactory getButtonFactory() {
		return ValkyrieRepository.getInstance().getApplicationConfig().buttonFactory();
	}

	public JComponent createLabeledSeparator(String labelKey) {
		return createLabeledSeparator(labelKey, Alignment.LEFT);
	}

	public JCheckBox createCheckBox(String labelKey) {
		return (JCheckBox) getButtonLabelInfo(getRequiredMessage(labelKey)).configure(createNewCheckBox());
	}

	public JCheckBox createCheckBox(String[] labelKeys) {
		return (JCheckBox) getButtonLabelInfo(getRequiredMessage(labelKeys)).configure(createNewCheckBox());
	}

	protected JCheckBox createNewCheckBox() {
		return new JCheckBox();
	}

	public JToggleButton createToggleButton(String labelKey) {
		return (JToggleButton) getButtonLabelInfo(getRequiredMessage(labelKey)).configure(createNewToggleButton());
	}

	public JToggleButton createToggleButton(String[] labelKeys) {
		return (JToggleButton) getButtonLabelInfo(getRequiredMessage(labelKeys)).configure(createNewToggleButton());
	}

	protected AbstractButton createNewToggleButton() {
		return new JToggleButton();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.richclient.factory.ComponentFactory#createRadioButton(java.lang.String)
	 */
	public JRadioButton createRadioButton(String labelKey) {
		return (JRadioButton) getButtonLabelInfo(getRequiredMessage(labelKey)).configure(createNewRadioButton());
	}

	protected JRadioButton createNewRadioButton() {
		return new JRadioButton();
	}

	public JRadioButton createRadioButton(String[] labelKeys) {
		return (JRadioButton) getButtonLabelInfo(getRequiredMessage(labelKeys)).configure(createNewRadioButton());
	}

	public JMenuItem createMenuItem(String labelKey) {
		return (JMenuItem) getButtonLabelInfo(getRequiredMessage(labelKey))
				.configure(getMenuFactory().createMenuItem());
	}

	protected MenuFactory getMenuFactory() {
		return ValkyrieRepository.getInstance().getApplicationConfig().menuFactory();
	}

	public JComponent createLabeledSeparator(String labelKey, Alignment alignment) {
		return com.jgoodies.forms.factories.DefaultComponentFactory.getInstance().createSeparator(
				getRequiredMessage(labelKey), ((Number) alignment.getCode()).intValue());
	}

	public JList createList() {
		return new JList();
	}

	public JComboBox createComboBox() {
		return new JComboBox();
	}

	public JComboBox createListValueModelComboBox(ValueModel selectedItemValueModel,
			ValueModel selectableItemsListHolder, String renderedPropertyPath) {
		return null;
	}

	/**
	 * Returns the default column count for new text fields (including formatted
	 * text and password fields)
	 *
	 * @return the default column count. Must not be lower than 0
	 * @see JTextField
	 */
	public int getTextFieldColumns() {
		return textFieldColumns;
	}

	/**
	 * Defines the default column count for new text fields (including formatted
	 * text and password fields)
	 *
	 * @param columns the default column count. Must not be lower than 0
	 * @see JTextField
	 */
	public void setTextFieldColumns(int columns) {
		if (columns < 0)
			throw new IllegalArgumentException("text field columns must not be lower than 0. Value was: " + columns);
		this.textFieldColumns = columns;
	}

	public JFormattedTextField createFormattedTextField(AbstractFormatterFactory formatterFactory) {
		PatchedJFormattedTextField patchedJFormattedTextField = new PatchedJFormattedTextField(formatterFactory);
		configureTextField(patchedJFormattedTextField);
		return patchedJFormattedTextField;
	}

	public JTextField createTextField() {
		JTextField textField = new JTextField();
		configureTextField(textField);
		return textField;
	}

	/**
	 * Configures the text field.
	 *
	 * @param textField the field to configure. Must not be null
	 */
	protected void configureTextField(JTextField textField) {
		textField.setColumns(getTextFieldColumns());
        //Customize TextField
        textField.setMargin(new Insets(3,3,3,3));
    }

	public JPasswordField createPasswordField() {
		JPasswordField passwordField = new JPasswordField();
		configureTextField(passwordField);
		return passwordField;
	}

	public JTextArea createTextArea() {
		return new JTextArea();
	}

	public JTextArea createTextArea(int rows, int columns) {
		JTextArea textArea = createTextArea();
		textArea.setRows(rows);
		textArea.setColumns(columns);
		return textArea;
	}

	public JTextArea createTextAreaAsLabel() {
		return GuiStandardUtils.textAreaAsLabel(createTextArea());
	}

	public JTabbedPane createTabbedPane() {
		return new JTabbedPane();
	}

	public void addConfiguredTab(JTabbedPane tabbedPane, String labelKey, JComponent tabComponent) {
		LabelInfo info = getLabelInfo(getRequiredMessage(labelKey));
		tabbedPane.addTab(info.getText(), tabComponent);
		int tabIndex = tabbedPane.getTabCount() - 1;
		tabbedPane.setMnemonicAt(tabIndex, info.getMnemonic());
		tabbedPane.setDisplayedMnemonicIndexAt(tabIndex, info.getMnemonicIndex());
		tabbedPane.setIconAt(tabIndex, getIcon(labelKey));
		tabbedPane.setToolTipTextAt(tabIndex, getCaption(labelKey));
	}

	public JScrollPane createScrollPane() {
		return new JScrollPane();
	}

	public JScrollPane createScrollPane(Component view) {
		return new JScrollPane(view);
	}

	public JScrollPane createScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		return new JScrollPane(view, vsbPolicy, hsbPolicy);
	}

	public JPanel createPanel() {
		return new JPanel();
	}

	public JPanel createPanel(LayoutManager layoutManager) {
		return new JPanel(layoutManager);
	}

	private String getCaption(String labelKey) {
		return getOptionalMessage(labelKey + ".caption");
	}

	protected String getOptionalMessage(String messageKey) {
		return getMessages().getMessage(messageKey, (String) null);
	}

	private Icon getIcon(String labelKey) {
		return getIconSource().getIcon(labelKey);
	}

	/**
	 * Returns the icon source.
	 */
	private IconSource getIconSource() {
		return ValkyrieRepository.getInstance().getApplicationConfig().iconSource();
	}

	/**
	 * Construct a JTable with a default model It will delegate the creation to
	 * a TableFactory if it exists.
	 *
	 * @return The new table.
	 */
	public JTable createTable() {
		return (getTableFactory() != null) ? getTableFactory().createTable() : new JTable();
	}

	/**
	 * Construct a JTable with the specified table model. It will delegate the
	 * creation to a TableFactory if it exists.
	 *
	 * @param model the table model
	 * @return The new table.
	 */
	public JTable createTable(TableModel model) {
		return (getTableFactory() != null) ? getTableFactory().createTable(model) : new JTable(model);
	}

	/**
	 * {@inheritDoc}
	 */
	public JComponent createToolBar() {
		JToolBar toolBar = new JToolBar();

		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		return toolBar;
	}
}
