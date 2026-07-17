package ca.sait.aris.lims.dto.resp;

//Sprint 3
public class SampleIdRespDTO {
    private String sampleId;

    public SampleIdRespDTO() {}
    public SampleIdRespDTO(String sampleId) { this.sampleId = sampleId; }

    public String getSampleId() { return sampleId; }
    public void setSampleId(String sampleId) { this.sampleId = sampleId; }
}