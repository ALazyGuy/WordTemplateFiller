package com.ltp.filler.context.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DocumentData {
    private Map<String, String> data = new HashMap<>();
}
