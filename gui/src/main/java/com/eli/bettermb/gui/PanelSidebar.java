package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

class PanelSidebar extends JPanel implements ActionListener
{
    List<ActionListener> listeners = new ArrayList<>();
    JPanel actionsArea;
    PanelSidebar(JPanel calendar, JPanel content) {
        setLayout(new BorderLayout());
        JDebug.addDebugFeatures(this);
        JPanel pages = JDebug.createDebugPanel();
        {
            pages.setLayout(new BoxLayout(pages, BoxLayout.Y_AXIS));
            pages.add(new PageButton("HOME", calendar, content));
            pages.add(new PageButton("ABOUT", new JPanel(), content));
        }
        add(pages, BorderLayout.NORTH);

        actionsArea = JDebug.createDebugPanel();
        {
            actionsArea.setLayout(new BoxLayout(actionsArea, BoxLayout.Y_AXIS));
            actionsArea.add(new ButtonBook("BOOK MEAL", actionsArea));

            Button buttonCancel = new Button("CANCEL MEAL");
            buttonCancel.setActionCommand("CANCEL");
            buttonCancel.addActionListener(this);

            actionsArea.add(buttonCancel);
        }
        add(actionsArea, BorderLayout.SOUTH);
    }
    public void addListener(ActionListener a)
    {
        listeners.add(a);
    }
    public void actionPerformed(ActionEvent e)
    {
        for (ActionListener l : listeners) l.actionPerformed(e);
    }
}
