package com.ltp.filler.context.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    private String name;
    private String description;
    private int start;
    private int end;
    private int size;
    private int replaceSize;
    private int actualStart;
    private int actualEnd;

    public boolean touches(int start, int end){
        return end > (this.start) && start < (this.start + 6 + name.length() + description.length() + Integer.toString(size).length());
    }

    public void increaseSize(Template template){
        int increaseValue = Math.abs(template.getSize() - template.toString().length());
        start += increaseValue;
        end += increaseValue;
    }

    @Override
    public String toString(){
        int currSize = 6 + name.length() + description.length() + Integer.toString(size).length();
        return String.format("{{%s,%s,%d%s}}", name, description, size, size > currSize ? "_".repeat(size - currSize) : "");
    }

}
