package ca.sait.aris.lims.dto.resp;

import java.math.BigDecimal;

//Sprint 3
public class LookupTestTypeRespDTO {
    private Integer testTypeId;
    private String typeName;
    private String description;
    private BigDecimal requiredVolume;
    private String bgColor;
    private String iconColor;
    private String borderColor;

    public LookupTestTypeRespDTO() {}

    public Integer getTestTypeId() { return testTypeId; }
    public void setTestTypeId(Integer testTypeId) { this.testTypeId = testTypeId; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getRequiredVolume() { return requiredVolume; }
    public void setRequiredVolume(BigDecimal requiredVolume) { this.requiredVolume = requiredVolume; }
    public String getBgColor() { return bgColor; }
    public void setBgColor(String bgColor) { this.bgColor = bgColor; }
    public String getIconColor() { return iconColor; }
    public void setIconColor(String iconColor) { this.iconColor = iconColor; }
    public String getBorderColor() { return borderColor; }
    public void setBorderColor(String borderColor) { this.borderColor = borderColor; }
}