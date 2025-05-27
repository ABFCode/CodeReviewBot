package com.github.abfcode.codereviewbot.service;

import com.github.abfcode.codereviewbot.dto.CommitListDTO;
import com.github.abfcode.codereviewbot.dto.PullRequestDTO;
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

    public PullRequestDTO getPullRequestDetails(String pullRequestId) {
        String path = String.format("/repositories/%s/%s/pullrequests/%s", workspace, repo, pullRequestId);

        log.info("Getting raw PR details from Bitbucket API at {}", path);
        log.info("Using workspace {}, repo {}", workspace, repo);
        log.info("Raw Pull Request is: {}", restClient.get().uri(path).retrieve().body(String.class));

        try{
            PullRequestDTO pullRequest = restClient.get()
                    .uri(path)
                    .retrieve()
                    .body(PullRequestDTO.class);
            log.info("Got raw PR details from Bitbucket API");
            log.info("PR details: {}", pullRequest);
            return pullRequest;
        }
        catch (RestClientException e) {
            log.error("Error fetching PR details for ID {}: {}", pullRequestId, e.getMessage(), e);
            return null;
        }
    }

    public CommitListDTO getCommits(String commitsUrl) {
        if(commitsUrl == null || commitsUrl.isBlank()) {
            log.warn("Commits URL is null or blank. Skipping.");
            return null;
        }

        log.info("Fetching commits from Bitbucket API at {}", commitsUrl);
        try{

            CommitListDTO commits =  restClient.get()
                    .uri(commitsUrl)
                    .retrieve()
                    .body(CommitListDTO.class);
            return commits;
        }
        catch (RestClientException e) {
            log.error("Error fetching commits: {}", e.getMessage(), e);
            return null;
        }
    }

    public String getDiff(String diffUrl) {
        if(diffUrl == null || diffUrl.isBlank()) {
            log.warn("Diff URL is null or blank. Skipping.");
            return null;
        }
        log.info("Fetching diff from Bitbucket API at {}", diffUrl);
        try{
            String diffContent = restClient.get().uri(diffUrl).retrieve().body(String.class);
            log.info("Diff fetched successfully");
            return diffContent;
        }
        catch (RestClientException e) {
            log.error("Error fetching diff: {}", e.getMessage(), e);
            return null;
        }
    }

}
