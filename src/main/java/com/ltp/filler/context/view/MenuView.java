package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.context.model.TemplateDocument;
import com.ltp.filler.exception.TemplateException;
import com.ltp.filler.util.WordUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MenuView extends JPanel {

    private final JButton exit = new JButton(LangController.get("menu.exit"));
    private final JButton builder = new JButton(LangController.get("menu.builder"));
    private final JButton filler = new JButton(LangController.get("menu.filler"));

    private static JFileChooser fileChooser;

    public MenuView(){
        setMaximumSize(new Dimension(700, 350));
        setMinimumSize(new Dimension(700, 350));
        setPreferredSize(new Dimension(700, 350));

        initLayout();
        initListeners();
    }

    public static void toFiller(){
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word template", "wtpl"));
        fileChooser.setDialogTitle("Choose template to edit");
        fileChooser.setDialogType(JFileChooser.FILES_ONLY);
        if(fileChooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try {
                List<Template> templates = new LinkedList<>();
                String text = WordUtils.readTemplate(file, templates);
                FillerView.setText(text);
                FillerView.setTemplates(templates);
                Context.getRenderPanel().show(FillerView.class);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toBuilderEdit(){
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word template", "wtpl"));
        fileChooser.setDialogTitle("Choose template to edit");
        fileChooser.setDialogType(JFileChooser.FILES_ONLY);
        if(fileChooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try {
                TemplateDocument templateDocument = new TemplateDocument(file);
                BuilderView.setText(WordUtils.getText(templateDocument.getWordFile()));
                BuilderView.setFilePath(templateDocument.getWordFile().getPath());
                templateDocument.getTemplates()
                                .forEach(BuilderView::appendTemplate);
                Context.getRenderPanel().show(BuilderView.class);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toBuilder(){
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word", "docx", "doc"));
        fileChooser.setDialogTitle("Choose file to create a template");
        fileChooser.setDialogType(JFileChooser.FILES_ONLY);
        if(fileChooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            BuilderView.setText(WordUtils.getText(file));
            BuilderView.setFilePath(file.getPath());
            Context.getRenderPanel().show(BuilderView.class);
        }
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
        filler.addActionListener(e -> toFiller());
        builder.addActionListener(e -> toBuilder());
        exit.addActionListener(e -> Context.exit());
    }

}
