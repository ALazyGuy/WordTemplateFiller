package com.ltp.filler.context.view;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {

    private final CardLayout layout;

    public RenderPanel(){
        layout = new CardLayout();
        setLayout(layout);

        add(MenuView.class.getName(), new MenuView());
        add(BuilderView.class.getName(), new BuilderView());
    }

    public void show(Class clazz){
        layout.show(this, clazz.getName());
    }

}
