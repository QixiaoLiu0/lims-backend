package ca.sait.aris.lims.dto.resp;

//Sprint 3
public class LookupSampleTypeRespDTO {
    private Integer sampleTypeId;
    private String sampleTypeName;

    public LookupSampleTypeRespDTO() {}

    public Integer getSampleTypeId() { return sampleTypeId; }
    public void setSampleTypeId(Integer sampleTypeId) { this.sampleTypeId = sampleTypeId; }
    public String getSampleTypeName() { return sampleTypeName; }
    public void setSampleTypeName(String sampleTypeName) { this.sampleTypeName = sampleTypeName; }
}