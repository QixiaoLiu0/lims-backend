package ca.sait.aris.lims.entity;

import java.math.BigDecimal;

/**
 * Entity mapping strictly to database table 'test_type'.
 */
public class TestType {
    private Integer testTypeId;
    private String typeName;
    private String description;
    private BigDecimal requiredVolume;
    private String bgColor;
    private String iconColor;
    private String borderColor;
    private Integer isActive;

    
    public TestType() {
    }
    
    public TestType(Integer testTypeId, String typeName, String description, BigDecimal requiredVolume, 
                    String bgColor, String iconColor, String borderColor, Integer isActive) {
        this.testTypeId = testTypeId;
        this.typeName = typeName;
        this.description = description;
        this.requiredVolume = requiredVolume;
        this.bgColor = bgColor;
        this.iconColor = iconColor;
        this.borderColor = borderColor;
        this.isActive = isActive;
    }

    
    // --- Getters & Setters ---
    
    
    
    public Integer getTestTypeId() {
        return testTypeId;
    }

    public void setTestTypeId(Integer testTypeId) {
        this.testTypeId = testTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRequiredVolume() {
        return requiredVolume;
    }

    public void setRequiredVolume(BigDecimal requiredVolume) {
        this.requiredVolume = requiredVolume;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
