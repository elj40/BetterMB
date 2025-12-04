package com.eli.bettermb.gui;

import com.eli.bettermb.client.Client;
import com.eli.bettermb.client.HttpClientImpl;

import javax.swing.*;
import java.awt.*;

import java.time.DayOfWeek;

public class GUI
{
    Client client;
    GuiUser user;

    static String sun_url = "https://web-apps.sun.ac.za";

    public static void main(String[] args)
    {
        boolean shouldDebug = false;
        Client.debugging = shouldDebug;
        GuiUser.debugging = shouldDebug;


        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        System.out.println("Hello GUI!");
        GUI gui = new GUI(client);
        gui.main();
    };
    GUI(Client _client)
    {
        client = _client;
        user = new GuiUser();
    }
    public void main() {
        final int frameSizeFactor = 50;
        JFrame frame = new JFrame("BetterMB-GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16*frameSizeFactor, 9*frameSizeFactor);
        frame.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        JPanel header = new PanelHeader("BetterMB-GUI", "28178564");
        JPanel calendar = new PanelCalendar();
        JPanel sidebar = new PanelSidebar(calendar, content);

        content.add(calendar, BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(content, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    void addDebugFeatures(JComponent c)
    {
        c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.setOpaque(true);
    }
};
