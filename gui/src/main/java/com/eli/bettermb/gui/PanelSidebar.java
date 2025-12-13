package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

import javax.swing.text.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class SidebarView extends JPanel
{
    JPanel actionsArea  = new JPanel();
    JButton aboutButton = new JButton("ABOUT");
    JButton homeButton  = new JButton("HOME");
    SidebarView() {

        setLayout(new BorderLayout());
        JDebug.addDebugFeatures(this);
        JPanel pages = JDebug.createDebugPanel();
        {
            pages.setLayout(new GridLayout(0, 1));
            pages.add(homeButton);
            pages.add(aboutButton);
        }
        add(pages, BorderLayout.NORTH);

        add(actionsArea, BorderLayout.SOUTH);
    }
    void setActionsArea(JPanel actionsAreaView)
    {
        actionsArea.removeAll();
        actionsArea.add(actionsAreaView);
        actionsArea.revalidate();
        actionsArea.repaint();
    };
    void onGoToAbout(ActionListener listener)
    {
        aboutButton.addActionListener(listener);
    }
    void onGoToHome(ActionListener listener)
    {
        homeButton.addActionListener(listener);
    }
}

class PanelSidebar extends JPanel implements ActionListener
{
    List<ActionListener> listeners = new ArrayList<>();
    JPanel actionsArea;
    JButton aboutButton = new JButton("ABOUT");
    JButton homeButton  = new JButton("HOME");
    PanelSidebar(JPanel calendar, JPanel content) {
        setLayout(new BorderLayout());
        JDebug.addDebugFeatures(this);
        JPanel pages = JDebug.createDebugPanel();
        {
            pages.setLayout(new GridLayout(0, 1));
            pages.add(homeButton);
            pages.add(aboutButton);
        }
        add(pages, BorderLayout.NORTH);

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
    void onGoToAbout(ActionListener listener)
    {
        aboutButton.addActionListener(listener);
    }
    void onGoToHome(ActionListener listener)
    {
        homeButton.addActionListener(listener);
    }
}
