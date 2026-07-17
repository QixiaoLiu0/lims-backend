package ca.sait.aris.lims.dto.req;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//sprint 3
public class SampleSaveReqDTO {
    private Integer sampleTypeId;
    private String sampleClientId;
    private LocalDateTime sampledTime;
    private String samplingPoint;
    private String matrix;
    private Integer numberOfContainers;
    private String remarks;
    private BigDecimal initialVolume;
    private BigDecimal remainingVolume;
    private Integer isFiltered;
    private Integer isPreserved;
    private Integer isFilteredAndPreserved;
    private List<Integer> testTypeIds;

    public SampleSaveReqDTO() {}

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
    public Integer getIsFiltered() { return isFiltered; }
    public void setIsFiltered(Integer isFiltered) { this.isFiltered = isFiltered; }
    public Integer getIsPreserved() { return isPreserved; }
    public void setIsPreserved(Integer isPreserved) { this.isPreserved = isPreserved; }
    public Integer getIsFilteredAndPreserved() { return isFilteredAndPreserved; }
    public void setIsFilteredAndPreserved(Integer isFilteredAndPreserved) { this.isFilteredAndPreserved = isFilteredAndPreserved; }
    public List<Integer> getTestTypeIds() { return testTypeIds; }
    public void setTestTypeIds(List<Integer> testTypeIds) { this.testTypeIds = testTypeIds; }
}