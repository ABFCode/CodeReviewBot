package com.github.abfcode.codereviewbot;

import com.github.abfcode.codereviewbot.dto.PullRequestDTO;
import com.github.abfcode.codereviewbot.service.BitbucketApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class CodeReviewBotApplication implements CommandLineRunner {

    private final BitbucketApiService bitbucketApiService;

    public static void main(String[] args) {
        SpringApplication.run(CodeReviewBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String testPullRequestId = "1";

        if(args.length > 0) {
            testPullRequestId = args[0];
            log.info("Running with pull request ID {}", testPullRequestId);
        }
        else{
            log.info("Running with default pull request ID {}", testPullRequestId);
        }

        log.info("Attempting to fetch PR details for ID {}", testPullRequestId);
        PullRequestDTO pullRequest = bitbucketApiService.getPullRequestDetails(testPullRequestId);

        if (pullRequest != null) {
            log.info("\n--- DESERIALIZED PULL REQUEST DTO ---\n{}", pullRequest.toString());
            log.info("PR Title: {}", pullRequest.title());
            log.info("PR Author: {}", pullRequest.author() != null ? pullRequest.author().displayName() : "N/A");

        }
        log.info("--- END OF DTO TEST ---");
    }
}
