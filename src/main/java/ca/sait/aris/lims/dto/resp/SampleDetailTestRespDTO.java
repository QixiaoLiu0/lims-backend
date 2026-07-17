package ca.sait.aris.lims.dto.resp;

import java.time.LocalDateTime;

//Sprint 3
public class SampleDetailTestRespDTO {
    private String testId;
    private Integer testTypeId;
    private String typeName;
    private Integer runNumber;
    private String retestReason;
    private String status;
    private LocalDateTime createdAt;

    public SampleDetailTestRespDTO() {}

    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }
    public Integer getTestTypeId() { return testTypeId; }
    public void setTestTypeId(Integer testTypeId) { this.testTypeId = testTypeId; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Integer getRunNumber() { return runNumber; }
    public void setRunNumber(Integer runNumber) { this.runNumber = runNumber; }
    public String getRetestReason() { return retestReason; }
    public void setRetestReason(String retestReason) { this.retestReason = retestReason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}