package com.uuzu.chinadep.taskmapper;

import com.uuzu.chinadep.taskpojo.JobAndTrigger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAndTriggerMapper {
    public List<JobAndTrigger> getJobAndTriggerDetails();
}
