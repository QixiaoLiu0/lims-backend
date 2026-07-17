package ca.sait.aris.lims.dto.req;

//Sprint 3
public class ResultUpdateReqDTO {
    private String resultId;
    private String value;
    private String qualifier;

    public ResultUpdateReqDTO() {}

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getQualifier() { return qualifier; }
    public void setQualifier(String qualifier) { this.qualifier = qualifier; }
}