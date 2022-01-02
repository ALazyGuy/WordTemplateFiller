package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JPanel {

    private final JButton exit = new JButton(LangController.get("menu.exit"));
    private final JButton builder = new JButton(LangController.get("menu.builder"));
    private final JButton filler = new JButton(LangController.get("menu.filler"));

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
    }

}
