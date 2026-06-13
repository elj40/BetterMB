package com.eli.bettermb.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

class SettingsView extends JPanel
{
    final int textFieldColumns = 64;

    JLabel title = new JLabel("Settings");

    JLabel showTutorialLabel = new JLabel("Display help message on startup");
    JCheckBox showTutorialCheckBox = new JCheckBox();

    JLabel cookiesLabel = new JLabel("Cookies");
    JTextField cookiesInput = new JTextField();

    JLabel cachedMealsLabel = new JLabel("Cached Meals File");
    JTextField cachedMealsInput = new JTextField();

    JLabel defaultFacilityLabel = new JLabel("Default Facility");
    JComboBox defaultFacilityInput = new JComboBox();

    final AbstractButton[] buttons = {
        showTutorialCheckBox,
    };
    final JTextField[] textfields = {
        cookiesInput,
        cachedMealsInput,
    };
    final JComboBox[] comboBoxes = {
        defaultFacilityInput,
    };
    final JComponent[] inputs = {
        showTutorialCheckBox,
        cookiesInput,
        cachedMealsInput,
        defaultFacilityInput,
    };

    JButton saveButton = new JButton("SAVE");
    SettingsView()
    {
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        GroupLayout layout = new GroupLayout(content);
        content.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        cookiesInput.setColumns(textFieldColumns);
        cachedMealsInput.setColumns(textFieldColumns);

        var horizontalGroup = layout.createSequentialGroup();
        horizontalGroup.addGroup( layout.createParallelGroup()
                .addComponent(showTutorialLabel)
                .addComponent(defaultFacilityLabel)
                .addComponent(cachedMealsLabel)
                .addComponent(cookiesLabel));
        horizontalGroup.addGroup( layout.createParallelGroup()
                .addComponent(showTutorialCheckBox)
                .addComponent(defaultFacilityInput)
                .addComponent(cookiesInput)
                .addComponent(cachedMealsInput)
                .addComponent(saveButton));
        layout.setHorizontalGroup(horizontalGroup);

        var verticalGroup = layout.createSequentialGroup();
        verticalGroup.addGroup( layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(showTutorialLabel)
                .addComponent(showTutorialCheckBox));
        verticalGroup.addGroup( layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(defaultFacilityLabel)
                .addComponent(defaultFacilityInput));
        verticalGroup.addGroup( layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cachedMealsLabel)
                .addComponent(cachedMealsInput));
        verticalGroup.addGroup( layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cookiesLabel)
                .addComponent(cookiesInput));
        verticalGroup.addGroup( layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(saveButton));
        layout.setVerticalGroup(verticalGroup);

        // content.add(createSetting(showTutorialLabel, showTutorialCheckBox));
        // content.add(createSetting(cookiesLabel, cookiesInput));
        // content.add(createSetting(cachedMealsLabel, cachedMealsInput));
        // content.add(createSetting(defaultFacilityLabel, defaultFacilityInput));

        var scrollPane = new JScrollPane(content);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
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
    final String settingsFilePath = "."+File.separator+"bettermb-settings.json";

    boolean showTutorial = true;
    String cookies = "default=cookies";
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

            this.showTutorial = loadedSettings.showTutorial;
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

    void start()
    {
        suppressEvents = true;
        model.loadFromFile(model.settingsFilePath);
        view.onSaveButtonPressed(e -> {
            model.showTutorial = view.showTutorialCheckBox.isSelected();
            model.cookies = view.cookiesInput.getText();
            model.cachedMealsFilePath = view.cachedMealsInput.getText();
            model.saveToFile(model.settingsFilePath); });

        this.view.onDefaultFacilitySelected(e -> this.onDefaultFacilitySelected());

        for (JComponent c : view.inputs)
        {
            if (c instanceof JTextField input)
                input.addActionListener(e -> saveSettings());
            if (c instanceof AbstractButton input)
                input.addActionListener(e -> saveSettings());
            if (c instanceof JComboBox input)
                input.addActionListener(e -> saveSettings());
        }


        view.showTutorialCheckBox.setSelected(model.showTutorial);
        view.cachedMealsInput.setText(model.cachedMealsFilePath);
        view.cookiesInput.setText(model.cookies);
        view.cachedMealsInput.setText(model.cachedMealsFilePath);

        String[] known = model.knownFacilities.toArray(new String[0]);
        view.setKnownFacilities(known);
        // NOTE: the commented out section does not ensure that the option is available,
        // this is undefined as far as I know, but it would be cleaner
        // view.defaultFacilityInput.setSelectedItem(model.defaultFacility);
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

    void saveSettings()
    {
        // TODO: consider if there are consequences to saving as soon as input
        // field changes, will we have to do some janky suppressEvents stuff?
        // System.out.println("saveSettings");
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
