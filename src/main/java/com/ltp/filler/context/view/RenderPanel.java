package com.ltp.filler.context.view;

import javax.swing.*;
import java.awt.*;

import com.ltp.filler.context.Context;
import com.ltp.filler.context.controller.LangController;

public class RenderPanel extends JPanel {

    private final CardLayout layout;

    public RenderPanel(){
        layout = new CardLayout();
        setLayout(layout);

        add(MenuView.class.getName(), new MenuView());
        add(BuilderView.class.getName(), new BuilderView());
        add(FillerView.class.getName(), new FillerView());
    }

    public void show(Class clazz){
        if(Context.getSecurityContext().hasAccess(clazz)) {
            layout.show(this, clazz.getName());
        }else{
            JOptionPane.showMessageDialog(null,
                    LangController.get("security.access.error"),
                    "Access error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
