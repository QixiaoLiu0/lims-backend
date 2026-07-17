package ca.sait.aris.lims.dto.resp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Sprint 3
public class CocDetailSampleRespDTO {
    private String sampleId;
    private String sampleClientId;
    private String matrix;
    private String samplingPoint;
    private LocalDateTime sampledTime;
    private BigDecimal initialVolume;
    private BigDecimal remainingVolume;
    private String status;
    private Integer totalTests;

    public CocDetailSampleRespDTO() {}

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
    public String getSampleClientId() { return sampleClientId; }
    public void setSampleClientId(String sampleClientId) { this.sampleClientId = sampleClientId; }
    public String getMatrix() { return matrix; }
    public void setMatrix(String matrix) { this.matrix = matrix; }
    public String getSamplingPoint() { return samplingPoint; }
    public void setSamplingPoint(String samplingPoint) { this.samplingPoint = samplingPoint; }
    public LocalDateTime getSampledTime() { return sampledTime; }
    public void setSampledTime(LocalDateTime sampledTime) { this.sampledTime = sampledTime; }
    public BigDecimal getInitialVolume() { return initialVolume; }
    public void setInitialVolume(BigDecimal initialVolume) { this.initialVolume = initialVolume; }
    public BigDecimal getRemainingVolume() { return remainingVolume; }
    public void setRemainingVolume(BigDecimal remainingVolume) { this.remainingVolume = remainingVolume; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTotalTests() { return totalTests; }
    public void setTotalTests(Integer totalTests) { this.totalTests = totalTests; }
}