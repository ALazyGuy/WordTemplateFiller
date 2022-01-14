package com.ltp.filler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltp.filler.context.model.Template;
import com.ltp.filler.exception.TemplateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordUtils {

    public static String getText(File file){
        try(XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SneakyThrows
    public static void saveTemplate(String filePath, String tpl, List<Template> templates){
        try(FileWriter fileWriter = new FileWriter(tpl)){
            fileWriter.write(filePath + System.lineSeparator());
            ObjectMapper mapper = new ObjectMapper();
            templates.stream()
                    .map(t -> {
                        try {
                            return mapper.writeValueAsString(t);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(str -> {
                        try {
                            fileWriter.write(str + System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public static String readTemplate(File template, List<Template> templates) throws TemplateException {
        try {
            List<String> lines = Files.lines(template.toPath()).collect(Collectors.toList());
            String wordFilePath = lines.get(0);
            if(Files.notExists(Paths.get(wordFilePath))){
                throw new TemplateException(String.format("No word file found `%s`!", wordFilePath));
            }
            File wordFile = new File(wordFilePath);

            String text = getText(wordFile);

            ObjectMapper mapper = new ObjectMapper();

            for(String line : lines){
                if(!line.startsWith("{")){
                    continue;
                }

                templates.add(mapper.readValue(line, Template.class));
            }

            return text;
        } catch (IOException e) {
            throw new TemplateException(String.format("No template file found `%s`!", template.getPath()));
        }
    }
}
