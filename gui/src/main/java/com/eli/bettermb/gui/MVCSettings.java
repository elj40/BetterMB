package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

class SettingsView extends JPanel
{
    JLabel title = new JLabel("Settings");
    JLabel cookiesLabel = new JLabel("Cookies");
    JTextField cookiesInput = new JTextField();
    JButton saveButton = new JButton("SAVE");
    SettingsView()
    {
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createSetting(cookiesLabel, cookiesInput));
        content.add(saveButton);

        add(content, BorderLayout.CENTER);
    }
    JPanel createSetting(JComponent desc, JComponent field)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(desc, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        Dimension maxSize = new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
        panel.setMaximumSize(maxSize);

        return panel;
    }
    void onSaveButtonPressed(ActionListener listener)
    {
        saveButton.addActionListener(listener);
    }
}

class SettingsModel
{
    String cookies = "default=cookies";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    void saveToFile(String filename)
    {
        String json = gson.toJson(this);
        System.out.println(cookies);
        System.out.println(json);
        Path path = Paths.get(filename);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes());
            System.out.println("Settings saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
    void loadFromFile(String filename)
    {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filename)));
            SettingsModel loadedSettings = gson.fromJson(json, SettingsModel.class);

            this.cookies = loadedSettings.cookies;
            System.out.println(this.cookies);

            System.out.println("Settings loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
}
