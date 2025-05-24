package com.github.abfcode.codereviewbot;

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
        String response = bitbucketApiService.getRawPRDetails(testPullRequestId);
        log.info("\n--- RAW BITBUCKET PULL REQUEST DETAILS ---\n{}", response);
        log.info("--- END OF RAW RESPONSE ---");
    }
}
