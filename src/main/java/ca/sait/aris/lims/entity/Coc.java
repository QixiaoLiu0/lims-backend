package ca.sait.aris.lims.entity;

import java.util.Date;

public class Coc {
    private String cocId;
    private String cocNumber;
    private String projectName;
    private String reportToName1;
    private String reportToEmail1;
    private String reportToName2;
    private String reportToEmail2;
    private Date dateRequired;
    private Integer isRush;
    private Date dateForRush;
    private String receivedBy;
    private Date receivedTime;
    private String relinquishedBy;
    private Date relinquishedTime;
    private Integer numberOfContainers;
    private String specialInstructions;
    private String createdByUserId;
    private Date createdAt;
    private String status;

    public Coc() {}

    public String getCocId() { return cocId; }
    public void setCocId(String cocId) { this.cocId = cocId; }
    public String getCocNumber() { return cocNumber; }
    public void setCocNumber(String cocNumber) { this.cocNumber = cocNumber; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getReportToName1() { return reportToName1; }
    public void setReportToName1(String reportToName1) { this.reportToName1 = reportToName1; }
    public String getReportToEmail1() { return reportToEmail1; }
    public void setReportToEmail1(String reportToEmail1) { this.reportToEmail1 = reportToEmail1; }
    public String getReportToName2() { return reportToName2; }
    public void setReportToName2(String reportToName2) { this.reportToName2 = reportToName2; }
    public String getReportToEmail2() { return reportToEmail2; }
    public void setReportToEmail2(String reportToEmail2) { this.reportToEmail2 = reportToEmail2; }
    public Date getDateRequired() { return dateRequired; }
    public void setDateRequired(Date dateRequired) { this.dateRequired = dateRequired; }
    public Integer getIsRush() { return isRush; }
    public void setIsRush(Integer isRush) { this.isRush = isRush; }
    public Date getDateForRush() { return dateForRush; }
    public void setDateForRush(Date dateForRush) { this.dateForRush = dateForRush; }
    public String getReceivedBy() { return receivedBy; }
    public void setReceivedBy(String receivedBy) { this.receivedBy = receivedBy; }
    public Date getReceivedTime() { return receivedTime; }
    public void setReceivedTime(Date receivedTime) { this.receivedTime = receivedTime; }
    public String getRelinquishedBy() { return relinquishedBy; }
    public void setRelinquishedBy(String relinquishedBy) { this.relinquishedBy = relinquishedBy; }
    public Date getRelinquishedTime() { return relinquishedTime; }
    public void setRelinquishedTime(Date relinquishedTime) { this.relinquishedTime = relinquishedTime; }
    public Integer getNumberOfContainers() { return numberOfContainers; }
    public void setNumberOfContainers(Integer numberOfContainers) { this.numberOfContainers = numberOfContainers; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}