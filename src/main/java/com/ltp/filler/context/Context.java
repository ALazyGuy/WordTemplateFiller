package com.ltp.filler.context;

import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.view.MenuView;
import com.ltp.filler.context.view.RenderPanel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Context {

    private static String CONFIG;
    private static Map<String, String> properties = new HashMap<>();
    private static JFrame window;
    private static RenderPanel renderPanel;

    public static void init(String CFG){
        CONFIG = CFG;
        loadProperties();
        LangController.init();
        initWindow();
    }

    public static String getProperty(String name){
        return properties.getOrDefault(name, "");
    }

    public static void exit(){
        saveEnv();
        window.dispose();
    }

    public static RenderPanel getRenderPanel(){
        return renderPanel;
    }

    @SneakyThrows
    private static void saveEnv(){
        String env = properties
                .entrySet()
                .stream()
                .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(System.lineSeparator()));
        Files.write(Paths.get(CONFIG), env.getBytes(StandardCharsets.UTF_8));
    }

    public static JDialog newDialog(String title, JPanel panel, boolean modal){
        JDialog dialog = new JDialog(window, title, modal);
        dialog.add(panel);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return dialog;
    }

    private static void initWindow(){
        renderPanel = new RenderPanel();
        window = new JFrame(properties.get("frame.name"));
        window.add(renderPanel);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveEnv();
            }
        });
    }


    @SneakyThrows
    private static void loadProperties(){
        new BufferedReader(new FileReader(CONFIG)).lines().filter(l -> !l.isEmpty()).forEach(l -> {
            String[] prop = l.split(":");
            properties.put(prop[0].trim(), prop[1].trim());
        });
    }

}
