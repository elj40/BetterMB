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
        PanelCalendar calendar = new PanelCalendar();
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
        String cmd = e.getActionCommand();
        System.out.println(cmd);
        if (cmd.equals("CANCEL")) setCancelActionArea();
        else if (cmd.equals("BOOK")) setBookActionArea();
        else if (cmd.equals("BACK")) setDefaultActionArea();

        else if (cmd.equals("CANCEL_ID")) cancelIDHandler();
        else if (cmd.equals("BOOK_DATE")) bookDateHandler();
        else if (cmd.equals("BOOK_SLOT")) bookSlotHandler();
        else if (cmd.equals("BOOK_FACL")) bookFaclHandler();
        else if (cmd.equals("BOOK_OPTN")) bookOptnHandler();
        else if (cmd.equals("BOOK_DAYS")) bookDaysHandler();
    }
    void cancelIDHandler()
    {
        Component expectedFC = sidebar.actionsArea.getComponent(0);
        assert(expectedFC instanceof FormCancel);
        FormCancel FC = (FormCancel) expectedFC;

        String id_string = FC.inputID.getText();
        System.out.println("[cancel: STUB] canceling meal with id " + id_string);

        setDefaultActionArea();
    }
    void bookDateHandler()
    {
        Component formExpected = sidebar.actionsArea.getComponent(0);
        assert(formExpected instanceof FormBook);

        FormBook form = (FormBook) formExpected;
        String text = form.dateInput.getText();

        System.out.println("[book: STUB] date received as: " + text);
        String[] stubReceivedSlots = { "Breakfast", "Lunch", "Dinner" };

        form.slotInput.comboBox.removeAllItems();
        form.slotInput.addItems(stubReceivedSlots);
        form.slotInput.setEnabled(true);
    }
    void bookSlotHandler()
    {
        Component formExpected = sidebar.actionsArea.getComponent(0);
        assert(formExpected instanceof FormBook);

        FormBook form = (FormBook) formExpected;

        if (!form.slotInput.comboBox.isEnabled()) return;
        String text = form.slotInput.getText();

        System.out.println("[book: STUB] slot received as: " + text);
        String[] stubReceivedSlots = { "Huis Visser", "Majuba", "Minerva", "Dagbreek" };

        form.faclInput.comboBox.removeAllItems();
        form.faclInput.addItems(stubReceivedSlots);
        form.faclInput.setEnabled(true);
    }
    void bookFaclHandler()
    {
        Component formExpected = sidebar.actionsArea.getComponent(0);
        assert(formExpected instanceof FormBook);

        FormBook form = (FormBook) formExpected;

        if (!form.faclInput.comboBox.isEnabled()) return;
        String text = form.faclInput.getText();

        System.out.println("[book: STUB] facl received as: " + text);
        String[] stubReceivedSlots = { "Standard Meal", "Extra Protein", "Halaal", "Vegetarian" };

        form.optnInput.comboBox.removeAllItems();
        form.optnInput.addItems(stubReceivedSlots);
        form.optnInput.setEnabled(true);
    }
    void bookOptnHandler()
    {
        Component formExpected = sidebar.actionsArea.getComponent(0);
        assert(formExpected instanceof FormBook);

        FormBook form = (FormBook) formExpected;

        if (!form.optnInput.comboBox.isEnabled()) return;
        String text = form.optnInput.getText();

        System.out.println("[book: STUB] optn received as: " + text);
        form.daysInput.setEnabled(true);
    }
    void bookDaysHandler()
    {
    }
    void setCancelActionArea()
    {
        JPanel form = sidebar.actionsArea;
        form.removeAll();

        FormCancel FC = new FormCancel();
        FC.addActionListener(this);
        form.add(FC);

        form.revalidate();
        form.repaint();
    }
    void setBookActionArea()
    {
        JPanel form = sidebar.actionsArea;
        form.removeAll();

        FormBook FB = new FormBook();
        FB.addActionListener(this);
        form.add(FB);

        form.revalidate();
        form.repaint();
    };
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
