package cn.ffcs.uom.bpm.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class FlowPersonalInfo {
    
    @JsonProperty("FIELD_NAME")
    private String fieldName;
    @JsonProperty("FIELD_VALUE")
    private String fieldValue;
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldValue() {
        return fieldValue;
    }
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
 
}
