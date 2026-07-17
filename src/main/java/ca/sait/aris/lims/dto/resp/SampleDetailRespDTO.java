package ca.sait.aris.lims.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//Sprint 3
public class SampleDetailRespDTO {
    private String sampleId;
    private String cocId;
    private Integer sampleTypeId;
    private String sampleClientId;
    private LocalDateTime sampledTime;
    private String samplingPoint;
    private String matrix;
    private Integer numberOfContainers;
    private String remarks;
    private BigDecimal initialVolume;
    private BigDecimal remainingVolume;
    private LocalDateTime createdAt;
    private Integer isFiltered;
    private Integer isPreserved;
    private Integer isFilteredAndPreserved;
    private String status;
    private List<SampleDetailTestRespDTO> tests;

    public SampleDetailRespDTO() {}

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
    public String getCocId() { return cocId; }
    public void setCocId(String cocId) { this.cocId = cocId; }
    public Integer getSampleTypeId() { return sampleTypeId; }
    public void setSampleTypeId(Integer sampleTypeId) { this.sampleTypeId = sampleTypeId; }
    public String getSampleClientId() { return sampleClientId; }
    public void setSampleClientId(String sampleClientId) { this.sampleClientId = sampleClientId; }
    public LocalDateTime getSampledTime() { return sampledTime; }
    public void setSampledTime(LocalDateTime sampledTime) { this.sampledTime = sampledTime; }
    public String getSamplingPoint() { return samplingPoint; }
    public void setSamplingPoint(String samplingPoint) { this.samplingPoint = samplingPoint; }
    public String getMatrix() { return matrix; }
    public void setMatrix(String matrix) { this.matrix = matrix; }
    public Integer getNumberOfContainers() { return numberOfContainers; }
    public void setNumberOfContainers(Integer numberOfContainers) { this.numberOfContainers = numberOfContainers; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public BigDecimal getInitialVolume() { return initialVolume; }
    public void setInitialVolume(BigDecimal initialVolume) { this.initialVolume = initialVolume; }
    public BigDecimal getRemainingVolume() { return remainingVolume; }
    public void setRemainingVolume(BigDecimal remainingVolume) { this.remainingVolume = remainingVolume; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getIsFiltered() { return isFiltered; }
    public void setIsFiltered(Integer isFiltered) { this.isFiltered = isFiltered; }
    public Integer getIsPreserved() { return isPreserved; }
    public void setIsPreserved(Integer isPreserved) { this.isPreserved = isPreserved; }
    public Integer getIsFilteredAndPreserved() { return isFilteredAndPreserved; }
    public void setIsFilteredAndPreserved(Integer isFilteredAndPreserved) { this.isFilteredAndPreserved = isFilteredAndPreserved; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<SampleDetailTestRespDTO> getTests() { return tests; }
    public void setTests(List<SampleDetailTestRespDTO> tests) { this.tests = tests; }
}