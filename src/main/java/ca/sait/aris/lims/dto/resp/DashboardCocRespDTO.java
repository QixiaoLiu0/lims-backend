package ca.sait.aris.lims.dto.resp;

import java.time.LocalDateTime;
import java.util.List;

//Sprint 3
public class DashboardCocRespDTO {
    private String cocId;
    private String cocNumber;
    private String projectName;
    private String status;
    private LocalDateTime receivedTime;
    private LocalDateTime dateRequired;
    private Integer totalTests;
    private Integer completedTests;
    private List<DashboardSampleRespDTO> samples;

    public DashboardCocRespDTO() {}

    public String getCocId() { return cocId; }
    public void setCocId(String cocId) { this.cocId = cocId; }
    public String getCocNumber() { return cocNumber; }
    public void setCocNumber(String cocNumber) { this.cocNumber = cocNumber; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getReceivedTime() { return receivedTime; }
    public void setReceivedTime(LocalDateTime receivedTime) { this.receivedTime = receivedTime; }
    public LocalDateTime getDateRequired() { return dateRequired; }
    public void setDateRequired(LocalDateTime dateRequired) { this.dateRequired = dateRequired; }
    public Integer getTotalTests() { return totalTests; }
    public void setTotalTests(Integer totalTests) { this.totalTests = totalTests; }
    public Integer getCompletedTests() { return completedTests; }
    public void setCompletedTests(Integer completedTests) { this.completedTests = completedTests; }
    public List<DashboardSampleRespDTO> getSamples() { return samples; }
    public void setSamples(List<DashboardSampleRespDTO> samples) { this.samples = samples; }
}