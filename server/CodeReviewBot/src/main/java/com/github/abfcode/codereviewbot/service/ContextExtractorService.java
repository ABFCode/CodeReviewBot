package com.github.abfcode.codereviewbot.service;


import com.github.abfcode.codereviewbot.dto.CommitListDTO;
import com.github.abfcode.codereviewbot.dto.LinkDTO;
import com.github.abfcode.codereviewbot.dto.PullRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContextExtractorService {

    private final BitbucketApiService bitbucketApiService;

    public CommitListDTO extractContext(PullRequestDTO pullRequestDTO) {
        log.info("Extracting Context for PR ID: {}", pullRequestDTO.id());
        LinkDTO commitsLink = pullRequestDTO.links().commits();

        CommitListDTO commits = bitbucketApiService.getCommits(commitsLink.href());
        return commits;
    }

}
