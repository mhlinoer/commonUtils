package com.linoer.app.model;

/**
 * arg传参的model
 */
public class OptionModel {
    // 参数缩略名
    private String abbreviatedName;
    // 参数全名
    private String fullName;
    // 是否有参数值
    private boolean hasArg;
    // 参数描述
    private String description;
    // 是否为必须参数
    private boolean required = false;
    // 参数值
    private String argValue;

    public String getArgValue() {
        return argValue;
    }

    public void setArgValue(String argValue) {
        this.argValue = argValue;
    }

    public OptionModel(String abbreviatedName, String fullName, boolean hasArg) {
        this.abbreviatedName = abbreviatedName;
        this.fullName = fullName;
        this.hasArg = hasArg;
    }

    public OptionModel(String abbreviatedName, String fullName, boolean hasArg, boolean required) {
        this.abbreviatedName = abbreviatedName;
        this.fullName = fullName;
        this.hasArg = hasArg;
        this.required = required;
    }

    @Override
    public String toString() {
        return "OptionModel{" +
                "abbreviatedName='" + abbreviatedName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", hasArg=" + hasArg +
                ", description='" + description + '\'' +
                ", required=" + required +
                ", argValue='" + argValue + '\'' +
                '}';
    }

    public OptionModel(String abbreviatedName, String fullName, boolean hasArg, String description, boolean required) {
        this.abbreviatedName = abbreviatedName;
        this.fullName = fullName;
        this.hasArg = hasArg;
        this.description = description;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isHasArg() {
        return hasArg;
    }

    public void setHasArg(boolean hasArg) {
        this.hasArg = hasArg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
