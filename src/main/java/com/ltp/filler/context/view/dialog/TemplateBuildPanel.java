package com.ltp.filler.context.view.dialog;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.context.view.BuilderView;

import javax.swing.*;
import java.awt.*;

public class TemplateBuildPanel extends JPanel {

    private static final int WIDTH = 450;
    private static final int HEIGHT = 375;

    private final JLabel tplNameLabel = new JLabel(LangController.get("builder.tplNameLabel"));
    private final JLabel tplDescriptionLabel = new JLabel(LangController.get("builder.tplDescriptionLabel"));
    private final JLabel tplSizeLabel = new JLabel();

    private final JTextField tplName = new JTextField();
    private final JTextField tplDescription = new JTextField();
    private final JTextField tplSize = new JTextField();

    private final JButton apply = new JButton(LangController.get("builder.apply"));
    private final JButton cancel = new JButton(LangController.get("builder.cancel"));

    private final int start;
    private final int end;

    public TemplateBuildPanel(int start, int end){
        this.start = start;
        this.end = end;

        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        tplNameLabel.setBounds(30, 30, WIDTH - 60, 30);
        tplName.setBounds(30, 60, WIDTH - 60, 30);

        tplDescriptionLabel.setBounds(30, 120, WIDTH - 60, 30);
        tplDescription.setBounds(30, 150, WIDTH - 60, 30);

        tplSizeLabel.setText(String.format("%s: %d", LangController.get("builder.tplSizeLabel"), end - start));
        tplSizeLabel.setBounds(30, 210, WIDTH - 60, 30);
        tplSize.setText(Integer.toString(end - start));
        tplSize.setBounds(30, 240, WIDTH - 60, 30);

        cancel.setBounds(30, HEIGHT - 45, 100, 30);
        apply.setBounds(WIDTH - 130, HEIGHT - 45, 100, 30);

        setLayout(null);
        add(tplNameLabel);
        add(tplName);
        add(tplDescriptionLabel);
        add(tplDescription);
        add(tplSizeLabel);
        add(tplSize);
        add(cancel);
        add(apply);

        registerListeners();
    }

    private void registerListeners(){
        cancel.addActionListener(e -> Context.getModal().dispose());
        apply.addActionListener(e -> {
            if(!tplName.getText().trim().isEmpty() &&
                    !tplDescription.getText().trim().isEmpty() &&
                    !tplSize.getText().trim().isEmpty()) {
                int sizes = BuilderView.getTemplates()
                        .stream()
                        .filter(t -> t.getEnd() < start)
                        .mapToInt(t -> t.toString().length())
                        .sum();
                int actualSizes = BuilderView.getTemplates()
                        .stream()
                        .filter(t -> t.getEnd() < start)
                        .mapToInt(Template::getSize)
                        .sum();
                Template template = new Template(
                        tplName.getText().trim(),
                        tplDescription.getText().trim(),
                        start,
                        end,
                        Math.max(end - start, Integer.parseInt(tplSize.getText().trim())),
                        end - start,
                        start - sizes + actualSizes,
                        end - sizes + actualSizes);

                System.out.println(start + " " + end + " " + (start - sizes + actualSizes) + " " + (end - sizes + actualSizes));

                BuilderView.appendTemplate(template);
                Context.getModal().dispose();
            }
        });
    }

}
