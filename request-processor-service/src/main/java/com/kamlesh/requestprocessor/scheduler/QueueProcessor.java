package com.kamlesh.requestprocessor.scheduler;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.service.ExternalApiService;
import com.kamlesh.requestprocessor.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class QueueProcessor {

    private static final Logger log = LoggerFactory.getLogger(QueueProcessor.class);

    private final QueueService queueService;
    private final ExternalApiService externalApiService;

    public QueueProcessor(QueueService queueService, ExternalApiService externalApiService) {
        this.queueService = queueService;
        this.externalApiService = externalApiService;
    }

    @Scheduled(fixedDelay = 5000) // every 5 sec
    public void processQueue() {

        log.info("Queue processing started");

        ProcessingJobRequestDto job;
        int processedCount = 0;

        while ((job = queueService.poll()) != null) {
            try {
                log.debug("Processing job from queue: name={}", job.getName());

                externalApiService.sendToExternal(job);

                processedCount++;

                log.debug("Successfully processed job: name={}", job.getName());

            } catch (Exception e) {
                log.error("Failed to process job: name={}, error={}", job.getName(), e.getMessage(), e);
            }
        }

        if (processedCount == 0) {
            log.info("Queue processing completed - no jobs found");
        } else {
            log.info("Queue processing completed - totalProcessed={}", processedCount);
        }
    }
}