package com.github.abfcode.codereviewbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
@RequiredArgsConstructor
public class BitbucketApiService {

    private final RestClient restClient;

    @Value("${bitbucket.workspace}")
    private String workspace;

    @Value("${bitbucket.reposlug}")
    private String repo;

    public String getRawPRDetails(String pullRequestId) {
        String path = String.format("/repositories/%s/%s/pullrequests/%s", workspace, repo, pullRequestId);

        log.info("Getting raw PR details from Bitbucket API at {}", path);
        log.info("Using workspace {}, repo {}", workspace, repo);

        try{
            String responseBody = restClient.get()
                    .uri(path)
                    .retrieve()
                    .body(String.class);
            log.info("Got raw PR details from Bitbucket API");
            return responseBody;
        }
        catch (RestClientException e) {
            log.error("Error fetching PR details for ID {}: {}", pullRequestId, e.getMessage(), e);
            return "Error: Could not fetch PR details." + e.getMessage();
        }
    }

}
