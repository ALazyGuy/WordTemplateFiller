package com.ltp.filler.context.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltp.filler.exception.TemplateException;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TemplateDocument {

    private File wordFile;
    private List<Template> templates;

    public TemplateDocument(File templateFile) throws TemplateException {
        try {
            List<String> lines = Files.lines(templateFile.toPath()).collect(Collectors.toList());
            String wordFilePath = lines.get(0);
            if(Files.notExists(Paths.get(wordFilePath))){
                throw new TemplateException(String.format("No word file found `%s`!", wordFilePath));
            }
            wordFile = new File(wordFilePath);

            ObjectMapper mapper = new ObjectMapper();

            templates = new LinkedList<>();

            for(String line : lines){
                if(!line.startsWith("{")){
                    continue;
                }

                templates.add(mapper.readValue(line, Template.class));
            }

        } catch (IOException e) {
            throw new TemplateException(String.format("No template file found `%s`!", templateFile.getPath()));
        }
    }

}
