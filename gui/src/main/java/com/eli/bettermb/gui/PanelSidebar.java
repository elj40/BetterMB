package com.eli.bettermb.gui;

import javax.swing.*;
import java.awt.*;

class PanelSidebar extends JPanel
{
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

            JPanel menuArea = JDebug.createDebugPanel();
            {
                menuArea.setLayout(new BoxLayout(menuArea, BoxLayout.Y_AXIS));
                menuArea.add(new Button("BOOK"));
                menuArea.add(new Button("CANCEL"));
            }
            add(menuArea, BorderLayout.SOUTH);
        }
}
