package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;

class PanelHeader extends JPanel
{
    PanelHeader(String title, String username)
    {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        JDebug.addDebugFeatures(titleLabel);
        titleLabel.setBackground(Color.BLUE);

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setBackground(Color.RED);
        JDebug.addDebugFeatures(usernameLabel);

        add(titleLabel, BorderLayout.CENTER);
        add(usernameLabel, BorderLayout.EAST);
    }
};
