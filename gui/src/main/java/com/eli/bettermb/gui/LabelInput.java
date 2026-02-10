package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

abstract class LabelInput
    extends JPanel
{
    JLabel label = JDebug.createDebugLabel("");
    JButton button = new JButton();

    boolean shouldListenerIgnore = false;
    abstract String getText();
    abstract void clear();

    LabelInput(String label_text, String button_text)
    {
        setLayout(new BorderLayout());
        label.setText(label_text);
        add(label, BorderLayout.NORTH);

        button.setLabel(button_text);
        add(button, BorderLayout.EAST);
    }
}

// Labeled ComboBox Input
class LabelComboBox extends LabelInput
{
    JComboBox comboBox = new JComboBox();
    LabelComboBox(String label_str, String button_label)
    {
        super(label_str, button_label);
        add(comboBox, BorderLayout.CENTER);
    }
    String getText()
    {
        return (String) comboBox.getSelectedItem();
    }
    void setSelectedIndex(int index)
    {
        comboBox.setSelectedIndex(index);
    }
    public void setItems(String[] items)
    {
        comboBox.removeAllItems();
        addItems(items);
    }
    public void addItems(String[] items)
    {
        for (String s : items) addItem(s);
    }
    public void addItem(String item)
    {
        comboBox.addItem(item);
    }
    public void setEnabled(boolean enabled)
    {
        comboBox.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void clear()
    {
        comboBox.removeAllItems();
    }

    void addActionListener(ActionListener a)
    {
        comboBox.addActionListener(a);
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        comboBox.removeActionListener(a);
        button.removeActionListener(a);
    }
}

class LabelText extends LabelInput
{
    JTextField textField = new JTextField();

    LabelText(String label_str, String button_label)
    {
        super(label_str, button_label);
        add(textField, BorderLayout.CENTER);
    }
    String getText()
    {
        return textField.getText();
    }
    public void setEnabled(boolean enabled)
    {
        textField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void addActionListener(ActionListener a)
    {
        textField.addActionListener(a);
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        textField.removeActionListener(a);
        button.removeActionListener(a);
    }
    void clear()
    {
        textField.setText("");
    }
    void setValue(String value)
    {
        textField.setText(value);
    }
}

// Labeled Formatted Text Input
class LabelFormatText extends LabelInput
{
    JFormattedTextField textField;

    LabelFormatText(String label_str, String button_label, DefaultFormatter textfield_format)
    {
        super(label_str, button_label);
        textField = new JFormattedTextField(textfield_format);
        add(textField, BorderLayout.CENTER);
    }
    String getText()
    {
        return textField.getText();
    }
    public void setEnabled(boolean enabled)
    {
        textField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void addActionListener(ActionListener a)
    {
        textField.addActionListener(a);
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        textField.removeActionListener(a);
        button.removeActionListener(a);
    }
    void clear()
    {
        textField.setValue("");
    }
    void setValue(String value)
    {
        textField.setValue(value);
    }
}

// Labeled Number Spinner Input
class LabelNumberSpinner extends LabelInput
{
    JSpinner numberField;

    LabelNumberSpinner(String label_str, String button_label)
    {
        super(label_str, button_label);
        var spinnerModel = new SpinnerNumberModel(0, 0, 31, 1);
        numberField = new JSpinner(spinnerModel);
        add(numberField, BorderLayout.CENTER);
    }
    String getText()
    {
        return Integer.toString(getValue());
    }
    int getValue()
    {
        return (int) numberField.getValue();
    }
    void setValue(int v)
    {
        numberField.setValue(v);
    }
    void clear()
    {
        numberField.setValue(0);
    }
    public void setEnabled(boolean enabled)
    {
        numberField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
    void setActionCommand(String ac)
    {
        button.setActionCommand(ac);
    }
    void addActionListener(ActionListener a)
    {
        button.addActionListener(a);
    }
    void removeActionListener(ActionListener a)
    {
        button.removeActionListener(a);
    }
}
