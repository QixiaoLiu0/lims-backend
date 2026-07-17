package ca.sait.aris.lims.entity;

import java.util.Date;

public class Result {
    private String resultId;
    private String testId;
    private Integer parameterId;
    private String value;
    private Date createdAt;
    private String createdByUserId;
    private String updatedByUserId;
    private Date updatedAt;
    private String qualifier;

    public Result() {}

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }
    public Integer getParameterId() { return parameterId; }
    public void setParameterId(Integer parameterId) { this.parameterId = parameterId; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public String getUpdatedByUserId() { return updatedByUserId; }
    public void setUpdatedByUserId(String updatedByUserId) { this.updatedByUserId = updatedByUserId; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getQualifier() { return qualifier; }
    public void setQualifier(String qualifier) { this.qualifier = qualifier; }
}