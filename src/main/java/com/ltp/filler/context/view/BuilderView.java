package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.context.model.TemplateAction;
import com.ltp.filler.context.view.dialog.TemplateBuildPanel;
import com.ltp.filler.util.WordUtils;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class BuilderView extends JScrollPane {

    private static final JTextArea text = new JTextArea();
    @Getter
    private static List<Template> templates = new LinkedList<>();
    private static Stack<Template> templatesToRecreate = new Stack<>();
    private static List<TemplateAction> saves = new LinkedList<>();
    private static int current = -1;
    @Setter
    private static String filePath;

    public BuilderView() {
        super(text);
        registerListeners();
        setFocusable(true);
        text.setEditable(false);
        grabFocus();
    }

    public static void setText(String txt) {
        text.setText(txt);
    }

    public static void appendTemplate(Template template){
        appendTemplate(template, true);
    }

    public static void appendTemplate(Template template, boolean isNew) {
        templates.add(template);
        templates = templates.stream()
                .sorted(Comparator.comparingInt(Template::getStart))
                .collect(Collectors.toList());

        int index = templates.indexOf(template);

        templates.stream()
                .skip(index + 1)
                .forEach(t -> t.increaseSize(template));

        if(isNew){
            registerState(template);
        }
        updateText(template);
    }

    private static void registerState(Template template) {
        if (current != saves.size() - 1) {
            saves = saves.stream().limit(current + 1).collect(Collectors.toList());
            templatesToRecreate.clear();
        }
        TemplateAction action = new TemplateAction(template, text.getText());
        current++;
        saves.add(action);
    }

    private static void updateText(Template template) {
        String pref = text.getText().substring(0, template.getStart());
        String post = text.getText().substring(template.getEnd());
        setText(String.format("%s%s%s", pref, template, post));
    }

    private void registerListeners(){
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();

                if (e.isControlDown() && key == KeyEvent.VK_Z) {
                    if(e.isShiftDown()){
                        if(!templatesToRecreate.empty()){
                            Template template = templatesToRecreate.pop();
                            appendTemplate(template, false);
                            current++;
                        }
                    }else if(current >= 0){
                        text.setText(saves.get(current).getText());
                        templates.remove(saves.get(current).getTemplate());
                        templatesToRecreate.push(saves.get(current).getTemplate());
                        current--;
                    }
                }

                if (e.isShiftDown() && e.isControlDown()) {
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
                    } else if (key == KeyEvent.VK_B) {
                        if (templates.isEmpty()) {
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
    }

}
