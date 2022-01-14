package com.ltp.filler.context.controller;

import com.ltp.filler.context.Context;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static List<Map.Entry<String, String>> getGroup(String groupName){
        return strings.entrySet()
                .stream()
                .filter(v -> Pattern.matches(String.format("(^%s\\.[a-zA-Z]*$)", groupName), v.getKey()))
                .collect(Collectors.toList());
    }

}
