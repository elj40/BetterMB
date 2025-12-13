package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

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
