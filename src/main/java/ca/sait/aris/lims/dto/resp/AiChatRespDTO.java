package ca.sait.aris.lims.dto.resp;

import java.util.List;

public class AiChatRespDTO {
    private String toolUsed;
    private List<?> results;
    private String message;

    public AiChatRespDTO() {}

    public String getToolUsed() { return toolUsed; }
    public void setToolUsed(String toolUsed) { this.toolUsed = toolUsed; }
    public List<?> getResults() { return results; }
    public void setResults(List<?> results) { this.results = results; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}