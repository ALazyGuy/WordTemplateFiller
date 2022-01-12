package com.ltp.filler.context.view;

import javax.swing.*;

public class BuilderView extends JScrollPane {

    private static final JTextArea text = new JTextArea();

    public BuilderView(){
        super(text);
    }

    public static void setText(String txt){
        text.setText(txt);
    }
}
