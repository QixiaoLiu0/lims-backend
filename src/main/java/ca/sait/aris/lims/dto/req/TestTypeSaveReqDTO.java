package ca.sait.aris.lims.dto.req;

import java.math.BigDecimal;
import java.util.List;

/**
 * Encapsulate the request payload for frontend creation/update of test types (including the main table and sub-parameter list).
 */
public class TestTypeSaveReqDTO {
	// --- master (test_type) fields ---
    private String typeName;
    private String description;
    private BigDecimal requiredVolume;
    private String bgColor;
    private String iconColor;
    private String borderColor;
    private Integer isActive;

    private List<ParameterReqDTO> parameters; //reference to static inner class down below

    // --- Getters & Setters (master) ---
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
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public List<ParameterReqDTO> getParameters() { return parameters; }
    public void setParameters(List<ParameterReqDTO> parameters) { this.parameters = parameters; }
    

    
}
