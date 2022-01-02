package com.ltp.filler.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Context {

    private static String CONFIG;
    private static Map<String, String> properties = new HashMap<>();
    private static JFrame window;

    public static void init(String CFG){
        CONFIG = CFG;
        loadProperties();
        initWindow();
    }

    private static void initWindow(){
        window = new JFrame(properties.get("frame.name"));
        //Add components here;
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    @SneakyThrows
    private static void loadProperties(){
        new BufferedReader(new FileReader(CONFIG)).lines().filter(l -> !l.isEmpty()).forEach(l -> {
            String[] prop = l.split(":");
            properties.put(prop[0].trim(), prop[1].trim());
        });
    }

}
