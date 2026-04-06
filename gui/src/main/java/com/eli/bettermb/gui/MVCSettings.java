package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

class SettingsView extends JPanel
{
    JLabel title = new JLabel("Settings");

    JLabel cookiesLabel = new JLabel("Cookies");
    JTextField cookiesInput = new JTextField();

    JLabel cachedMealsLabel = new JLabel("Cached Meals File");
    JTextField cachedMealsInput = new JTextField();

    JLabel defaultFacilityLabel = new JLabel("Default Facility");
    JComboBox defaultFacilityInput = new JComboBox();

    JButton saveButton = new JButton("SAVE");
    SettingsView()
    {
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createSetting(cookiesLabel, cookiesInput));
        content.add(createSetting(cachedMealsLabel, cachedMealsInput));
        content.add(createSetting(defaultFacilityLabel, defaultFacilityInput));
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
    void setKnownFacilities(String[] items)
    {
        defaultFacilityInput.removeAllItems();
        for (String s : items) defaultFacilityInput.addItem(s);
    }
    void onSaveButtonPressed(ActionListener listener)
    {
        saveButton.addActionListener(listener);
    }
    void onDefaultFacilitySelected(ActionListener listener)
    {
        defaultFacilityInput.addActionListener(listener);
    }
}

class SettingsModel
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String cookies = "default=cookies";

    final String settingsFilePath = "."+File.separator+"bettermb-settings.json";
    String cachedMealsFilePath = "."+File.separator+"bettermb-meals.json";

    final int expectedFacilityCount = 8;
    HashSet<String> knownFacilities = new HashSet(expectedFacilityCount);
    String defaultFacility = null;

    void saveToFile(String filename)
    {
        String json = gson.toJson(this);
        Path path = Paths.get(filename);
        Path pathAbs = path.toAbsolutePath();
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes());
            System.out.println("Settings saved to " + pathAbs.toString());
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
            this.cachedMealsFilePath = loadedSettings.cachedMealsFilePath;
            this.knownFacilities = loadedSettings.knownFacilities;
            this.defaultFacility = loadedSettings.defaultFacility;

            System.out.println("Settings loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }
    void addDefaultFacilityOptions(String[] options)
    {
        for (String opt: options)
        {
            knownFacilities.add(opt);
        }
    }
}

class SettingsController
{
    SettingsModel model = new SettingsModel();
    SettingsView view = new SettingsView();

    boolean suppressEvents = false;

    SettingsController()
    {
        this.view.onDefaultFacilitySelected(e -> this.onDefaultFacilitySelected());
    }

    void start()
    {
        suppressEvents = true;
        model.loadFromFile(model.settingsFilePath);
        view.onSaveButtonPressed(e -> {
            model.cookies = view.cookiesInput.getText();
            model.cachedMealsFilePath = view.cachedMealsInput.getText();
            model.saveToFile(model.settingsFilePath); });

        view.cookiesInput.setText(model.cookies);
        view.cachedMealsInput.setText(model.cachedMealsFilePath);

        String[] known = model.knownFacilities.toArray(new String[0]);
        view.setKnownFacilities(known);
        for (String fac : known)
        {
            if (model.defaultFacility.equals(fac))
            {
                view.defaultFacilityInput.setSelectedItem(fac);
                break;
            }
        }
        suppressEvents = false;
    }

    void onDefaultFacilitySelected()
    {
        if (suppressEvents) return;
        model.defaultFacility = (String) view.defaultFacilityInput.getSelectedItem();
    }

    void addDefaultFacilityOptions(String[] options)
    {
        suppressEvents = true;
        model.addDefaultFacilityOptions(options);
        view.setKnownFacilities(model.knownFacilities.toArray(new String[0]));
        suppressEvents = false;
    }
}
