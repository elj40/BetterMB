package com.eli.bettermb.gui;

import com.eli.bettermb.client.Client;
import com.eli.bettermb.client.DefaultHttpClient;
import com.eli.bettermb.client.Configuration;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.time.DayOfWeek;

public class GUI
{
    public static void main(String[] args) {
        final int frameSizeFactor = 50;
        JFrame frame = new JFrame("BetterMB-GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(16*frameSizeFactor, 9*frameSizeFactor);

        Client client = new Client();
        client.config = Configuration.devLive;
        client.setHttpClient(new StubBookHttpClient());
        // client.setHttpClient(new DefaultHttpClient());

        MainView view = new MainView();
        MainModel model = new MainModel(client);
        MainController controller = new MainController(view, model);

        frame.add(view);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        controller.start();
    }
};
