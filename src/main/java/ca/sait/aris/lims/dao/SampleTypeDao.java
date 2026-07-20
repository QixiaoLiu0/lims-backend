package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.LookupSampleTypeRespDTO;
import java.util.List;

public class SampleTypeDao extends BaseJdbcDao {

    public List<LookupSampleTypeRespDTO> selectAllSampleTypes() throws Exception {
        String sql = "SELECT sample_type_id, sample_type_name FROM sample_type ORDER BY sample_test_name";

        return executeQuery(sql, LookupSampleTypeRespDTO.class);
    }
}