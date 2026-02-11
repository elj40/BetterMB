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
    static String sun_url = "https://web-apps.sun.ac.za";

    public static void main(String[] args) {
        final int frameSizeFactor = 50;
        JFrame frame = new JFrame("BetterMB-GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16*frameSizeFactor, 9*frameSizeFactor);

        Client.debugging = true;

        //Client client = new Client(new StubBookHttpClient());
        //client.setUrlBase("http://127.0.0.0");

        Client client = new Client(new HttpClientImpl());
        client.setUrlBase(sun_url);

        MainView view = new MainView();
        MainModel model = new MainModel(client);
        MainController controller = new MainController(view, model);

        frame.add(view);
        frame.setVisible(true);
    }
};
