package ca.sait.aris.lims.dto.resp;

//Sprint 3
public class CocIdRespDTO {
    private String cocId;

    public CocIdRespDTO() {}
    public CocIdRespDTO(String cocId) { this.cocId = cocId; }

    public String getCocId() { return cocId; }
    public void setCocId(String cocId) { this.cocId = cocId; }
}