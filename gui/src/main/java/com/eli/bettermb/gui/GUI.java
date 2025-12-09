package com.eli.bettermb.gui;

import com.eli.bettermb.client.Client;
import com.eli.bettermb.client.HttpClientImpl;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.time.DayOfWeek;

public class GUI
    implements ActionListener
{
    Client client;
    GuiUser user;

    static String sun_url = "https://web-apps.sun.ac.za";

    PanelSidebar sidebar;
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
        sidebar = new PanelSidebar(calendar, content);
        sidebar.addListener(this);

        content.add(calendar, BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(content, BorderLayout.CENTER);

        setDefaultActionArea();

        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        System.out.println(e.paramString());
        String cmd = e.getActionCommand();
        if (cmd.equals("CANCEL")) setCancelActionArea();
        else if (cmd.equals("BOOK")) setBookActionArea();
        else if (cmd.equals("BACK")) setDefaultActionArea();

        else if (cmd.equals("CANCEL_ID")) cancelMeal(source);
    }
    void cancelMeal(Object src)
    {
        assert(src instanceof TextFieldWithButton);
        var tfwb = (TextFieldWithButton) src;

        String id_string = tfwb.textField.getText();
        System.out.println("[cancel: STUB] canceling meal with id " + id_string);
    }
    JPanel createTextFieldWithButton(String tf_text, String btn_label, String ac, ActionListener al)
    {
        JPanel p = JDebug.createDebugPanel();
        p.setLayout(new BorderLayout());

        JTextField tf = new JTextField(tf_text);
        tf.addActionListener(al);
        tf.setActionCommand(ac);
        p.add(tf, BorderLayout.CENTER);

        Button b = new Button(btn_label);
        b.addActionListener(al);
        b.setActionCommand(ac);
        p.add(b, BorderLayout.EAST);

        return p;
    }
    void setCancelActionArea()
    {
        JPanel form = sidebar.actionsArea;
        form.setLayout(new GridLayout(0,1));
        form.removeAll();

        form.add(JDebug.createDebugLabel("[Cancel] Enter meal ID:"));
        form.add(new TextFieldWithButton("", ">", "CANCEL_ID", this));
        form.add(new ButtonCommand("Back", "BACK", this));

        form.revalidate();
        form.repaint();
    }
    void setBookActionArea() {};
    void setDefaultActionArea()
    {
        JPanel form = sidebar.actionsArea;
        form.removeAll();

        form.add(new ButtonCommand("Cancel", "CANCEL", this));
        form.add(new ButtonCommand("Book", "BOOK", this));

        form.revalidate();
        form.repaint();
    };
};
