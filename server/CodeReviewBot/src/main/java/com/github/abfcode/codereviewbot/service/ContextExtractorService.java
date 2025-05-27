package com.github.abfcode.codereviewbot.service;


import com.github.abfcode.codereviewbot.dto.CommitListDTO;
import com.github.abfcode.codereviewbot.dto.LinksDto;
import com.github.abfcode.codereviewbot.dto.PullRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContextExtractorService {

    private final BitbucketApiService bitbucketApiService;

    public String extractContext(PullRequestDTO pullRequestDTO) {
        log.info("Extracting Context for PR ID: {}", pullRequestDTO.id());
        LinksDto links = pullRequestDTO.links();
        String commitsLinkHref = links.commits().href();
        String diffLinkHref = links.diff().href();


        log.info("Diff link href: {}", diffLinkHref);

        CommitListDTO commits = bitbucketApiService.getCommits(commitsLinkHref);
        String diff = bitbucketApiService.getDiff(diffLinkHref);
        log.info("Diff is: {}", diff);
        log.info("Links are: {}", links);
        return commits.toString();
        //return diff;
    }

}
