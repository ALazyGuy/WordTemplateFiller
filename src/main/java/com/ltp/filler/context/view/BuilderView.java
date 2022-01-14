package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.context.view.dialog.TemplateBuildPanel;
import com.ltp.filler.util.WordUtils;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderView extends JScrollPane {

    private static final JTextArea text = new JTextArea();
    private static List<Template> templates = new LinkedList<>();
    private static String savedText;
    @Setter
    private static String filePath;

    public BuilderView(){
        super(text);

        setFocusable(true);

        text.setEditable(false);

        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if(e.isShiftDown() && e.isControlDown()) {
                    if (key == KeyEvent.VK_T) {
                        final int selectionStart = text.getSelectionStart();
                        final int selectionEnd = text.getSelectionEnd();
                        if (selectionEnd == selectionStart || templates.stream()
                                .anyMatch(t -> t.touches(selectionStart, selectionEnd))) {
                            return;
                        }
                        JDialog tplBuildDialog = Context.newDialog(LangController.get("builder.tplTitle"),
                                new TemplateBuildPanel(selectionStart, selectionEnd),
                                true);
                        tplBuildDialog.pack();
                        tplBuildDialog.setVisible(true);
                    }else if(key == KeyEvent.VK_B){
                        if(templates.isEmpty()){
                            JOptionPane.showMessageDialog(
                                    null,
                                    LangController.get("builder.fail"),
                                    LangController.get("builder.built"),
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        WordUtils.saveTemplate(filePath, filePath.substring(0, filePath.lastIndexOf('.')) + ".wtpl", templates);
                        JOptionPane.showMessageDialog(
                                null,
                                LangController.get("builder.success"),
                                LangController.get("builder.built"),
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        grabFocus();
    }

    public static void setText(String txt){
        text.setText(txt);
        savedText = txt;
    }

    public static void appendTemplate(Template template){
        templates.add(template);
        templates = templates.stream()
                        .sorted(Comparator.comparingInt(Template::getStart))
                                .collect(Collectors.toList());

        int index = templates.indexOf(template);

        templates.stream()
                .skip(index + 1)
                .forEach(t -> t.increaseSize(template));

        updateText(template);
    }

    private static void updateText(Template template){
        String pref = text.getText().substring(0, template.getStart());
        String post = text.getText().substring(template.getEnd());
        setText(String.format("%s%s%s", pref, template, post));
    }

}
