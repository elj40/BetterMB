package com.eli.bettermb.gui;

import com.eli.bettermb.client.Client;
import com.eli.bettermb.client.HttpClientImpl;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

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

        //Client client = new Client(new StubBookHttpClient());
        //client.setUrlBase("http://127.0.0.0");
        //Client.debugging = true;

        String sun_url = "https://web-apps.sun.ac.za";
        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        MainView view = new MainView();
        MainModel model = new MainModel(client);
        MainController controller = new MainController(view, model);

        frame.add(view);
        frame.setVisible(true);
    }
};
