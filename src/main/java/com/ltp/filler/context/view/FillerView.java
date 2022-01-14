package com.ltp.filler.context.view;

import com.ltp.filler.context.model.Template;
import lombok.Setter;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class FillerView extends JScrollPane {

    private static final JTextArea text = new JTextArea();
    private static List<Template> templates = new LinkedList<>();

    public FillerView() {
        super(text);
        setFocusable(true);
        text.setEditable(false);
        grabFocus();
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
