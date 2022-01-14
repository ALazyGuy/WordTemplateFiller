package com.ltp.filler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltp.filler.context.model.Template;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
}
