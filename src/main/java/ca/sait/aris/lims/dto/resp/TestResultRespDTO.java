package ca.sait.aris.lims.dto.resp;

//Sprint 3
public class TestResultRespDTO {
    private String resultId;
    private Integer parameterId;
    private String parameterName;
    private String unit;
    private String limit;
    private String value;
    private String qualifier;

    public TestResultRespDTO() {}

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public Integer getParameterId() { return parameterId; }
    public void setParameterId(Integer parameterId) { this.parameterId = parameterId; }
    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getLimit() { return limit; }
    public void setLimit(String limit) { this.limit = limit; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getQualifier() { return qualifier; }
    public void setQualifier(String qualifier) { this.qualifier = qualifier; }
}