package ca.sait.aris.lims.entity;

import java.util.Date;

public class Test {
    private String testId;
    private String sampleId;
    private Integer testTypeId;
    private String status;
    private Date createdAt;
    private Integer runNumber;
    private String retestReason;

    public Test() {}

    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }
    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
    public Integer getTestTypeId() { return testTypeId; }
    public void setTestTypeId(Integer testTypeId) { this.testTypeId = testTypeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Integer getRunNumber() { return runNumber; }
    public void setRunNumber(Integer runNumber) { this.runNumber = runNumber; }
    public String getRetestReason() { return retestReason; }
    public void setRetestReason(String retestReason) { this.retestReason = retestReason; }
}