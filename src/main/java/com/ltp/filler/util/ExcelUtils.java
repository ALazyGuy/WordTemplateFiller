package com.ltp.filler.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ltp.filler.context.model.DocumentData;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtils {

    @SneakyThrows
    public static List<DocumentData> readExcel(File file){
        List<DocumentData> result = new LinkedList<>();

        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        boolean firstIteration = true;
        List<String> header = new LinkedList<>();

        for(Row row: sheet) {

            if(firstIteration){
                header = parseRow(row, formulaEvaluator);
                firstIteration = false;
            }else{
                List<String> data = parseRow(row, formulaEvaluator);
                DocumentData documentData = new DocumentData();
                documentData.setData(IntStream
                        .range(0, data.size())
                        .boxed()
                        .collect(Collectors.toMap(header::get, data::get)));
                result.add(documentData);
            }
        }

        return result;
    }

    private static List<String> parseRow(Row row, FormulaEvaluator formulaEvaluator){
        List<String> result = new LinkedList<>();

        for (Cell cell : row){
            switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                case NUMERIC:
                    result.add(Double.toString(cell.getNumericCellValue()));
                    break;
                case STRING:
                    result.add(cell.getStringCellValue());
                    break;
            }
        }

        return result;
    }

}
