package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.view.dialog.SettingsPanel;
import com.ltp.filler.util.WordUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MenuView extends JPanel {

    private final JButton exit = new JButton(LangController.get("menu.exit"));
    private final JButton builder = new JButton(LangController.get("menu.builder"));
    private final JButton filler = new JButton(LangController.get("menu.filler"));

    private JFileChooser fileChooser;

    public MenuView(){
        setMaximumSize(new Dimension(700, 350));
        setMinimumSize(new Dimension(700, 350));
        setPreferredSize(new Dimension(700, 350));

        initLayout();
        initListeners();
    }

    private void initLayout(){
        setLayout(null);

        exit.setBounds(250, 250, 200, 50);
        builder.setBounds(250, 150, 200, 50);
        filler.setBounds(250, 50, 200, 50);
        add(exit);
        add(builder);
        add(filler);
    }

    private void initListeners(){
        exit.addActionListener(e -> Context.exit());

        builder.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Word", "docx", "doc"));
            fileChooser.setDialogTitle("Choose file to create a template");
            fileChooser.setDialogType(JFileChooser.FILES_ONLY);
            if(fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                BuilderView.setText(WordUtils.getText(file));
            }
            Context.getRenderPanel().show(BuilderView.class);
        });
    }

}
