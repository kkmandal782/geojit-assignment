package com.kamlesh.requestprocessor.service;

import com.kamlesh.requestprocessor.dto.ProcessingJobRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final Logger log =
            LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate;

    @Value("${EXTERNAL_API_URL}")
    private String externalApiUrl;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void sendToExternal(ProcessingJobRequestDto job) {
        try {
            log.info("Sending job to external API with name={} to URL={}",
                    job.getName(), externalApiUrl);

            String response = restTemplate.postForObject(
                    externalApiUrl,
                    job,
                    String.class
            );

            log.info("Received response from external API for job name={}", job.getName());
            log.debug("External API response: {}", response);

        } catch (Exception e) {
            log.error("Failed to send job to external API with name={} to URL={}",
                    job.getName(), externalApiUrl, e);
        }
    }
}