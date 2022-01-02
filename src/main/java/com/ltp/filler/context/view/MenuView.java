package com.ltp.filler.context.view;

import com.ltp.filler.context.Context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuView extends JPanel {

    private final JButton exit = new JButton("Exit");

    public MenuView(){
        setMaximumSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));

        initLayout();
        initListeners();
    }

    private void initLayout(){
        setLayout(null);

        exit.setBounds(300, 450, 200, 50);
        add(exit);
    }

    private void initListeners(){
        exit.addActionListener(e -> Context.exit());
    }

}
