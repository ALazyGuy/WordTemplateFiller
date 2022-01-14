package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.context.view.dialog.TemplateBuildPanel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class BuilderView extends JScrollPane {

    private static final JTextArea text = new JTextArea();
    private static final List<Template> templates = new LinkedList<>();

    public BuilderView(){
        super(text);

        setFocusable(true);

        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_S && e.isShiftDown() && e.isControlDown()){
                    final int selectionStart = text.getSelectionStart();
                    final int selectionEnd = text.getSelectionEnd();
                    if(selectionEnd == selectionStart || templates.stream()
                            .anyMatch(t -> t.touches(selectionStart, selectionEnd))){
                        return;
                    }
                    JDialog tplBuildDialog = Context.newDialog(LangController.get("builder.tplTitle"),
                            new TemplateBuildPanel(selectionStart, selectionEnd),
                            true);
                    tplBuildDialog.pack();
                    tplBuildDialog.setVisible(true);
                }
            }
        });

        grabFocus();
    }

    public static void setText(String txt){
        text.setText(txt);
    }

    public static void appendTemplate(Template template){
        templates.add(template);
        updateText(template);
    }

    private static void updateText(Template template){
        String pref = text.getText().substring(0, template.getStart());
        String post = text.getText().substring(template.getEnd());
        setText(String.format("%s%s%s", pref, template, post));
    }

}
