package com.ltp.filler.context.controller;

import com.ltp.filler.context.Context;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class LangController {

    private static Map<String, String> strings = new HashMap<>();

    @SneakyThrows
    public static void init(){
        String file = String.format("%s.lang", Context.getProperty("frame.lang"));
        new BufferedReader(new FileReader(file)).lines().filter(l -> !l.isEmpty()).forEach(l -> {
            String[] prop = l.split(":");
            strings.put(prop[0].trim(), prop[1].trim());
        });
    }

    public static String get(String key){
        return strings.getOrDefault(key, "");
    }

}
