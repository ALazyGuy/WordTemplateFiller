package com.ltp.filler.context.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemplateAction {
    private final Template template;
    private final String text;
}
