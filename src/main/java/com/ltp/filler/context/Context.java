package com.ltp.filler.context;

import com.ltp.filler.Start;
import com.ltp.filler.context.controller.LangController;
import com.ltp.filler.context.view.MenuView;
import com.ltp.filler.context.view.RenderPanel;
import com.ltp.filler.context.view.dialog.SettingsPanel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Context {

    private static String CONFIG;
    private static Map<String, String> properties = new HashMap<>();
    private static JFrame window;
    private static RenderPanel renderPanel;
    private static JDialog modalDialog;

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

    public static void setProperty(String name, String value){
        properties.put(name, value);
    }

    @SneakyThrows
    public static void restartApplication()
    {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        if(!currentJar.getName().endsWith(".jar"))
            return;

        final List<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        exit();
        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
    }

    public static JDialog newDialog(String title, JPanel panel, boolean modal){
        JDialog dialog = new JDialog(window, title, modal);
        dialog.add(panel);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        modalDialog = dialog;
        return dialog;
    }

    public static JDialog getModal(){
        return modalDialog;
    }

    private static void initWindow(){
        renderPanel = new RenderPanel();
        window = new JFrame(properties.get("frame.name"));
        window.setJMenuBar(buildMenuBar());
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

    private static JMenuBar buildMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu(LangController.get("menuBar.file"));
        JMenuItem settings = new JMenuItem(LangController.get("menuBar.file.settings"));
        JMenuItem exit = new JMenuItem(LangController.get("menuBar.file.exit"));

        file.add(settings);
        file.add(exit);

        menuBar.add(file);

        settings.addActionListener(e -> {
            JDialog settingsDialog = newDialog("Settings", new SettingsPanel(), true);
            settingsDialog.pack();
            settingsDialog.setVisible(true);
        });

        exit.addActionListener(e -> exit());

        return menuBar;
    }

    @SneakyThrows
    private static void loadProperties(){
        new BufferedReader(new FileReader(CONFIG)).lines().filter(l -> !l.isEmpty()).forEach(l -> {
            String[] prop = l.split(":");
            properties.put(prop[0].trim(), prop[1].trim());
        });
    }

}
