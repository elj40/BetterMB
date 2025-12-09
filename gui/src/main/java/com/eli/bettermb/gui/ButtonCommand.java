package com.eli.bettermb.gui;

import java.awt.*;
import java.awt.event.*;

class ButtonCommand extends Button
{
    ButtonCommand(String label, String command, ActionListener al)
    {
        super(label);
        setActionCommand(command);
        addActionListener(al);
    }
}
