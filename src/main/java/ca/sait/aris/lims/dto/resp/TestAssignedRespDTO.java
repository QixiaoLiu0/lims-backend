package ca.sait.aris.lims.dto.resp;

//Sprint 3
public class TestAssignedRespDTO {
    private String testId;
    private Integer runNumber;

    public TestAssignedRespDTO() {}
    public TestAssignedRespDTO(String testId, Integer runNumber) {
        this.testId = testId;
        this.runNumber = runNumber;
    }

    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }
    public Integer getRunNumber() { return runNumber; }
    public void setRunNumber(Integer runNumber) { this.runNumber = runNumber; }
}