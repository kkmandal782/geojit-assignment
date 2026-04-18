package com.kamlesh.requestprocessor.repository;

import com.kamlesh.requestprocessor.model.ProcessingJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, UUID> {
    boolean existsByJobName(String name);

}
