package ca.sait.aris.lims.dto.req;

import java.util.List;

//Sprint 3
public class ResultBatchSaveReqDTO {
    private Boolean isComplete;
    private List<ResultUpdateReqDTO> results;

    public ResultBatchSaveReqDTO() {}

    public Boolean getIsComplete() { return isComplete; }
    public void setIsComplete(Boolean isComplete) { this.isComplete = isComplete; }
    public List<ResultUpdateReqDTO> getResults() { return results; }
    public void setResults(List<ResultUpdateReqDTO> results) { this.results = results; }
}