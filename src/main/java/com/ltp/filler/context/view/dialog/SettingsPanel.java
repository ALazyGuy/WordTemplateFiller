package com.ltp.filler.context.view.dialog;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsPanel extends JPanel {

    private static final int WIDTH = 380;
    private static final int HEIGHT = 150;

    private final JComboBox languages = new JComboBox();
    private final JLabel languagesLabel = new JLabel(LangController.get("settings.langLabel") + ":");
    private final JButton accept = new JButton(LangController.get("settings.accept"));
    private final JButton cancel = new JButton(LangController.get("settings.cancel"));

    private final Map<String, String> langs = new HashMap<>();

    public SettingsPanel(){
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        for(Map.Entry entry: LangController.getGroup("settings.lang")){
            String key = (String)entry.getKey();
            String langAbbr = key.substring(key.lastIndexOf('.') + 1);
            langs.put((String)entry.getValue(), langAbbr);
            languages.addItem(entry.getValue());
            if(Context.getProperty("frame.lang").equals(langAbbr)){
                languages.setSelectedItem(entry.getValue());
            }
        }

        setLayout(null);
        languages.setBounds(220, 42, 130, 30);
        languagesLabel.setBounds(60, 30, 130, 50);

        cancel.setBounds(30, HEIGHT - 50, 130, 30);
        accept.setBounds(WIDTH - 160, HEIGHT - 50, 130, 30);

        registerListeners();

        add(languages);
        add(languagesLabel);
        add(cancel);
        add(accept);
    }

    private void registerListeners(){
        cancel.addActionListener(e -> Context.getModal().dispose());
        accept.addActionListener(e -> {
            Context.setProperty("frame.lang", langs.get(languages.getSelectedItem()));
            int i = JOptionPane.showConfirmDialog(this, LangController.get("settings.restart"));
            if(i == JOptionPane.YES_OPTION){
                Context.restartApplication();
            }
            Context.getModal().dispose();
        });
    }

}
