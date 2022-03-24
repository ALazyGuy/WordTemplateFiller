package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.model.DocumentData;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.exception.TemplateException;
import com.ltp.filler.util.ExcelUtils;
import com.ltp.filler.util.WordUtils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FillerView extends JScrollPane {

    @Getter
    @Setter
    private static File templateDocument;
    private static final JTextArea text = new JTextArea();
    private static List<Template> templates = new LinkedList<>();

    private JFileChooser fileChooser;

    public FillerView() {
        super(text);
        setFocusable(true);
        text.setEditable(false);
        grabFocus();

        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()){
                    fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Excel", "xlsx"));
                    fileChooser.setDialogTitle("Choose Excel data table");
                    fileChooser.setDialogType(JFileChooser.FILES_ONLY);
                    if(fileChooser.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION){
                        File file = fileChooser.getSelectedFile();
                        List<DocumentData> data = ExcelUtils.readExcel(file);
                        final String dir = file.getParent() + "\\results\\";
                        File dirFile = new File(dir);
                        dirFile.mkdir();
                        for (DocumentData dataRow: data) {
                            WordUtils.generateFile(dataRow, templates, dirFile);
                        }
                    }
                }
            }
        });
    }

    public static void setTemplates(List<Template> templates1){
        templates = templates1;
        templates.forEach(FillerView::executeTemplate);
    }

    public static void setText(String txt){
        text.setText(txt);
    }

    private static void executeTemplate(Template template){
        String pref = text.getText().substring(0, template.getActualStart());
        String post = text.getText().substring(template.getActualEnd());
        setText(String.format("%s%s%s", pref, "_".repeat(template.getSize()), post));
    }

}
