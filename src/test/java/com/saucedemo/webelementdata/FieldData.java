package com.saucedemo.webelementdata;

public class FieldData {
    private String fieldName;
    private String caseType;
    private int numberOfChars;
    private String valueType;
    private String fieldValue;

    public FieldData(String fieldValue, String fieldName) {
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }

    public FieldData(String fieldName, String caseType, int numberOfChars, String valueType) {
        this.fieldName = fieldName;
        this.caseType = caseType;
        this.numberOfChars = numberOfChars;
        this.valueType = valueType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getCaseType() {
        return caseType;
    }

    public int getNumberOfChars() {
        return numberOfChars;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getValueType() {
        return valueType;
    }

}
