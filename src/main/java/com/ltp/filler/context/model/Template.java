package com.ltp.filler.context.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Template {
    private final String name;
    private final String description;
    private final int start;
    private final int end;
    private final int size;

    public boolean touches(int start, int end){
        return end > (this.start) && start < (this.start + 6 + name.length() + description.length() + Integer.toString(size).length());
    }

    @Override
    public String toString(){
        int currSize = 6 + name.length() + description.length() + Integer.toString(size).length();
        return String.format("{{%s,%s,%d%s}}", name, description, size, size > currSize ? "_".repeat(size - currSize) : "");
    }

}
