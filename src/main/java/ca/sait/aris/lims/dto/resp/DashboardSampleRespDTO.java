package ca.sait.aris.lims.dto.resp;


//Sprint 3
public class DashboardSampleRespDTO {
    private String sampleId;
    private String sampleClientId;
    private String matrix;

    public DashboardSampleRespDTO() {}

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
    public String getSampleClientId() { return sampleClientId; }
    public void setSampleClientId(String sampleClientId) { this.sampleClientId = sampleClientId; }
    public String getMatrix() { return matrix; }
    public void setMatrix(String matrix) { this.matrix = matrix; }
}