package com.kamlesh.requestprocessor.scheduler;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import com.kamlesh.requestprocessor.service.ExternalApiService;
import com.kamlesh.requestprocessor.service.QueueService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class QueueProcessor {

    private final QueueService queueService;
    private final ExternalApiService externalApiService;

    public QueueProcessor(QueueService queueService, ExternalApiService externalApiService) {
        this.queueService = queueService;
        this.externalApiService = externalApiService;
    }

    @Scheduled(fixedDelay = 20000) // every 5 sec
    public void processQueue() {
        ProcessingJobRequestDto job;

        while ((job = queueService.poll()) != null) {
            externalApiService.sendToExternal(job);
        }
    }


}
