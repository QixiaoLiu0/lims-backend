package ca.sait.aris.lims.dto.resp;

import java.util.List;

public class TestTypeRespDTO extends TestTypeListRespDTO {

    
    private List<ParameterRespDTO> parameters;

    
    
    
    
    
    // setters & getters
    public List<ParameterRespDTO> getParameters() {return parameters;}

    public void setParameters(List<ParameterRespDTO> parameters) {this.parameters = parameters;}

   
}
