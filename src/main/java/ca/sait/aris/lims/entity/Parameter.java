package ca.sait.aris.lims.entity;

public class Parameter {
    private Integer parameterId;
    private Integer testTypeId; // FK
    private String parameterName;
    private String unit;
    private String limit;

    
    public Parameter() {}

    // Full constructor data manipulation
    public Parameter(Integer parameterId, Integer testTypeId, String parameterName, String unit, String limit) {
        this.parameterId = parameterId;
        this.testTypeId = testTypeId;
        this.parameterName = parameterName;
        this.unit = unit;
        this.limit = limit;
    }


    // --- Getters & Setters ---
    
    
    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Integer getTestTypeId() {
        return testTypeId;
    }

    public void setTestTypeId(Integer testTypeId) {
        this.testTypeId = testTypeId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getlimit() {
        return limit;
    }

    public void setlimit(String limit) {
        this.limit = limit;
    }
}
