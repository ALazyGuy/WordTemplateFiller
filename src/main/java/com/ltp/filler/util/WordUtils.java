package com.ltp.filler.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

}
