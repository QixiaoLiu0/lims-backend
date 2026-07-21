package ca.sait.aris.lims.dao;

import ca.sait.aris.lims.dto.resp.CocDetailSampleRespDTO;
import ca.sait.aris.lims.entity.Sample;
import java.util.ArrayList;
import java.util.List;

public class SampleDao extends BaseJdbcDao {

    // Do not insert the status field
    public void insertSample(Sample sample) throws Exception {
        String sql = "INSERT INTO sample (sample_id, coc_id, sample_type_id, sample_client_id, sampled_time, " +
                "sampling_point, matrix, number_of_containers, remarks, initial_volume, remaining_volume, " +
                "created_at, is_filtered, is_preserved, is_filtered_and_preserved) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql,
                sample.getSampleId(),
                sample.getCocId(),
                sample.getSampleTypeId(),
                sample.getSampleClientId(),
                toTimestamp(sample.getSampledTime()),
                sample.getSamplingPoint(),
                sample.getMatrix(),
                sample.getNumberOfContainers(),
                sample.getRemarks(),
                sample.getInitialVolume(),
                sample.getRemainingVolume(),
                toTimestamp(sample.getCreatedAt()),
                sample.getIsFiltered(),
                sample.getIsPreserved(),
                sample.getIsFilteredAndPreserved()
        );
    }

    public void deleteSampleById(String sampleId) throws Exception {
    	//TODO
    }

    public Sample selectSampleById(String sampleId) throws Exception {
    	//TODO
		return null;
        
    }

    // Aggregate SQL: Query the Sample list and Test info statistics for the coc details page.
    public List<CocDetailSampleRespDTO> selectSamplesByCocId(String cocId) throws Exception {
    	//TODO
		return null;
    }

    // Defense against N+1: Use the IN statement to retrieve all samples from multiple COCs at once.
    public List<Sample> selectSamplesByCocIds(List<String> cocIds) throws Exception {
    	//TODO
		return null;
        
    }

    // status Bubble Update
    public void updateSampleStatus(String sampleId, String status) throws Exception {
    	//TODO
    }

    // Check if there are any incomplete Samples under a given COC.
    public int countIncompleteSamplesByCocId(String cocId) throws Exception {
    	//TODO
		return 0;
        
    }
}