package ca.sait.aris.lims.entity;

public class SampleType {
    private Integer sampleTypeId;
    private String sampleTypeName;

    public SampleType() {}

    public Integer getSampleTypeId() { return sampleTypeId; }
    public void setSampleTypeId(Integer sampleTypeId) { this.sampleTypeId = sampleTypeId; }
    public String getSampleTypeName() { return sampleTypeName; }
    public void setSampleTypeName(String sampleTypeName) { this.sampleTypeName = sampleTypeName; }
}