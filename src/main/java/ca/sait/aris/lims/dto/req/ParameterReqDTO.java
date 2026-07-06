package ca.sait.aris.lims.dto.req;

public class ParameterReqDTO {
	
	private Integer parameterId;
    private String parameterName;
    private String unit;
    private String limit;

    // --- Getters & Setters ---
    
    public String getParameterName() {
        return parameterName;
    }
    public Integer getParameterId() {
		return parameterId;
	}
	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getLimit() {
        return limit;
    }
    public void setLimit(String limit) {
        this.limit = limit;
    }
}
